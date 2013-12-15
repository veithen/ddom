/*
 * Copyright 2013 Andreas Veithen
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

import com.googlecode.ddom.backend.linkedlist.intf.LLElement;
import com.googlecode.ddom.core.CoreModelStreamException;
import com.googlecode.ddom.core.ElementNameMismatchException;
import com.googlecode.ddom.core.ElementNamespaceAwarenessMismatchException;
import com.googlecode.ddom.stream.StreamException;

final class UnwrappingBuilderHandlerDelegate extends BuilderHandlerDelegate {
    private final LLElement target;
    private int skipDepth;
    private boolean elementFound;

    UnwrappingBuilderHandlerDelegate(Builder builder, LLElement target) {
        super(builder);
        this.target = target;
    }

    boolean isElementFound() {
        return elementFound;
    }

    @Override
    void startEntity(boolean fragment, String inputEncoding) {
    }

    @Override
    void processXmlDeclaration(String version, String encoding, Boolean standalone) {
    }

    @Override
    void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        skipDepth++;
    }

    @Override
    BuilderHandlerDelegate endDocumentTypeDeclaration() throws StreamException {
        skipDepth--;
        return this;
    }

    @Override
    BuilderHandlerDelegate startElement(String tagName) throws StreamException {
        if (elementFound) {
            // TODO: better exception
            throw new IllegalStateException();
        } else {
            if (target instanceof NSUnawareElement) {
                elementFound = true;
                try {
                    ((NSUnawareElement)target).initName(tagName);
                } catch (ElementNameMismatchException ex) {
                    throw new CoreModelStreamException(ex);
                }
                return newContext(target);
            } else {
                throw new CoreModelStreamException(new ElementNamespaceAwarenessMismatchException("Expected a namespace unaware element"));
            }
        }
    }

    @Override
    BuilderHandlerDelegate startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        if (elementFound) {
            // TODO: better exception
            throw new IllegalStateException();
        } else {
            if (target instanceof NSAwareElement) {
                elementFound = true;
                try {
                    ((NSAwareElement)target).initName(namespaceURI, localName, prefix);
                } catch (ElementNameMismatchException ex) {
                    throw new CoreModelStreamException(ex);
                }
                return newContext(target);
            } else {
                throw new CoreModelStreamException(new ElementNamespaceAwarenessMismatchException("Expected a namespace aware element"));
            }
        }
    }

    @Override
    BuilderHandlerDelegate endElement() throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    BuilderHandlerDelegate startAttribute(String name, String type) throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    BuilderHandlerDelegate startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    BuilderHandlerDelegate startNamespaceDeclaration(String prefix) throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    BuilderHandlerDelegate endAttribute() throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    void resolveElementNamespace(String namespaceURI) throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    void attributesCompleted() throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    void processCharacterData(String data, boolean ignorable) throws StreamException {
        // TODO: parser doesn't report ignorable correctly
//        if (!ignorable && skipDepth == 0) {
//            // TODO: better exception
//            throw new IllegalStateException();
//        }
    }

    @Override
    BuilderHandlerDelegate startProcessingInstruction(String target) throws StreamException {
        skipDepth++;
        return this;
    }

    @Override
    BuilderHandlerDelegate endProcessingInstruction() throws StreamException {
        skipDepth--;
        return this;
    }

    @Override
    BuilderHandlerDelegate startComment() throws StreamException {
        skipDepth++;
        return this;
    }

    @Override
    BuilderHandlerDelegate endComment() throws StreamException {
        skipDepth--;
        return this;
    }

    @Override
    BuilderHandlerDelegate startCDATASection() throws StreamException {
        // TODO: better exception
        throw new IllegalStateException();
    }

    @Override
    BuilderHandlerDelegate endCDATASection() throws StreamException {
        throw new IllegalStateException();
    }

    @Override
    void processEntityReference(String name) throws StreamException {
        // TODO: better exception
        throw new IllegalStateException();
    }

    @Override
    BuilderHandlerDelegate completed() throws StreamException {
        return null;
    }
}
