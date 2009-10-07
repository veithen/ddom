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

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.Assert;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.ctc.wstx.stax.WstxInputFactory;

public class XMLConformanceTestSuiteTest {
    private static XMLConformanceTestSuite suite;
    
    @BeforeClass
    public static void load() {
        suite = XMLConformanceTestSuite.load();
    }
    
    @Test
    public void testNumberOfTests() {
        Assert.assertEquals(2570, suite.getTests().size());
    }
    
    @Test
    public void testLoadTestFiles() throws Exception {
        byte[] buffer = new byte[4096];
        for (XMLConformanceTest test : suite.getTests()) {
            InputStream in = test.getInputStream();
            try {
                while (in.read(buffer) != -1) {
                    // Just loop
                }
            } finally {
                in.close();
            }
        }
    }
    
    @Test
    public void testParseWithWoodstox() throws Exception {
        XMLInputFactory factory = new WstxInputFactory();
        for (XMLConformanceTest test : suite.getTestsByType(XMLConformanceTest.Type.VALID)) {
            InputStream in = test.getInputStream();
            try {
                XMLStreamReader reader = factory.createXMLStreamReader(test.getSystemId(), test.getInputStream());
                try {
                    while (reader.hasNext()) {
                        reader.next();
                    }
                } catch (XMLStreamException ex) {
                    System.out.println("Failing test: " + test.getSystemId());
                    throw ex;
                } finally {
                    reader.close();
                }
            } finally {
                in.close();
            }
        }
    }
    
    @Test
    public void testParseWithXerces() throws Exception {
        DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
        DocumentBuilder builder = factory.newDocumentBuilder();
        for (XMLConformanceTest test : suite.getTestsByType(XMLConformanceTest.Type.VALID)) {
            try {
                builder.parse(test.getSystemId());
            } catch (SAXException ex) {
                System.out.println("Failing test: " + test.getSystemId());
                throw ex;
            }
        }
    }
}
