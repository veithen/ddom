/*
 * Copyright 2009-2010 Andreas Veithen
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

import com.google.code.ddom.backend.linkedlist.support.ChildrenByTypeIterator;
import com.google.code.ddom.backend.linkedlist.support.ElementsByLocalNameIterator;
import com.google.code.ddom.backend.linkedlist.support.ElementsByNameIterator;
import com.google.code.ddom.backend.linkedlist.support.ElementsByNamespaceIterator;
import com.google.code.ddom.core.Axis;
import com.google.code.ddom.core.ChildTypeNotAllowedException;
import com.google.code.ddom.core.CoreCharacterData;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.CoreNode;
import com.google.code.ddom.core.CoreParentNode;
import com.google.code.ddom.core.CyclicRelationshipException;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.NodeNotFoundException;
import com.google.code.ddom.core.ext.ModelExtension;
import com.google.code.ddom.stream.spi.FragmentSource;

public abstract class ParentNode extends Node implements LLParentNode {
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

    public final void coreSetContent(FragmentSource source, ModelExtension modelExtension) {
        // TODO: need to clear any existing content!
        complete = false;
        // TODO: getting the producer should be deferred!
        internalGetDocument().internalCreateBuilder(source.getProducer(), modelExtension, this);
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

    public final void coreSetValue(String value) throws DeferredParsingException {
        // TODO: what if arg is null?
        if (content != null) {
            coreClear();
        }
        content = value;
        internalNotifyChildrenModified(1);
    }

    public final void coreClear() throws DeferredParsingException {
        if (content instanceof LLChildNode) {
            LLChildNode child = (LLChildNode)content;
            do {
                LLChildNode next = child.internalGetNextSibling();
                child.internalSetParent(null);
                child.internalSetNextSibling(null);
                child = next;
            } while (child != null);
        }
        content = null;
        internalNotifyChildrenCleared();
    }

    public final String coreGetTextContent() throws DeferredParsingException {
        CharSequence content = internalCollectTextContent(null);
        return content == null ? "" : content.toString();
    }
    
    @Override
    final CharSequence internalCollectTextContent(CharSequence appendTo) throws DeferredParsingException {
        CharSequence content = appendTo;
        for (CoreChildNode node = coreGetFirstChild(); node != null; node = node.coreGetNextSibling()) {
            content = ((Node)node).internalCollectTextContent(content);
        }
        return content;
    }

    public final LLChildNode internalGetFirstChildIfMaterialized() {
        return (LLChildNode)content;
    }

    public final void internalSetFirstChild(CoreChildNode child) {
        content = child;
    }

    public final boolean coreIsComplete() {
        return complete;
    }

    public final void internalSetComplete(boolean complete) {
        this.complete = complete;
    }
    
    public final void coreBuild() throws DeferredParsingException {
        if (!complete) {
            Builder builder = internalGetDocument().internalGetBuilderFor(this);
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
                Builder builder = internalGetDocument().internalGetBuilderFor(this);
                while (content == null && !complete) {
                    builder.next();
                }
                return (CoreChildNode)content;
            }
        } else if (content instanceof String) {
            // TODO: no cast here
            LLChildNode firstChild = new Text((Document)internalGetDocument(), (String)content);
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
            // TODO: this is suboptimal because it will repeatedly lookup the builder
            child = child.coreGetNextSibling();
        }
        return previousChild;
    }
    
    public void internalPrepareNewChild(CoreChildNode newChild) throws CoreModelException {
        internalValidateOwnerDocument(newChild);
        
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
        
        // TODO: should never or not always be necessary
        if (newChild instanceof CoreParentNode) {
            ((CoreParentNode)newChild).coreBuild();
        }
    }
    
    // insertBefore: newChild != null, refChild != null, removeRefChild == false
    // appendChild:  newChild != null, refChild == null, removeRefChild == false
    // replaceChild: newChild != null, refChild != null, removeRefChild == true
    // removeChild:  newChild == null, refChild != null, removeRefChild == true
    private void merge(CoreNode newChild, CoreChildNode refChild, boolean removeRefChild) throws CoreModelException {
        if (newChild instanceof CoreChildNode) {
            internalPrepareNewChild((CoreChildNode)newChild);
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
                ((LLChildNode)previousSibling).internalSetNextSibling((LLChildNode)nextSibling);
            }
            internalNotifyChildrenModified(-1);
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
                    internalValidateChildType(node, removeRefChild ? refChild : null);
                    ((LLChildNode)node).internalSetParent(this);
                    lastNodeToInsert = node;
                }
                delta = fragment.coreGetChildCount();
                // TODO: need to clear the document fragment?
            } else if (newChild instanceof CoreChildNode) {
                ((CoreChildNode)newChild).coreDetach();
                firstNodeToInsert = lastNodeToInsert = (CoreChildNode)newChild;
                internalValidateChildType(firstNodeToInsert, removeRefChild ? refChild : null);
                ((LLChildNode)firstNodeToInsert).internalSetParent(this);
                delta = 1;
            } else {
                throw new ChildTypeNotAllowedException();
            }
            if (removeRefChild) {
                delta--;
            }
            if (delta != 0) {
                internalNotifyChildrenModified(delta);
            }
            if (previousSibling == null) {
                internalSetFirstChild(firstNodeToInsert);
            } else {
                ((LLChildNode)previousSibling).internalSetNextSibling((LLChildNode)firstNodeToInsert);
            }
            if (nextSibling != null) {
                ((LLChildNode)lastNodeToInsert).internalSetNextSibling((LLChildNode)nextSibling);
            }
        }
        if (removeRefChild) {
            ((LLChildNode)refChild).internalSetParent(null);
        }
    }

    public final void coreAppendChild(CoreChildNode newChild) throws CoreModelException {
        merge(newChild, null, false);
    }

    public final void coreAppendChildren(CoreDocumentFragment newChildren) throws CoreModelException {
        internalValidateOwnerDocument(newChildren);
        merge(newChildren, null, false);
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
