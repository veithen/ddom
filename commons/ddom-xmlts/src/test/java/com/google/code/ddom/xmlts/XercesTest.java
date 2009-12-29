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
package com.google.code.ddom.xmlts;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

import com.google.code.ddom.collections.AndFilter;

public class XercesTest extends TestCase {
    private final XMLConformanceTest test;
    
    public XercesTest(XMLConformanceTest test) {
        super(test.getId());
        this.test = test;
    }

    @Override
    protected void runTest() throws Throwable {
        DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
        // Reference output documents don't contain CDATA sections and comments
        factory.setCoalescing(true);
        factory.setIgnoringComments(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document actual = builder.parse(test.getSystemId());
        String outputSystemId = test.getOutputSystemId();
        if (outputSystemId != null) {
            Document expected = builder.parse(outputSystemId);
            XMLAssert.assertXMLEqual(expected, actual);
        }
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
            suite.addTest(new XercesTest(test));
        }
        return suite;
    }
}
