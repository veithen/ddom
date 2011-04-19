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
package com.googlecode.ddom.stream.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;

import com.google.code.ddom.collections.AndFilter;
import com.google.code.ddom.xmlts.Filters;
import com.google.code.ddom.xmlts.XMLConformanceTest;
import com.google.code.ddom.xmlts.XMLConformanceTestSuite;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.dom.DOMInput;
import com.googlecode.ddom.stream.dom.DOMOutput;

public class Parser2DOMOutputTest extends TestCase {
    private final XMLConformanceTest test;
    private final boolean flush;

    public Parser2DOMOutputTest(XMLConformanceTest test, boolean flush) {
        super(test.getName());
        this.test = test;
        this.flush = flush;
        setName(getName() + " [flush=" + flush + "]");
    }

    @Override
    protected void runTest() throws Throwable {
        DocumentBuilderFactory domFactory = new DocumentBuilderFactoryImpl();
        domFactory.setNamespaceAware(test.isUsingNamespaces());
        domFactory.setExpandEntityReferences(false);
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        Document original = domBuilder.parse(test.getSystemId());
        Document expected = domBuilder.newDocument();
        // DOMOutput has some intrinsic limitations. To get comparable documents,
        // we need to pass the original/expected document through DOMOutput as well:
        new Stream(new DOMInput(original), new DOMOutput(expected)).flush();
        Document actual = domBuilder.newDocument();
        Parser parser = new Parser(test.getInputStream(), null, test.isUsingNamespaces());
        DOMOutput output = new DOMOutput(actual);
        if (flush) {
            new Stream(parser, output).flush();
        } else {
            EventCountingFilter filter = new EventCountingFilter();
            parser.addFilter(filter);
            Stream stream = new Stream(parser, output);
            while (!stream.isComplete()) {
                stream.proceed();
                filter.reset();
            }
        }
        XMLAssert.assertXMLIdentical(XMLUnit.compareXML(expected, actual), true);
    }
    
    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
            suite.addTest(new Parser2DOMOutputTest(test, false));
            suite.addTest(new Parser2DOMOutputTest(test, true));
        }
        return suite;
    }
}
