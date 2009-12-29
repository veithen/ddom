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
package com.google.code.ddom.xsltts;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

public class XSLTConformanceTest {
    private final String id;
    private final boolean errorScenario;
    private final boolean inDoubt;
    private final URL input;
    private final URL stylesheet;
    private final URL output;
    private final String compare; // TODO: doesn't seem to be reliable
    
    XSLTConformanceTest(String id, boolean errorScenario, boolean inDoubt, URL input, URL stylesheet, URL output, String compare) {
        this.id = id;
        this.errorScenario = errorScenario;
        this.inDoubt = inDoubt;
        this.input = input;
        this.stylesheet = stylesheet;
        this.output = output;
        this.compare = compare;
    }
    
    public String getId() {
        return id;
    }

    public boolean isErrorScenario() {
        return errorScenario;
    }

    public boolean isInDoubt() {
        return inDoubt;
    }

    private static DOMSource createDOMSource(DocumentBuilder documentBuilder, URL url) throws Exception {
        String systemId = url.toExternalForm();
        return new DOMSource(documentBuilder.parse(systemId), systemId);
    }
    
    public void execute(DocumentBuilder refDocumentBuilder, DocumentBuilder documentBuilder, TransformerFactory transformerFactory) throws Exception {
        transformerFactory.setErrorListener(StrictErrorListener.INSTANCE);
        
        DOMSource refInputSource = createDOMSource(refDocumentBuilder, input);
        DOMSource refStylesheetSource = createDOMSource(refDocumentBuilder, stylesheet);
        Transformer refTransformer = transformerFactory.newTransformer(refStylesheetSource);
        refTransformer.setErrorListener(StrictErrorListener.INSTANCE);

        DOMSource inputSource = createDOMSource(documentBuilder, input);
        DOMSource stylesheetSource = createDOMSource(documentBuilder, stylesheet);
        Transformer transformer = transformerFactory.newTransformer(stylesheetSource);
        transformer.setErrorListener(StrictErrorListener.INSTANCE);
        
        boolean isXmlOutput = refTransformer.getOutputProperty(OutputKeys.METHOD).equals("xml");
        boolean repeat;
        do {
            repeat = false;
            if (isXmlOutput) {
                Document refOutputDocument = refDocumentBuilder.newDocument();
                DOMResult refOutputResult = new DOMResult(refOutputDocument);
                try {
                    refTransformer.transform(refInputSource, refOutputResult);
                } catch (TransformerException ex) {
                    isXmlOutput = false;
                    repeat = true;
                }
                if (!repeat) {
                    Document outputDocument = documentBuilder.newDocument();
                    DOMResult outputResult = new DOMResult(outputDocument);
                    refTransformer.transform(inputSource, outputResult);
                    XMLAssert.assertXMLEqual(refOutputDocument, outputDocument);
                }
            } else {
                // TODO
            }
        } while (repeat);
    }
}
