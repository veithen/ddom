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
package com.google.code.ddom.frontend.dom;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class JAXPDOMUtil extends DOMUtil {
    protected abstract DocumentBuilderFactory createDocumentBuilderFactory();
    
    @Override
    public Document newDocument() {
        try {
            DocumentBuilderFactory factory = createDocumentBuilderFactory();
            return factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }
    
    @Override
    public Document parse(boolean namespaceAware, InputSource source) {
        try {
            DocumentBuilderFactory factory = createDocumentBuilderFactory();
            factory.setNamespaceAware(namespaceAware);
            return factory.newDocumentBuilder().parse(source);
        } catch (SAXException ex) {
            throw new Error(ex);
        } catch (IOException ex) {
            throw new Error(ex);
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }

    @Override
    public DOMImplementation getDOMImplementation() {
        try {
            return createDocumentBuilderFactory().newDocumentBuilder().getDOMImplementation();
        } catch (ParserConfigurationException ex) {
            throw new Error(ex);
        }
    }
}
