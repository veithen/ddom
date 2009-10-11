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
package com.google.code.ddom.stream.sax;

import java.util.EnumSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;

import com.google.code.ddom.DeferredDocumentFactory;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.xmlts.XMLConformanceTest;
import com.google.code.ddom.xmlts.XMLConformanceTestSuite;

public class SAXStreamProviderTest extends TestCase {
    private final XMLConformanceTest test;

    public SAXStreamProviderTest(XMLConformanceTest test) {
        super(test.getId());
        this.test = test;
    }

    @Override
    protected void runTest() throws Throwable {
        SAXParserFactory saxFactory = new SAXParserFactoryImpl();
        saxFactory.setNamespaceAware(test.isUsingNamespaces());
        SAXSource source = new SAXSource(saxFactory.newSAXParser().getXMLReader(), test.getInputSource());
        Document actual = (Document)DeferredDocumentFactory.newInstance().parse("dom", source);
        ((CoreDocument)actual).build();
        
        DocumentBuilderFactory domFactory = new DocumentBuilderFactoryImpl();
        domFactory.setNamespaceAware(test.isUsingNamespaces());
        Document expected = domFactory.newDocumentBuilder().parse(test.getSystemId());
        XMLAssert.assertXMLIdentical(XMLUnit.compareXML(expected, actual), true);
    }
    
    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTestsByType(EnumSet.of(XMLConformanceTest.Type.VALID))) {
            suite.addTest(new SAXStreamProviderTest(test));
        }
        return suite;
    }
}
