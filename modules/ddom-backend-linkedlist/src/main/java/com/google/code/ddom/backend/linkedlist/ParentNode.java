/*
 * Copyright 2009-2011 Andreas Veithen
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

import com.google.code.ddom.backend.ExtensionFactoryLocator;
import com.google.code.ddom.backend.Inject;
import com.google.code.ddom.backend.linkedlist.intf.LLBuilder;
import com.google.code.ddom.backend.linkedlist.intf.LLChildNode;
import com.google.code.ddom.backend.linkedlist.intf.LLParentNode;
import com.google.code.ddom.backend.linkedlist.support.ChildrenByTypeIterator;
import com.google.code.ddom.backend.linkedlist.support.ElementsByLocalNameIterator;
import com.google.code.ddom.backend.linkedlist.support.ElementsByNameIterator;
import com.google.code.ddom.backend.linkedlist.support.ElementsByNamespaceIterator;
import com.google.code.ddom.backend.linkedlist.support.TreeSerializer;
import com.google.code.ddom.core.Axis;
import com.google.code.ddom.core.ChildIterator;
import com.google.code.ddom.core.ChildNotAllowedException;
import com.google.code.ddom.core.CoreCDATASection;
import com.google.code.ddom.core.CoreCharacterData;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreComment;
import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreDocumentTypeDeclaration;
import com.google.code.ddom.core.CoreEntityReference;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.CoreNSUnawareElement;
import com.google.code.ddom.core.CoreNode;
import com.google.code.ddom.core.CoreParentNode;
import com.google.code.ddom.core.CoreProcessingInstruction;
import com.google.code.ddom.core.CoreText;
import com.google.code.ddom.core.CyclicRelationshipException;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.HierarchyException;
import com.google.code.ddom.core.NodeInUseException;
import com.google.code.ddom.core.NodeMigrationException;
import com.google.code.ddom.core.NodeMigrationPolicy;
import com.google.code.ddom.core.NodeNotFoundException;
import com.google.code.ddom.core.WrongDocumentException;
import com.google.code.ddom.core.ext.ModelExtension;
import com.google.code.ddom.stream.spi.FragmentSource;
import com.google.code.ddom.stream.spi.XmlInput;

public abstract class ParentNode extends Node implements LLParentNode {
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    @Inject
    private static ModelExtension modelExtension;
    
    /**
     * The content of this node. This is a {@link CoreChildNode} if the node is expanded, a
     * {@link FragmentSource} if the content is sourced or a {@link String} if the value has been
     * set.
     */
    private Object content;
    
    public ParentNode(Object content) {
        this.content = content;
        internalSetComplete(true);
    }

    public ParentNode(boolean complete) {
        internalSetComplete(complete);
    }

    public final Object coreGetContent() {
        return content;
    }

    public final boolean coreIsExpanded() {
        return content instanceof CoreChildNode;
    }

    public final void coreSetContent(FragmentSource source) {
        // TODO: need to clear any existing content!
        internalSetComplete(false);
        // TODO: getting the producer should be deferred!
        internalGetOwnerDocument().internalCreateBuilder(source.getProducer(), this);
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
    
    public final void internalSetValue(String value) {
        content = value;
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
        if (content instanceof String) {
            return (String)content;
        } else {
            CharSequence content = internalCollectTextContent(null);
            return content == null ? "" : content.toString();
        }
    }
    
    @Override
    final CharSequence internalCollectTextContent(CharSequence appendTo) throws DeferredParsingException {
        CharSequence content = appendTo;
        for (LLChildNode node = internalGetFirstChild(); node != null; node = node.internalGetNextSibling()) {
            // TODO: define this method in LLNode
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
        return internalGetFlag(Flags.COMPLETE);
    }

    public final void internalSetComplete(boolean complete) {
        internalSetFlag(Flags.COMPLETE, complete);
    }
    
    public final void coreBuild() throws DeferredParsingException {
        if (!coreIsComplete()) {
            LLBuilder builder = internalGetOwnerDocument().internalGetBuilderFor(this);
            do {
                builder.next();
            } while (!coreIsComplete());
        }
    }
    
    public final CoreChildNode coreGetFirstChild() throws DeferredParsingException {
        return internalGetFirstChild();
    }
    
    public final LLChildNode internalGetFirstChild() throws DeferredParsingException {
        if (content == null) {
            if (coreIsComplete()) {
                return null;
            } else {
                LLBuilder builder = internalGetOwnerDocument().internalGetBuilderFor(this);
                while (content == null && !coreIsComplete()) {
                    builder.next();
                }
                // After calling the builder, the content may be a String object,
                // so just continue.
            }
        }
        if (content instanceof String) {
            // TODO: no cast here
            LLChildNode firstChild = new Text((Document)internalGetOwnerDocument(), (String)content, false);
            firstChild.internalSetParent(this);
            content = firstChild;
            return firstChild;
        } else {
            return (LLChildNode)content;
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
    
    @Deprecated // TODO: should be replaced by the variant taking a NodeMigrationPolicy argument
    public void internalPrepareNewChild(CoreChildNode newChild) throws WrongDocumentException, CyclicRelationshipException {
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
    }
    
    public LLChildNode internalPrepareNewChild(CoreChildNode newChild, NodeMigrationPolicy policy) throws NodeMigrationException, CyclicRelationshipException {
        boolean hasParent = newChild.coreHasParent();
        boolean isForeignDocument = !coreIsSameOwnerDocument(newChild);
        boolean isForeignModel = newChild.coreGetNodeFactory() != coreGetNodeFactory();
        if (hasParent || isForeignDocument || isForeignModel) {
            switch (policy.getAction(hasParent, isForeignDocument, isForeignModel)) {
                case REJECT:
                    if (isForeignDocument) {
                        // Note that since isForeignModel implies isForeignDocument, we also get here
                        // if isForeignModel is true.
                        throw new WrongDocumentException();
                    } else {
                        // We get here if isForeignDocument and isForeignModel are false. Since at least
                        // one of the three booleans must be true, this implies that hasParent is true.
                        throw new NodeInUseException();
                    }
                case MOVE:
                    if (isForeignModel) {
                        throw new UnsupportedOperationException();
                    } else {
                        break;
                    }
                case CLONE:
                    // TODO
                    throw new UnsupportedOperationException();
                default:
                    // Should never get here unless new values are added to the enum
                    throw new IllegalStateException();
            }
        }
        if (!isForeignModel) {
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
        }
        LLChildNode result = (LLChildNode)newChild;
        if (hasParent) {
            result.internalDetach();
        }
        return result;
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
                throw new ChildNotAllowedException();
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

    public final void coreAppendChild(CoreChildNode coreChild, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        internalValidateChildType(coreChild, null);
        LLChildNode child = internalPrepareNewChild(coreChild, policy);
        LLChildNode lastChild = (LLChildNode)coreGetLastChild(); // TODO: avoid cast here
        if (lastChild == null) {
            internalSetFirstChild(child);
        } else {
            lastChild.internalSetNextSibling(child);
        }
        child.internalSetParent(this);
        internalNotifyChildrenModified(1);
    }

    public final void coreAppendChildren(CoreDocumentFragment newChildren) throws CoreModelException {
        internalValidateOwnerDocument(newChildren);
        merge(newChildren, null, false);
    }
    
    private void appendNewlyCreatedChild(LLChildNode child) throws ChildNotAllowedException, DeferredParsingException {
        internalValidateChildType(child, null);
        LLChildNode lastChild = (LLChildNode)coreGetLastChild(); // TODO: avoid cast here
        if (lastChild == null) {
            internalSetFirstChild(child);
        } else {
            lastChild.internalSetNextSibling(child);
        }
        child.internalSetParent(this);
        internalNotifyChildrenModified(1);
    }

    public final CoreDocumentTypeDeclaration coreAppendDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws ChildNotAllowedException, DeferredParsingException {
        DocumentTypeDeclaration child = new DocumentTypeDeclaration(null, rootName, publicId, systemId, null);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreNSUnawareElement coreAppendElement(String tagName) throws ChildNotAllowedException, DeferredParsingException {
        NSUnawareElement child = new NSUnawareElement(null, tagName, true);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreNSAwareElement coreAppendElement(String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredParsingException {
        NSAwareElement child = nsAwareElementFactory.create(modelExtension == null ? null : modelExtension.mapElement(namespaceURI, localName), null, namespaceURI, localName, prefix, true);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreNSAwareElement coreAppendElement(Class<?> extensionInterface, String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredParsingException {
        NSAwareElement child = nsAwareElementFactory.create(extensionInterface, null, namespaceURI, localName, prefix, true);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreProcessingInstruction coreAppendProcessingInstruction(String target, String data) throws ChildNotAllowedException, DeferredParsingException {
        ProcessingInstruction child = new ProcessingInstruction(null, target, data);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreText coreAppendText(String data) throws ChildNotAllowedException, DeferredParsingException {
        Text child = new Text(null, data, false);
        appendNewlyCreatedChild(child);
        return child;
    }

    public final CoreComment coreAppendComment(String data) throws ChildNotAllowedException, DeferredParsingException {
        Comment child = new Comment(null, data);
        appendNewlyCreatedChild(child);
        return child;
    }

    public final CoreCDATASection coreAppendCDATASection() throws ChildNotAllowedException, DeferredParsingException {
        CDATASection child = new CDATASection(null, true);
        appendNewlyCreatedChild(child);
        return child;
    }

    public final CoreEntityReference coreAppendEntityReference(String name) throws ChildNotAllowedException, DeferredParsingException {
        EntityReference child = new EntityReference(null, name);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final <T extends CoreChildNode> ChildIterator<T> coreGetChildrenByType(Axis axis, Class<T> type) {
        return new ChildrenByTypeIterator<T>(this, axis, type);
    }

    public final ChildIterator<CoreNSAwareElement> coreGetElementsByName(Axis axis, String namespaceURI, String localName) {
        return new ElementsByNameIterator(this, axis, namespaceURI, localName);
    }

    public final ChildIterator<CoreNSAwareElement> coreGetElementsByNamespace(Axis axis, String namespaceURI) {
        return new ElementsByNamespaceIterator(this, axis, namespaceURI);
    }

    public final ChildIterator<CoreNSAwareElement> coreGetElementsByLocalName(Axis axis, String localName) {
        return new ElementsByLocalNameIterator(this, axis, localName);
    }

    public <T extends CoreChildNode> T coreGetFirstChildByType(Class<T> type) throws DeferredParsingException {
        CoreChildNode child = coreGetFirstChild();
        while (true) {
            if (child == null) {
                return null;
            } else if (type.isInstance(child)) {
                return type.cast(child);
            }
            child = child.coreGetNextSibling();
        }
    }

    public final XmlInput coreGetInput(boolean preserve) {
        return new TreeSerializer(this, preserve);
    }
}
