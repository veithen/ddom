/*
 * Copyright 2009 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.ddom.backend.linkedlist;

import java.util.Iterator;

import com.google.code.ddom.backend.Axis;
import com.google.code.ddom.backend.ChildTypeNotAllowedException;
import com.google.code.ddom.backend.CoreCharacterData;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNSAwareElement;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.CyclicRelationshipException;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.NodeNotFoundException;
import com.google.code.ddom.backend.SelfRelationshipException;
import com.google.code.ddom.stream.spi.FragmentSource;

@Implementation
public abstract class ParentNode extends Node implements CoreParentNode {
    /**
     * The content of this node. This is a {@link CoreChildNode} if the node is expanded, a
     * {@link FragmentSource} if the content is sourced or a {@link String} if the value has been
     * set.
     */
    private Object content;
    
    // TODO: this should be merged with other flags
    private boolean complete;
    
    public ParentNode(Object content) {
        this.content = content;
        complete = true;
    }

    public ParentNode(boolean complete) {
        this.complete = complete;
    }

    public final Object coreGetContent() {
        return content;
    }

    public final boolean coreIsExpanded() {
        return content instanceof CoreChildNode;
    }

    public final void coreSetContent(FragmentSource source) {
        // TODO: need to clear any existing content!
        complete = false;
        // TODO: getting the producer should be deferred!
        getDocument().createBuilder(source.getProducer(), this);
        // TODO: need to decide how to handle symbol tables in a smart way here
//        symbols = producer.getSymbols();
    }

    public final String coreGetValue() throws DeferredParsingException {
        // TODO: this should also be applicable for other OptimizedParentNodes
        if (content instanceof String) {
            return (String)content;
        } else {
            // TODO: get the getTextContent feature back into the core model
            StringBuilder buffer = new StringBuilder();
            CoreChildNode child = (CoreChildNode)content;
            while (child != null) {
                buffer.append(((CoreCharacterData)child).coreGetData());
                child = child.coreGetNextSibling();
            }
            return buffer.toString();
//            return getTextContent();
        }
    }

    public final void coreSetValue(String value) {
        // TODO: what if arg is null?
        // TODO: need to remove any existing children!
        content = value;
    }

    public final CoreChildNode internalGetFirstChild() {
        return (CoreChildNode)content;
    }

    public final void internalSetFirstChild(CoreChildNode child) {
        content = child;
    }

    public final boolean coreIsComplete() {
        return complete;
    }

    public final void internalSetComplete() {
        complete = true;
    }
    
    public final void coreBuild() throws DeferredParsingException {
        if (!complete) {
            Builder builder = getDocument().getBuilderFor(this);
            do {
                builder.next();
            } while (!complete);
        }
    }
    
    public final CoreChildNode coreGetFirstChild() throws DeferredParsingException {
        if (content == null) {
            if (complete) {
                return null;
            } else {
                Builder builder = getDocument().getBuilderFor(this);
                while (content == null && !complete) {
                    builder.next();
                }
                return (CoreChildNode)content;
            }
        } else if (content instanceof String) {
            CoreChildNode firstChild;
            CoreDocument document = getDocument();
            firstChild = document.getNodeFactory().createText(document, (String)content);
            firstChild.internalSetParent(this);
            content = firstChild;
            return firstChild;
        } else {
            return (CoreChildNode)content;
        }
    }
    
    public final CoreChildNode coreGetLastChild() throws DeferredParsingException {
        CoreChildNode previousChild = null;
        CoreChildNode child = coreGetFirstChild();
        while (child != null) {
            previousChild = child;
            child = child.coreGetNextSibling();
        }
        return previousChild;
    }
    
    private void prepareNewChild(CoreChildNode newChild) throws CoreModelException {
        validateOwnerDocument(newChild);
        
        // Check that the new node is not an ancestor of this node
        CoreParentNode current = this;
        do {
            if (current == newChild) {
                throw new CyclicRelationshipException();
            }
            if (current instanceof CoreChildNode) {
                current = ((CoreChildNode)current).coreGetParent();
            } else {
                break;
            }
        } while (current != null);
        
        if (newChild instanceof CoreParentNode) {
            ((CoreParentNode)newChild).coreBuild();
        }
    }
    
    /**
     * Check if the given node is allowed as a child.
     * 
     * @param newChild
     *            the child that will be added
     * @param replacedChild
     *            the child that will be replaced by the new node, or <code>null</code> if the new
     *            child will be inserted and doesn't replace any existing node
     * @throws ChildTypeNotAllowedException
     *             if the child is not allowed
     * @throws DeferredParsingException 
     */
    protected abstract void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException, DeferredParsingException;
    
    // insertBefore: newChild != null, refChild != null, removeRefChild == false
    // appendChild:  newChild != null, refChild == null, removeRefChild == false
    // replaceChild: newChild != null, refChild != null, removeRefChild == true
    // removeChild:  newChild == null, refChild != null, removeRefChild == true
    private void merge(CoreNode newChild, CoreChildNode refChild, boolean removeRefChild) throws CoreModelException {
        if (newChild instanceof CoreChildNode) {
            prepareNewChild((CoreChildNode)newChild);
        }
        CoreChildNode previousSibling; // The sibling that will precede the new child
        CoreChildNode nextSibling; // The sibling that will follow the new child
        if (refChild == null) { // implies removeRefChild == false
            previousSibling = coreGetLastChild();
            nextSibling = null;
        } else {
            previousSibling = null;
            CoreChildNode node = coreGetFirstChild();
            while (node != null && node != refChild) {
                previousSibling = node;
                node = node.coreGetNextSibling();
            }
            if (node == null) {
                throw new NodeNotFoundException();
            }
            nextSibling = removeRefChild ? node.coreGetNextSibling() : node;
        }
        if (newChild == null && removeRefChild) {
            if (previousSibling == null) {
                internalSetFirstChild(nextSibling);
            } else {
                previousSibling.internalSetNextSibling(nextSibling);
            }
            notifyChildrenModified(-1);
        } else {
            CoreChildNode firstNodeToInsert;
            CoreChildNode lastNodeToInsert;
            int delta; // The difference in number of children before and after the operation
            if (newChild instanceof CoreDocumentFragment) {
                CoreDocumentFragment fragment = (CoreDocumentFragment)newChild;
                firstNodeToInsert = fragment.coreGetFirstChild();
                lastNodeToInsert = null;
                for (CoreChildNode node = firstNodeToInsert; node != null; node = node.coreGetNextSibling()) {
                    // TODO: if validateChildType throws an exception, this will leave the DOM tree in a corrupt state!
                    validateChildType(node, removeRefChild ? refChild : null);
                    node.internalSetParent(this);
                    lastNodeToInsert = node;
                }
                delta = fragment.coreGetChildCount();
                // TODO: need to clear the document fragment?
            } else if (newChild instanceof CoreChildNode) {
                ((CoreChildNode)newChild).coreDetach();
                firstNodeToInsert = lastNodeToInsert = (CoreChildNode)newChild;
                validateChildType(firstNodeToInsert, removeRefChild ? refChild : null);
                firstNodeToInsert.internalSetParent(this);
                delta = 1;
            } else {
                throw new ChildTypeNotAllowedException();
            }
            if (removeRefChild) {
                delta--;
            }
            if (delta != 0) {
                notifyChildrenModified(delta);
            }
            if (previousSibling == null) {
                internalSetFirstChild(firstNodeToInsert);
            } else {
                previousSibling.internalSetNextSibling(firstNodeToInsert);
            }
            if (nextSibling != null) {
                lastNodeToInsert.internalSetNextSibling(nextSibling);
            }
        }
        if (removeRefChild) {
            refChild.internalSetParent(null);
        }
    }

    public final void coreAppendChild(CoreNode newChild) throws CoreModelException {
        merge(newChild, null, false);
    }

    public final void coreInsertChildAfter(CoreNode newChild, CoreChildNode refChild) throws CoreModelException {
        if (newChild == refChild) {
            throw new SelfRelationshipException();
        }
        merge(newChild, refChild.coreGetNextSibling(), false);
    }

    public final void coreInsertChildBefore(CoreNode newChild, CoreChildNode refChild) throws CoreModelException {
        if (newChild == refChild) {
            throw new SelfRelationshipException();
        }
        merge(newChild, refChild, false);
    }

    public final void coreRemoveChild(CoreChildNode child) throws CoreModelException {
        merge(null, child, true);
    }

    public final void coreReplaceChild(CoreNode newChild, CoreChildNode oldChild) throws CoreModelException {
        merge(newChild, oldChild, true);
    }

    public final <T extends CoreChildNode> Iterator<T> coreGetChildrenByType(Axis axis, Class<T> type) {
        return new ChildrenByTypeIterator<T>(this, axis, type);
    }

    public final Iterator<CoreNSAwareElement> coreGetElementsByName(Axis axis, String namespaceURI, String localName) {
        return new ElementsByNameIterator(this, axis, namespaceURI, localName);
    }

    public final Iterator<CoreNSAwareElement> coreGetElementsByNamespace(Axis axis, String namespaceURI) {
        return new ElementsByNamespaceIterator(this, axis, namespaceURI);
    }

    public final Iterator<CoreNSAwareElement> coreGetElementsByLocalName(Axis axis, String localName) {
        return new ElementsByLocalNameIterator(this, axis, localName);
    }
}
