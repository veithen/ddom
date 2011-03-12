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
package com.googlecode.ddom.backend.linkedlist;

import javax.xml.namespace.QName;

import org.apache.commons.lang.ObjectUtils;

import com.googlecode.ddom.backend.Implementation;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.ElementAlreadyExistsException;
import com.googlecode.ddom.core.Sequence;
import com.googlecode.ddom.core.SequenceItem;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

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

    public final int coreGetNodeType() {
        return NS_AWARE_ELEMENT_NODE;
    }

    public final String coreGetNamespaceURI() {
        return namespaceURI;
    }

    public final void coreSetNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }
    
    public final String coreGetPrefix() {
        return prefix;
    }

    public final void coreSetPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public final String coreGetLocalName() {
        return localName;
    }
    
    public final void coreSetLocalName(String localName) {
        this.localName = localName;
    }

    public final QName coreGetQName() {
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
        return querySequence(sequence, index, null, create ? SEQOP_GET_OR_CREATE : SEQOP_GET);
    }

    public final CoreNSAwareElement coreCreateElementInSequence(Sequence sequence, int index) throws CoreModelException {
        return querySequence(sequence, index, null, SEQOP_CREATE);
    }

    public final void coreInsertElementInSequence(Sequence sequence, int index, CoreNSAwareElement element) throws CoreModelException {
        querySequence(sequence, index, element, SEQOP_INSERT);
    }

    private final CoreNSAwareElement querySequence(Sequence sequence, int index, CoreNSAwareElement newElement, int operation) throws CoreModelException {
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
                previousElement.coreInsertSiblingAfter(element);
            } else if (nextElement != null) {
                nextElement.coreInsertSiblingBefore(element);
            } else {
                coreAppendChild(element, null); // TODO: don't use null here
            }
            return element;
        }
    }
    
    private boolean matches(CoreNSAwareElement element, Sequence sequence, int index) {
        SequenceItem item = sequence.item(index);
        if (sequence.isMatchByInterface()) {
            return item.getExtensionInterface().isInstance(element);
        } else {
            return ObjectUtils.equals(item.getLocalName(), element.coreGetLocalName())
                    && ObjectUtils.equals(item.getNamespaceURI(), element.coreGetNamespaceURI());
        }
    }

    public final void internalGenerateStartEvent(XmlHandler handler) throws StreamException {
        handler.startElement(namespaceURI, localName, prefix);
    }
}