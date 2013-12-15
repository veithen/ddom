/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import javax.xml.namespace.QName;

import com.googlecode.ddom.backend.Implementation;
import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLElement;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreModelStreamException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.ElementAlreadyExistsException;
import com.googlecode.ddom.core.ElementNameMismatchException;
import com.googlecode.ddom.core.NodeMigrationPolicy;
import com.googlecode.ddom.core.Sequence;
import com.googlecode.ddom.core.SequenceItem;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

// TODO: some methods access namespaceURI/localName/prefix directly; they should use the corresponding coreGetXxx methods
@Implementation(factory=NSAwareElementFactory.class)
public class NSAwareElement extends Element implements CoreNSAwareElement {
    private static final int SEQOP_GET = 1;
    private static final int SEQOP_GET_OR_CREATE = 2;
    private static final int SEQOP_CREATE = 3;
    private static final int SEQOP_INSERT = 4;
    
    private String namespaceURI;
    private String localName;
    private String prefix;

    public NSAwareElement(Document document, String namespaceURI, String localName, String prefix, boolean complete) {
        super(document, complete);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
    }

    void initName(String namespaceURI, String localName, String prefix) throws ElementNameMismatchException {
        if (namespaceURI != null) {
            resolveNamespace(namespaceURI);
        }

        if (internalGetFlag(Flags.LOCAL_NAME_SET)) {
            // Do nothing; keep the overridden local name
        } else if (this.localName != null) {
            if (!this.localName.equals(localName)) {
                throw new ElementNameMismatchException("Unexpected local name");
            }
        } else {
            this.localName = localName;
        }
        
        if (internalGetFlag(Flags.PREFIX_SET)) {
            // Do nothing; keep the overridden prefix
        } else if (this.prefix != null) {
            if (!this.prefix.equals(prefix)) {
                throw new ElementNameMismatchException("Unexpected prefix");
            }
        } else {
            this.prefix = prefix;
        }
    }
    
    void resolveNamespace(String namespaceURI) throws ElementNameMismatchException {
        if (internalGetFlag(Flags.NAMESPACE_URI_SET)) {
            // Do nothing; keep the overridden namespace URI
        } else if (this.namespaceURI != null) {
            if (namespaceURI != null && !this.namespaceURI.equals(namespaceURI)) {
                throw new ElementNameMismatchException("Unexpected namespace URI");
            }
        } else {
            this.namespaceURI = namespaceURI;
        }
    }
        
    public final int coreGetNodeType() {
        return NS_AWARE_ELEMENT_NODE;
    }

    public final String coreGetNamespaceURI() throws DeferredBuildingException {
        if (namespaceURI == null) {
            switch (internalGetState()) {
                case STATE_SOURCE_SET:
                    // Requesting the input context in this state will result
                    // in a call to initName so that the namespace URI gets
                    // initialized, except if the namespace is resolved lazily.
                    internalGetOrCreateInputContext();
                    if (namespaceURI != null) {
                        break;
                    }
                    // Otherwise, fall through
                case STATE_ATTRIBUTES_PENDING:
                    // In this state namespaceURI is null because the namespace
                    // has not yet been resolved. Proceed with the attributes
                    // until the parser reports the namespace.
                    InputContext inputContext = internalGetOrCreateInputContext();
                    do {
                        inputContext.next(false);
                    } while (namespaceURI == null && internalGetState() == STATE_ATTRIBUTES_PENDING);
            }
        }
        return namespaceURI;
    }

    public final void coreSetNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
        internalSetFlag(Flags.NAMESPACE_URI_SET, true);
    }
    
    public final String coreGetPrefix() throws DeferredBuildingException {
        if (prefix == null && internalGetState() == STATE_SOURCE_SET) {
            internalGetOrCreateInputContext();
        }
        return prefix;
    }

    public final void coreSetPrefix(String prefix) {
        this.prefix = prefix;
        internalSetFlag(Flags.PREFIX_SET, true);
    }
    
    public final String coreGetLocalName() throws DeferredBuildingException {
        if (localName == null && internalGetState() == STATE_SOURCE_SET) {
            internalGetOrCreateInputContext();
        }
        return localName;
    }
    
    public final void coreSetLocalName(String localName) {
        this.localName = localName;
        internalSetFlag(Flags.LOCAL_NAME_SET, true);
    }

    public final QName coreGetQName() throws DeferredBuildingException {
        return NSAwareNamedNodeHelper.coreGetQName(this);
    }

    @Override
    protected final String getImplicitNamespaceURI(String prefix) {
        if (prefix == null) {
            return this.prefix == null ? namespaceURI : null;
        } else {
            return prefix.equals(this.prefix) ? namespaceURI : null;
        }
    }

    @Override
    protected final String getImplicitPrefix(String namespaceURI) {
        return namespaceURI.equals(this.namespaceURI) ? prefix : null;
    }

    public final CoreNSAwareElement coreGetElementFromSequence(Sequence sequence, int index, boolean create) throws CoreModelException {
        return querySequence(sequence, index, null, create ? SEQOP_GET_OR_CREATE : SEQOP_GET, null);
    }

    public final CoreNSAwareElement coreCreateElementInSequence(Sequence sequence, int index) throws CoreModelException {
        return querySequence(sequence, index, null, SEQOP_CREATE, null);
    }

    public final void coreInsertElementInSequence(Sequence sequence, int index, CoreNSAwareElement element, NodeMigrationPolicy policy) throws CoreModelException {
        querySequence(sequence, index, element, SEQOP_INSERT, policy);
    }

    private final CoreNSAwareElement querySequence(Sequence sequence, int index, CoreNSAwareElement newElement, int operation, NodeMigrationPolicy policy) throws CoreModelException {
        int ptr = 0;
        CoreChildNode child = coreGetFirstChild();
        CoreNSAwareElement previousElement = null;
        CoreNSAwareElement nextElement = null;
        while (child != null) {
            if (child instanceof CoreNSAwareElement) {
                CoreNSAwareElement element = (CoreNSAwareElement)child;
                while (!matches(element, sequence, ptr)) {
                    ptr++;
                    if (ptr == sequence.length()) {
                        throw new CoreModelException("Unexpected element {" + element.coreGetNamespaceURI() + "}" + element.coreGetLocalName()); // TODO
                    }
                }
                if (ptr == index) {
                    switch (operation) {
                        case SEQOP_GET:
                        case SEQOP_GET_OR_CREATE:
                            return element;
                        case SEQOP_CREATE:
                            throw new ElementAlreadyExistsException(sequence, index);
                        case SEQOP_INSERT:
                            element.coreReplaceWith(newElement);
                            return null;
                    }
                } else if (ptr > index) {
                    nextElement = element;
                    break;
                } else {
                    previousElement = element;
                }
            }
            child = child.coreGetNextSibling();
        }
        if (operation == SEQOP_GET) {
            return null;
        } else {
            CoreDocument document = coreGetOwnerDocument(true);
            SequenceItem item = sequence.item(index);
            CoreNSAwareElement element;
            if (operation == SEQOP_INSERT) {
                element = newElement;
            } else {
                String namespaceURI = item.getNamespaceURI();
                String prefix = namespaceURI.length() == 0 ? "" : coreLookupPrefix(namespaceURI, false);
                if (item.isUseExtensionInterface()) {
                    element = coreGetNodeFactory().createElement(document, item.getExtensionInterface(), item.getNamespaceURI(), item.getLocalName(), prefix);
                } else {
                    element = coreGetNodeFactory().createElement(document, item.getNamespaceURI(), item.getLocalName(), prefix);
                }
            }
            if (previousElement != null) {
                previousElement.coreInsertSiblingAfter(element, policy);
            } else if (nextElement != null) {
                nextElement.coreInsertSiblingBefore(element, policy);
            } else {
                coreAppendChild(element, policy);
            }
            return element;
        }
    }
    
    private boolean matches(CoreNSAwareElement element, Sequence sequence, int index) throws DeferredBuildingException {
        SequenceItem item = sequence.item(index);
        if (sequence.isMatchByInterface()) {
            return item.getExtensionInterface().isInstance(element);
        } else {
            return item.getLocalName().equals(element.coreGetLocalName())
                    && item.getNamespaceURI().equals(element.coreGetNamespaceURI());
        }
    }

    public final void internalGenerateStartEvent(XmlHandler handler) throws StreamException {
        try {
            handler.startElement(coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix());
        } catch (DeferredBuildingException ex) {
            throw new CoreModelStreamException(ex);
        }
    }

    @Override
    final LLElement shallowCloneWithoutAttributes(ClonePolicy policy) {
        return new NSAwareElement(null, namespaceURI, localName, prefix, true);
    }
}
