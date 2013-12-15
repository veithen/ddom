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
package com.googlecode.ddom.stream.pivot;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlOutput;

/**
 * Base class for {@link XmlOutput} implementations that appear as pull parser implementations to
 * the client code.
 * 
 * @author Andreas Veithen
 */
public abstract class XmlPivot extends XmlOutput {
    private XmlPivotHandler handler;
    
    @Override
    protected final XmlHandler createXmlHandler() {
        // TODO: this makes the assumption that the stream is connected when createXmlHandler is called; needs to be documented
        handler = new XmlPivotHandler(this, getStream());
        return handler;
    }
    
    protected final void nextEvent() throws StreamException {
        handler.next();
    }

    protected abstract boolean startEntity(boolean fragment, String inputEncoding);
    
    protected abstract boolean processXmlDeclaration(String version, String encoding, Boolean standalone);
    
    protected abstract boolean startDocumentTypeDeclaration(String rootName, String publicId, String systemId);

    protected abstract boolean endDocumentTypeDeclaration();
    
    protected abstract boolean startElement(String tagName);
    
    protected abstract boolean startElement(String namespaceURI, String localName, String prefix);
    
    protected abstract boolean endElement();
    
    protected abstract boolean startAttribute(String name, String type);
    
    protected abstract boolean startAttribute(String namespaceURI, String localName, String prefix, String type);
    
    protected abstract boolean startNamespaceDeclaration(String prefix);
    
    protected abstract boolean endAttribute();
    
    protected abstract boolean resolveElementNamespace(String namespaceURI);
    
    protected abstract boolean resolveAttributeNamespace(int index, String namespaceURI);
    
    protected abstract boolean attributesCompleted();
    
    protected abstract boolean processCharacterData(String data, boolean ignorable);
    
    protected abstract boolean startProcessingInstruction(String target);
    
    protected abstract boolean endProcessingInstruction();
    
    protected abstract boolean startComment();
    
    protected abstract boolean endComment();
    
    protected abstract boolean startCDATASection();
    
    protected abstract boolean endCDATASection();
    
    protected abstract boolean processEntityReference(String name);
    
    protected abstract void completed();
}
