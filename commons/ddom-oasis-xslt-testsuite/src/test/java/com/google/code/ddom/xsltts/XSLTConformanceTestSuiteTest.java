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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class XSLTConformanceTestSuiteTest extends TestCase {
    private final XSLTConformanceTest test;
    
    public XSLTConformanceTestSuiteTest(XSLTConformanceTest test) {
        super(test.getId());
        this.test = test;
    }

    @Override
    protected void runTest() throws Throwable {
        DocumentBuilderFactory documentBuilderFactory = new DocumentBuilderFactoryImpl();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        test.execute(documentBuilder, documentBuilder, new TransformerFactoryImpl());
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XSLTConformanceTest test : XSLTConformanceTestSuite.load().getTests()) {
            if (!test.isErrorScenario() && !test.isInDoubt()) {
                suite.addTest(new XSLTConformanceTestSuiteTest(test));
            }
        }
        return suite;
    }
}
