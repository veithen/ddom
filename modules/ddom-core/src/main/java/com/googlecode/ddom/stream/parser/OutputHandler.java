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
package com.googlecode.ddom.stream.parser;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.symbols.Symbols;

abstract class OutputHandler {
    final Symbols symbols;
    final XmlHandler handler;
    
    OutputHandler(Symbols symbols, XmlHandler handler) {
        this.symbols = symbols;
        this.handler = handler;
    }
    
    final void startEntity(boolean fragment, String inputEncoding) throws StreamException {
        handler.startEntity(fragment, inputEncoding);
    }
    
    final void processXmlDeclaration(String version, String encoding, Boolean standalone) throws StreamException {
        handler.processXmlDeclaration(version, encoding, standalone);
    }
    
    final void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        handler.startDocumentTypeDeclaration(rootName, publicId, systemId);
    }
    
    final void endDocumentTypeDeclaration() throws StreamException {
        handler.endDocumentTypeDeclaration();
    }
    
    abstract void startElement(char[] name, int len) throws StreamException;
    
    /**
     * Notify the handler that the end of an element has been reached. The handler is expected to
     * check that the tag name is correct.
     * 
     * @param name
     *            the buffer containing the tag name of the element (at offset 0), or
     *            <code>null</code> if the element was empty and no name check needs to be performed
     * @param len
     *            the length of the tag name; only meaningful if <code>name</code> is not null
     * @throws StreamException
     */
    abstract void endElement(char[] name, int len) throws StreamException;

    abstract void startAttribute(char[] name, int len) throws StreamException;
    
    abstract void endAttribute() throws StreamException;
    
    abstract void attributesCompleted() throws StreamException;
    
    abstract void processCharacterData(String data) throws StreamException;
    
    final void startProcessingInstruction(String target) throws StreamException {
        handler.startProcessingInstruction(target);
    }
    
    final void endProcessingInstruction() throws StreamException {
        handler.endProcessingInstruction();
    }
    
    final void startComment() throws StreamException {
        handler.startComment();
    }
    
    final void endComment() throws StreamException {
        handler.endComment();
    }
    
    final void startCDATASection() throws StreamException {
        handler.startCDATASection();
    }
    
    final void endCDATASection() throws StreamException {
        handler.endCDATASection();
    }
    
    final void processEntityReference(String name) throws StreamException {
        handler.processEntityReference(name);
    }
    
    final void completed() throws StreamException {
        handler.completed();
    }
}
