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
package com.google.code.ddom.xmlts;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

public class XMLConformanceTestSuiteTest {
    private static XMLConformanceTestSuite suite;
    
    @BeforeClass
    public static void load() {
        suite = XMLConformanceTestSuite.load();
    }
    
    @Test
    public void testNumberOfTests() {
        Assert.assertTrue(suite.getTests().size() > 2000);
    }
    
    @Test
    public void testIDsAreUnique() {
        Set<String> ids = new HashSet<String>();
        for (XMLConformanceTest test : suite.getTests()) {
            String id = test.getId();
            Assert.assertTrue("Duplicate ID: " + id, ids.add(test.getId()));
        }
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
    
/*
    @Test
    public void testParseWithWoodstox() throws Exception {
        XMLInputFactory factory = new WstxInputFactory();
        for (XMLConformanceTest test : suite.getTestsByType(XMLConformanceTest.Type.VALID)) {
            factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, test.isUsingNamespaces());
            InputStream in = test.getInputStream();
            try {
                XMLStreamReader reader = factory.createXMLStreamReader(test.getSystemId(), test.getInputStream());
                try {
                    while (reader.hasNext()) {
                        reader.next();
                    }
                } catch (XMLStreamException ex) {
                    System.out.println("Failing test: " + test.getSystemId() + " [ID: " + test.getId() + "]");
                    throw ex;
                } finally {
                    reader.close();
                }
            } finally {
                in.close();
            }
        }
    }
*/
}
