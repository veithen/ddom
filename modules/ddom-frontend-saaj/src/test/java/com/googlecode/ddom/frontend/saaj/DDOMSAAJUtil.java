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
package com.googlecode.ddom.frontend.saaj;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;

import org.w3c.dom.Document;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.saaj.intf.SAAJDocument;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;

public class DDOMSAAJUtil extends SAAJUtil {
    public static final DDOMSAAJUtil INSTANCE = new DDOMSAAJUtil();
    
    private final NodeFactory nodeFactory;
    
    private DDOMSAAJUtil() {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(DDOMSAAJUtil.class.getClassLoader());
        try {
            nodeFactory = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("saaj")).getNodeFactory();
        } catch (ModelLoaderException ex) {
            throw new Error(ex);
        }
    }
    
    @Override
    public SOAPElement createSOAPElement(String namespaceURI, String localName, String prefix) {
        Document document = (Document)nodeFactory.createDocument();
        return (SOAPElement)document.createElementNS(namespaceURI, prefix == null ? localName : prefix + ":" + localName);
    }

    @Override
    public SOAPEnvelope createSOAP11Envelope() {
        // TODO: we may also create the envelope without an owner document
        return ((SAAJDocument)nodeFactory.createDocument()).createSOAP11Envelope();
    }
    
    @Override
    public SOAPEnvelope createSOAP12Envelope() {
        // TODO: we may also create the envelope without an owner document
        return ((SAAJDocument)nodeFactory.createDocument()).createSOAP12Envelope();
    }
}
