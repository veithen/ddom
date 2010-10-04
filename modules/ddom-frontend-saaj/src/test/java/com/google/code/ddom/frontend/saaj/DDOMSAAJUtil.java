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
package com.google.code.ddom.frontend.saaj;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;

import org.w3c.dom.Document;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.frontend.saaj.intf.SAAJDocument;

public class DDOMSAAJUtil extends SAAJUtil {
    public static final DDOMSAAJUtil INSTANCE = new DDOMSAAJUtil();
    
    private static final DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance();
    
    private DDOMSAAJUtil() {}
    
    @Override
    public SOAPElement createSOAPElement(String namespaceURI, String localName, String prefix) {
        Document document = (Document)documentHelper.newDocument("saaj");
        return (SOAPElement)document.createElementNS(namespaceURI, prefix == null ? localName : prefix + ":" + localName);
    }

    @Override
    public SOAPEnvelope createSOAP11Envelope() {
        return ((SAAJDocument)documentHelper.newDocument("saaj")).createSOAP11Envelope();
    }
    
    @Override
    public SOAPEnvelope createSOAP12Envelope() {
        return ((SAAJDocument)documentHelper.newDocument("saaj")).createSOAP12Envelope();
    }
}
