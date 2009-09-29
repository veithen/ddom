/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.dom.impl;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XercesDOMUtilImpl implements DOMUtilImpl {
    public static final XercesDOMUtilImpl INSTANCE = new XercesDOMUtilImpl();
    
    public Document newDocument() {
        try {
            DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
            return factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }
    
    public Document parse(boolean namespaceAware, String xml) {
        try {
            DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
            factory.setNamespaceAware(namespaceAware);
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (SAXException ex) {
            throw new Error(ex);
        } catch (IOException ex) {
            throw new Error(ex);
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }
}
