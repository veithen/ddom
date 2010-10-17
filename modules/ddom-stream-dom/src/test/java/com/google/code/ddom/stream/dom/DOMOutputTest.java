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
package com.google.code.ddom.stream.dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;

import com.google.code.ddom.Options;
import com.google.code.ddom.collections.AndFilter;
import com.google.code.ddom.stream.Transformer;
import com.google.code.ddom.stream.options.NamespaceAwareness;
import com.google.code.ddom.xmlts.Filters;
import com.google.code.ddom.xmlts.XMLConformanceTest;
import com.google.code.ddom.xmlts.XMLConformanceTestSuite;
import com.google.code.ddom.xmlts.XMLConformanceTestUtils;

public class DOMOutputTest extends TestCase {
    private static final Transformer transformer = Transformer.getInstance();
    
    private final XMLConformanceTest test;

    public DOMOutputTest(XMLConformanceTest test) {
        super(test.getName());
        this.test = test;
    }

    @Override
    protected void runTest() throws Throwable {
        DocumentBuilderFactory domFactory = new DocumentBuilderFactoryImpl();
        domFactory.setNamespaceAware(test.isUsingNamespaces());
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        Document actual = domBuilder.newDocument();
        
        Options options = new Options();
        options.set(NamespaceAwareness.get(test.isUsingNamespaces()));
        transformer.from(test.getInputSource(), options, false).to(actual);
        XMLConformanceTestUtils.coalesceTextNodes(actual);
        // TODO: once we implement support for EmptyCDATASectionPolicy, this should no longer be required
        XMLConformanceTestUtils.removeEmptyCDATASections(actual);
        
        Document expected = domBuilder.parse(test.getSystemId());
        // TODO: need to check if we should enhance the event model to include the necessary information to generate xml:base attributes
        XMLConformanceTestUtils.removeXmlBaseAttributes(expected);
        
        // TODO: at some point we should check that the documents are identical rather than equal
        XMLAssert.assertXMLEqual(XMLUnit.compareXML(expected, actual), true);
    }
    
    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
            suite.addTest(new DOMOutputTest(test));
        }
        return suite;
    }
}
