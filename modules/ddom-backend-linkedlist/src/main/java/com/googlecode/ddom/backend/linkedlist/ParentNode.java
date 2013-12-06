/*
 * Copyright 2009-2013 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist;

import java.util.ArrayList;

import com.googlecode.ddom.backend.ExtensionFactoryLocator;
import com.googlecode.ddom.backend.Inject;
import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.backend.linkedlist.support.ChildrenByTypeIterator;
import com.googlecode.ddom.backend.linkedlist.support.ElementsIterator;
import com.googlecode.ddom.backend.linkedlist.support.TreeWalker;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ChildIterator;
import com.googlecode.ddom.core.ChildNotAllowedException;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreEntityReference;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreNSUnawareElement;
import com.googlecode.ddom.core.CoreNode;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.CoreProcessingInstruction;
import com.googlecode.ddom.core.CyclicRelationshipException;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ElementMatcher;
import com.googlecode.ddom.core.ExceptionTranslator;
import com.googlecode.ddom.core.HierarchyException;
import com.googlecode.ddom.core.NodeConsumedException;
import com.googlecode.ddom.core.NodeInUseException;
import com.googlecode.ddom.core.NodeMigrationException;
import com.googlecode.ddom.core.NodeMigrationPolicy;
import com.googlecode.ddom.core.NodeNotFoundException;
import com.googlecode.ddom.core.Selector;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.core.WrongDocumentException;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.stream.NullXmlHandler;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

public abstract class ParentNode extends Node implements LLParentNode {
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    @Inject
    private static ModelExtension modelExtension;
    
    /**
     * The content of this node. This is a {@link CoreChildNode} if the node is expanded, a
     * {@link XmlSource} if the content is sourced or a {@link String} if the value has been
     * set.
     */
    private Object content;
    
    public ParentNode(String value) {
        if (value == null) {
            internalSetState(Flags.STATE_EXPANDED);
        } else {
            internalSetState(Flags.STATE_VALUE_SET);
            content = value;
        }
    }

    public ParentNode(int state) {
        internalSetState(state);
    }

    public final Object coreGetContent() {
        return content;
    }

    public final boolean coreIsExpanded() {
        return content instanceof CoreChildNode;
    }

    public final void coreSetContent(XmlSource source) {
        // TODO: need to clear any existing content!
        internalSetState(Flags.STATE_CONTENT_SET);
        content = source;
        // TODO: need to decide how to handle symbol tables in a smart way here
//        symbols = producer.getSymbols();
        contentReset();
    }

    public final void coreSetSource(XmlSource source) {
        // TODO: need to clear any existing content!
        internalSetState(Flags.STATE_SOURCE_SET);
        content = source;
        contentReset();
    }

    // May be overridden by subclasses
    protected void contentReset() {
    }

    public final String coreGetValue() throws DeferredBuildingException {
        // TODO: this should also be applicable for other OptimizedParentNodes
        if (internalGetState() == Flags.STATE_VALUE_SET) {
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
        coreClear();
        if (value != null && value.length() > 0) {
            content = value;
            internalSetState(Flags.STATE_VALUE_SET);
            internalNotifyChildrenModified(1);
        }
    }
    
    public final void internalSetValue(String value) {
        content = value;
    }

    public final void coreClear() throws DeferredParsingException {
        switch (internalGetState()) {
            case Flags.STATE_ATTRIBUTES_PENDING:
                // TODO
                throw new UnsupportedOperationException();
            case Flags.STATE_CHILDREN_PENDING:
                internalGetOwnerDocument(false).internalGetInputContext(this).setPassThroughHandler(NullXmlHandler.INSTANCE);
                // Fall through
            case Flags.STATE_EXPANDED:
                LLChildNode child = (LLChildNode)content;
                while (child != null) {
                    LLChildNode next = child.internalGetNextSiblingIfMaterialized();
                    child.internalSetParent(null);
                    child.internalSetNextSibling(null);
                    child = next;
                };
                break;
            case Flags.STATE_CONSUMED:
                // TODO
                throw new UnsupportedOperationException();
        }
        content = null;
        internalNotifyChildrenCleared();
        internalSetState(Flags.STATE_EXPANDED);
    }

    public final String coreGetTextContent(TextCollectorPolicy policy) throws DeferredBuildingException {
        if (content instanceof String) {
            return (String)content;
        } else {
            LLChildNode node = internalGetFirstChild();
            if (node == null) {
                return "";
            } else {
                CharSequence text = null;
                int depth = 0;
                loop: while (true) {
                    boolean recurse = false;
                    if (node instanceof CoreCharacterData) {
                        String data = ((CoreCharacterData)node).coreGetData();
                        if (text == null) {
                            text = data;
                        } else if (text instanceof String) {
                            String existing = (String)text;
                            StringBuilder builder = new StringBuilder(existing.length() + data.length());
                            builder.append(existing);
                            builder.append(data);
                            text = builder;
                        } else {
                            ((StringBuilder)text).append(data);
                        }
                    } else {
                        switch (policy.getAction(node.coreGetNodeType(), text != null)) {
                            case RECURSE:
                                recurse = true;
                                break;
                            case SKIP:
                                break;
                            case STOP:
                                break loop;
                            case FAIL:
                                // TODO
                                throw new Error();
                        }
                    }
                    while (true) {
                        if (recurse) {
                            // TODO: this will expand compact parent nodes; optimize this!
                            LLChildNode firstChild = ((LLParentNode)node).internalGetFirstChild();
                            if (firstChild == null) {
                                recurse = false;
                            } else {
                                node = firstChild;
                                depth++;
                                break;
                            }
                        } else {
                            LLChildNode sibling = node.internalGetNextSibling();
                            if (sibling == null) {
                                if (depth == 0) {
                                    break loop;
                                } else {
                                    node = (LLChildNode)node.internalGetParent();
                                    depth--;
                                }
                            } else {
                                node = sibling;
                                break;
                            }
                        }
                    }
                }
                return text == null ? "" : text.toString();
            }
        }
    }
    
    public final LLChildNode internalGetFirstChildIfMaterialized() {
        return (LLChildNode)content;
    }

    public final void internalSetFirstChild(CoreChildNode child) {
        content = child;
    }

    public final boolean coreIsComplete() {
        int state = internalGetState();
        return state == Flags.STATE_EXPANDED || state == Flags.STATE_VALUE_SET;
    }
    
    public final void coreBuild() throws DeferredBuildingException {
        if (!coreIsComplete()) {
            InputContext context = internalGetOrCreateInputContext();
            do {
                context.next(false);
            } while (!coreIsComplete());
        }
    }
    
    public final boolean coreHasValue() throws DeferredBuildingException {
        if (content == null && !coreIsComplete()) {
            // TODO: should use internalGetOrCreateInputContext here
            InputContext context = internalGetOwnerDocument(false).internalGetInputContext(this);
            do {
                context.next(false);
            } while (content == null && !coreIsComplete());
        }
        return content instanceof String;
    }

    public final boolean coreIsEmpty() throws DeferredBuildingException {
        if (content == null && !coreIsComplete()) {
            InputContext context = internalGetOwnerDocument(false).internalGetInputContext(this);
            do {
                context.next(false);
            } while (content == null && !coreIsComplete());
        }
        return content == null;
    }

    public final CoreChildNode coreGetFirstChild() throws DeferredBuildingException {
        return internalGetFirstChild();
    }
    
    public final InputContext internalGetOrCreateInputContext() throws DeferredBuildingException {
        InputContext inputContext;
        int state = internalGetState();
        if (state == Flags.STATE_CONTENT_SET || state == Flags.STATE_SOURCE_SET) {
            XmlSource source = (XmlSource)content;
            content = null;
            LLDocument document = internalGetOwnerDocument(true);
            try {
                inputContext = document.internalCreateInputContext(source.getInput(XmlSource.Hints.DEFAULTS), this, state == Flags.STATE_SOURCE_SET);
            } catch (StreamException ex) {
                throw new DeferredParsingException(ex);
            }
        } else {
            LLDocument document = internalGetOwnerDocument(false);
            if (document == null) {
                // We should never get here
                throw new IllegalStateException();
            }
            inputContext = document.internalGetInputContext(this);
        }
        return inputContext;
    }
    
    public final LLChildNode internalGetFirstChild() throws DeferredBuildingException {
        InputContext context = null;
        while (true) {
            switch (internalGetState()) {
                case Flags.STATE_CONTENT_SET:
                case Flags.STATE_SOURCE_SET:
                    context = internalGetOrCreateInputContext();
                    break;
                case Flags.STATE_ATTRIBUTES_PENDING:
                case Flags.STATE_CHILDREN_PENDING:
                    if (content == null) {
                        if (context == null) {
                            context = internalGetOrCreateInputContext();
                        }
                        do {
                            // TODO: this also expands attributes, which is not what we want
                            context.next(true);
                        } while (content == null && !coreIsComplete());
                        // After calling the builder, the node may be in state "Value set".
                        // Just loop to dispatch to the correct case.
                        break;
                    } else {
                        return (LLChildNode)content;
                    }
                case Flags.STATE_EXPANDED:
                    return (LLChildNode)content;
                case Flags.STATE_CONSUMED:
                    if (content != null) {
                        return (LLChildNode)content;
                    } else {
                        throw new NodeConsumedException();
                    }
                case Flags.STATE_VALUE_SET:
                    // TODO: no cast here
                    LLChildNode firstChild = new CharacterData((Document)internalGetOwnerDocument(false), (String)content, false);
                    firstChild.internalSetParent(this);
                    content = firstChild;
                    internalSetState(Flags.STATE_EXPANDED);
                    return firstChild;
                default:
                    throw new IllegalStateException(); // TODO: consumed??
            }
        }
    }
    
    public final CoreChildNode coreGetLastChild() throws DeferredBuildingException {
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
    
    public final void internalImportBuilder(LLDocument foreignDocument, LLParentNode node) {
        ArrayList<Builder> foreignBuilders = foreignDocument.getBuilders();
        ArrayList<Builder> builders = null;
        for (int i=0, l=foreignBuilders.size(); i<l; i++) {
            Builder builder = foreignBuilders.get(i);
            if (builder.isBuilderForTree(node)) {
                if (builders == null) {
                    builders = internalGetOwnerDocument(true).getBuilders();
                }
                builders.add(builder);
            }
        }
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
                    } else if (isForeignDocument && newChild instanceof CoreParentNode) {
                        // If we are moving the node from another document, then we need to register
                        // the relevant builders in the target node's document. Otherwise deferred
                        // parsing will be broken
                        Document foreignDocument = (Document)newChild.coreGetOwnerDocument(false); // TODO: avoid cast here
                        if (foreignDocument != null) {
                            internalImportBuilder(foreignDocument, (LLParentNode)newChild);
                        }
                        break;
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
            // The new parent will be set by the caller; however, we still need to unset it here
            // to avoid confusing the builder if the node is not complete and deferred parsing
            // occurs before the new parent is set.
            result.internalUnsetParent(null);
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

    public final void coreAppendChild(CoreChildNode coreChild, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredBuildingException {
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
    
    private void appendNewlyCreatedChild(LLChildNode child) throws ChildNotAllowedException, DeferredBuildingException {
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

    public final CoreDocumentTypeDeclaration coreAppendDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws ChildNotAllowedException, DeferredBuildingException {
        DocumentTypeDeclaration child = new DocumentTypeDeclaration(null, rootName, publicId, systemId);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreNSUnawareElement coreAppendElement(String tagName) throws ChildNotAllowedException, DeferredBuildingException {
        NSUnawareElement child = new NSUnawareElement(null, tagName, true);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreNSAwareElement coreAppendElement(String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredBuildingException {
        NSAwareElement child = nsAwareElementFactory.create(modelExtension == null ? null : modelExtension.mapElement(namespaceURI, localName), null, namespaceURI, localName, prefix, true);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final <T extends CoreNSAwareElement> T coreAppendElement(Class<T> extensionInterface, String namespaceURI, String localName, String prefix) throws ChildNotAllowedException, DeferredBuildingException {
        NSAwareElement child = nsAwareElementFactory.create(extensionInterface, null, namespaceURI, localName, prefix, true);
        appendNewlyCreatedChild(child);
        return extensionInterface.cast(child);
    }
    
    public final CoreProcessingInstruction coreAppendProcessingInstruction(String target, String data) throws ChildNotAllowedException, DeferredBuildingException {
        ProcessingInstruction child = new ProcessingInstruction(null, target, data);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final CoreCharacterData coreAppendCharacterData(String data) throws ChildNotAllowedException, DeferredBuildingException {
        CharacterData child = new CharacterData(null, data, false);
        appendNewlyCreatedChild(child);
        return child;
    }

    public final CoreComment coreAppendComment(String data) throws ChildNotAllowedException, DeferredBuildingException {
        Comment child = new Comment(null, data);
        appendNewlyCreatedChild(child);
        return child;
    }

    public final CoreCDATASection coreAppendCDATASection(String data) throws ChildNotAllowedException, DeferredBuildingException {
        CDATASection child = new CDATASection(null, data);
        appendNewlyCreatedChild(child);
        return child;
    }

    public final CoreEntityReference coreAppendEntityReference(String name) throws ChildNotAllowedException, DeferredBuildingException {
        EntityReference child = new EntityReference(null, name);
        appendNewlyCreatedChild(child);
        return child;
    }
    
    public final <T> ChildIterator<T> coreGetNodes(Axis axis, Selector selector, Class<T> type, ExceptionTranslator exceptionTranslator) {
        return new ChildrenByTypeIterator<T>(this, axis, selector, type, exceptionTranslator);
    }

    public <T extends CoreElement> ChildIterator<T> coreGetElements(Axis axis, Class<T> type, ElementMatcher<? super T> matcher, String namespaceURI, String name, ExceptionTranslator exceptionTranslator) {
        return new ElementsIterator<T>(this, axis, type, matcher, namespaceURI, name, exceptionTranslator);
    }

    public <T extends CoreChildNode> T coreGetFirstChildByType(Class<T> type) throws DeferredBuildingException {
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
        return new TreeWalker(this, preserve);
    }

    public final CoreNode coreClone(ClonePolicy policy) throws DeferredParsingException {
        LLParentNode clone = shallowClone(policy);
        if (policy.cloneChildren(coreGetNodeType())) {
            if (content instanceof String) {
                clone.coreSetValue((String)content);
            } else {
                // TODO
            }
        }
        return clone;
    }
    
    abstract LLParentNode shallowClone(ClonePolicy policy);
}
