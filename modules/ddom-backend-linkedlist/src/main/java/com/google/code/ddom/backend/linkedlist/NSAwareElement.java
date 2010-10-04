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

import javax.xml.namespace.QName;

import org.apache.commons.lang.ObjectUtils;

import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.ElementAlreadyExistsException;
import com.google.code.ddom.core.Sequence;
import com.google.code.ddom.core.SequenceItem;
import com.google.code.ddom.core.SequenceOperation;

@Implementation(factory=NSAwareElementFactory.class)
public class NSAwareElement extends Element implements CoreNSAwareElement {
    private String namespaceURI;
    private String localName;
    private String prefix;

    public NSAwareElement(Document document, String namespaceURI, String localName, String prefix, boolean complete) {
        super(document, complete);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
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

    public final CoreNSAwareElement coreQuerySequence(Sequence sequence, int index, SequenceOperation operation) throws CoreModelException {
        int ptr = 0;
        CoreChildNode child = coreGetFirstChild();
        CoreNSAwareElement previousElement = null;
        CoreNSAwareElement nextElement = null;
        while (child != null) {
            if (child instanceof CoreNSAwareElement) {
                CoreNSAwareElement element = (CoreNSAwareElement)child;
                while (!matches(element, sequence.item(ptr))) {
                    ptr++;
                    if (ptr == sequence.length()) {
                        throw new CoreModelException(); // TODO
                    }
                }
                if (ptr == index) {
                    if (operation == SequenceOperation.CREATE) {
                        throw new ElementAlreadyExistsException(sequence, index);
                    } else {
                        return element;
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
        if (operation == SequenceOperation.GET) {
            return null;
        } else {
            CoreDocument document = coreGetDocument();
            SequenceItem item = sequence.item(index);
            CoreNSAwareElement element;
            // TODO: handle prefix
            if (item.isUseExtensionInterface()) {
                element = document.coreCreateElement(item.getExtensionInterface(), item.getNamespaceURI(), item.getLocalName(), null);
            } else {
                element = document.coreCreateElement(item.getNamespaceURI(), item.getLocalName(), null);
            }
            if (previousElement != null) {
                previousElement.coreInsertSiblingAfter(element);
            } else if (nextElement != null) {
                nextElement.coreInsertSiblingBefore(element);
            } else {
                coreAppendChild(element);
            }
            return element;
        }
    }
    
    private boolean matches(CoreNSAwareElement element, SequenceItem item) {
        Class<?> extensionInterface = item.getExtensionInterface();
        if (extensionInterface != null) {
            return extensionInterface.isInstance(element);
        } else {
            return ObjectUtils.equals(item.getLocalName(), element.coreGetLocalName())
                    && ObjectUtils.equals(item.getNamespaceURI(), element.coreGetNamespaceURI());
        }
    }
}
