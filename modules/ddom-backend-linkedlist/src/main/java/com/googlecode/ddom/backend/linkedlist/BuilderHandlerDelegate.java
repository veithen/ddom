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

import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.stream.StreamException;

abstract class BuilderHandlerDelegate {
    final Builder builder;
    private Context nestedContext;
    
    BuilderHandlerDelegate(Builder builder) {
        this.builder = builder;
    }

    final Context newContext(LLParentNode target) {
        if (nestedContext == null) {
            nestedContext = new Context(builder, this, target);
        } else {
            nestedContext.init(target);
        }
        return nestedContext;
    }
    
    final Context getNestedContext() {
        return nestedContext != null && nestedContext.getTargetNode() != null ? nestedContext : null;
    }
    
    abstract void startEntity(boolean fragment, String inputEncoding);
    abstract void processXmlDeclaration(String version, String encoding, Boolean standalone);
    abstract void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException;
    abstract BuilderHandlerDelegate endDocumentTypeDeclaration() throws StreamException;
    abstract BuilderHandlerDelegate startElement(String tagName) throws StreamException;
    abstract BuilderHandlerDelegate startElement(String namespaceURI, String localName, String prefix) throws StreamException;
    abstract BuilderHandlerDelegate endElement() throws StreamException;
    abstract BuilderHandlerDelegate startAttribute(String name, String type) throws StreamException;
    abstract BuilderHandlerDelegate startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException;
    abstract BuilderHandlerDelegate startNamespaceDeclaration(String prefix) throws StreamException;
    abstract BuilderHandlerDelegate endAttribute() throws StreamException;
    abstract void resolveElementNamespace(String namespaceURI) throws StreamException;
    abstract void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException;
    abstract void attributesCompleted() throws StreamException;
    abstract void processCharacterData(String data, boolean ignorable) throws StreamException;
    abstract BuilderHandlerDelegate startProcessingInstruction(String target) throws StreamException;
    abstract BuilderHandlerDelegate endProcessingInstruction() throws StreamException;
    abstract BuilderHandlerDelegate startComment() throws StreamException;
    abstract BuilderHandlerDelegate endComment() throws StreamException;
    abstract BuilderHandlerDelegate startCDATASection() throws StreamException;
    abstract BuilderHandlerDelegate endCDATASection() throws StreamException;
    abstract void processEntityReference(String name) throws StreamException;
    abstract BuilderHandlerDelegate completed() throws StreamException;
}
