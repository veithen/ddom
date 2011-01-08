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
package com.google.code.ddom.stream.spi.buffer;

import com.google.code.ddom.stream.spi.XmlHandler;
import com.google.code.ddom.stream.spi.XmlOutput;

/**
 * Base class for {@link XmlOutput} implementations that appear as pull parser implementations to
 * the client code.
 * 
 * @author Andreas Veithen
 */
public abstract class XmlPivot extends XmlOutput {
    @Override
    protected final XmlHandler createXmlHandler() {
        // TODO: this makes the assumption that the stream is connected when createXmlHandler is called; needs to be documented
        return new XmlPivotHandler(this, getStream());
    }

    protected abstract void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone);
    
    protected abstract boolean processDocumentType(String rootName, String publicId, String systemId);

    protected abstract boolean startElement(String tagName);
    
    protected abstract boolean startElement(String namespaceURI, String localName, String prefix);
    
    protected abstract boolean endElement();
    
    protected abstract boolean startAttribute(String name, String type);
    
    protected abstract boolean startAttribute(String namespaceURI, String localName, String prefix, String type);
    
    protected abstract boolean startNamespaceDeclaration(String prefix);
    
    protected abstract boolean endAttribute();
    
    protected abstract boolean attributesCompleted();
    
    protected abstract boolean processProcessingInstruction(String target, String data);
    
    protected abstract boolean processText(String data);
    
    protected abstract boolean processComment(String data);
    
    protected abstract boolean startCDATASection();
    
    protected abstract boolean endCDATASection();
    
    protected abstract boolean processEntityReference(String name);
    
    protected abstract void completed();
}
