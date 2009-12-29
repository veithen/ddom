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
import javax.xml.transform.ErrorListener;
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
    private final URL input;
    private final URL stylesheet;
    private final URL output;
    private final String compare; // TODO: doesn't seem to be reliable
    
    XSLTConformanceTest(String id, URL input, URL stylesheet, URL output, String compare) {
        this.id = id;
        this.input = input;
        this.stylesheet = stylesheet;
        this.output = output;
        this.compare = compare;
    }
    
    public String getId() {
        return id;
    }

    private static DOMSource createDOMSource(DocumentBuilder documentBuilder, URL url) throws Exception {
        String systemId = url.toExternalForm();
        return new DOMSource(documentBuilder.parse(systemId), systemId);
    }
    
    public void execute(DocumentBuilder documentBuilder, TransformerFactory transformerFactory) throws Exception {
        DOMSource inputSource = createDOMSource(documentBuilder, input);
        DOMSource stylesheetSource = createDOMSource(documentBuilder, stylesheet);
        Transformer transformer = transformerFactory.newTransformer(stylesheetSource);
        transformer.setErrorListener(new ErrorListener() {
            public void warning(TransformerException exception) throws TransformerException {
            }
            
            public void fatalError(TransformerException exception) throws TransformerException {
                throw exception;
            }
            
            public void error(TransformerException exception) throws TransformerException {
                throw exception;
            }
        });
        String method = transformer.getOutputProperty(OutputKeys.METHOD);
        if (method.equals("xml")) {
            Document expectedOutputDocument = documentBuilder.parse(output.toExternalForm());
            Document actualOutputDocument = documentBuilder.newDocument();
            DOMResult outputResult = new DOMResult(actualOutputDocument);
            transformer.transform(inputSource, outputResult);
            XMLAssert.assertXMLEqual(expectedOutputDocument, actualOutputDocument);
        } else {
            // TODO
        }
    }
}
