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
package com.google.code.ddom.tests.xslt;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

import com.google.code.ddom.xsltts.Filters;
import com.google.code.ddom.xsltts.StrictErrorListener;
import com.google.code.ddom.xsltts.XSLTConformanceTest;
import com.google.code.ddom.xsltts.XSLTConformanceTestSuite;
import com.googlecode.ddom.jaxp.DocumentBuilderFactoryImpl;

public class XSLTTestCase extends TestCase {
    private final TransformerFactory transformerFactory;
    private final XSLTConformanceTest test;
    
    public XSLTTestCase(TransformerFactory transformerFactory, XSLTConformanceTest test) {
        super(test.getId());
        this.transformerFactory = transformerFactory;
        this.test = test;
    }

    @Override
    protected void runTest() throws Throwable {
        DocumentBuilderFactory refDocumentBuilderFactory = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
        refDocumentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder refDocumentBuilder = refDocumentBuilderFactory.newDocumentBuilder();
        
        DocumentBuilderFactory documentBuilderFactory = new DocumentBuilderFactoryImpl();
        documentBuilderFactory.setNamespaceAware(true);
        // Some of the XSLTs in the test suite have output that depends on the attribute order
        // used in the input. Since Xerces sorts attributes, we need to do the same with DDOM.
        documentBuilderFactory.setFeature(DocumentBuilderFactoryImpl.FEATURE_SORT_ATTRIBUTES, true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        
        String inputSystemId = test.getInput().toExternalForm();
        String stylesheetSystemId = test.getStylesheet().toExternalForm();
        
        DOMSource refInputSource = new DOMSource(refDocumentBuilder.parse(inputSystemId), inputSystemId);
        DOMSource refStylesheetSource = new DOMSource(refDocumentBuilder.parse(stylesheetSystemId), stylesheetSystemId);
        Transformer refTransformer = transformerFactory.newTransformer(refStylesheetSource);
        refTransformer.setErrorListener(StrictErrorListener.INSTANCE);

        DOMSource inputSource = new DOMSource(documentBuilder.parse(inputSystemId), inputSystemId);
        DOMSource stylesheetSource = new DOMSource(documentBuilder.parse(stylesheetSystemId), stylesheetSystemId);
        Transformer transformer = transformerFactory.newTransformer(stylesheetSource);
        transformer.setErrorListener(StrictErrorListener.INSTANCE);
        
        String method = refTransformer.getOutputProperty(OutputKeys.METHOD);
        boolean isXmlOutput = method == null || method.equals("xml");
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
                    try {
                        XMLAssert.assertXMLEqual(refOutputDocument, outputDocument);
                    } catch (AssertionFailedError ex) {
                        // Dump the two documents for investigation
                        Transformer identity = transformerFactory.newTransformer();
                        StreamResult sysOut = new StreamResult(System.out);
                        System.out.print("\nReference input:\n");
                        identity.transform(refInputSource, sysOut);
                        System.out.print("\nActual input:\n");
                        identity.transform(inputSource, sysOut);
                        System.out.print("\nReference output:\n");
                        identity.transform(new DOMSource(refOutputDocument), sysOut);
                        System.out.print("\nActual output:\n");
                        identity.transform(new DOMSource(outputDocument), sysOut);
                        throw ex;
                    }
                }
            } else {
                // TODO
            }
        } while (repeat);
    }

    public static TestSuite suite(TransformerFactory transformerFactory) {
        transformerFactory.setErrorListener(StrictErrorListener.INSTANCE);
        TestSuite suite = new TestSuite();
        for (XSLTConformanceTest test : XSLTConformanceTestSuite.load().getTests(Filters.XALAN_2_7_1_FILTER)) {
            // TODO: resolve the issues in the two remaining test cases (related to entities and xml:base)
            if (!test.isErrorScenario() && !test.isInDoubt() && !test.getId().equals("expression_expression02") && !test.getId().equals("copy_copy21")) {
                suite.addTest(new XSLTTestCase(transformerFactory, test));
            }
        }
        return suite;
    }
}
