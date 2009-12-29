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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.collections.Filter;
import com.google.code.ddom.collections.FilteredCollection;

public class XSLTConformanceTestSuite {
    private static XSLTConformanceTestSuite instance;
    
    private static final Set<String> ignoredTests = new HashSet<String>(Arrays.asList(new String[] {
            "Keys_PerfRepro3", // Hangs or takes too much time
    }));
    
    private final List<XSLTConformanceTest> tests = new LinkedList<XSLTConformanceTest>();
    
    private XSLTConformanceTestSuite() {}
    
    public synchronized static XSLTConformanceTestSuite load() {
        if (instance == null) {
            XSLTConformanceTestSuite suite = new XSLTConformanceTestSuite();
            try {
                Set<String> testsInDoubt = new HashSet<String>();
                loadDoubts(testsInDoubt, XSLTConformanceTestSuite.class.getResource("/doubts.xml"));
                loadDoubts(testsInDoubt, XSLTConformanceTestSuite.class.getResource("doubts.xml"));
                suite.loadCatalog(testsInDoubt);
            } catch (Exception ex) {
                throw new RuntimeException("Could not load test suite", ex);
            }
            instance = suite;
        }
        return instance;
    }
    
    private static Set<String> loadDoubts(Set<String> testsInDoubt, URL url) throws XMLStreamException, IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in = url.openStream();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            while (!reader.isStartElement()) {
                reader.next();
            }
            reader.require(XMLStreamReader.START_ELEMENT, null, "test-suite");
            while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
                parseDoubtsTestCatalog(reader, testsInDoubt);
            }
        } finally {
            in.close();
        }
        return testsInDoubt;
    }
    
    private static void parseDoubtsTestCatalog(XMLStreamReader reader, Set<String> testsInDoubt) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "test-catalog");
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            parseDoubtsTestCase(reader, testsInDoubt);
        }
    }
    
    private static void parseDoubtsTestCase(XMLStreamReader reader, Set<String> testsInDoubt) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "test-case");
        String id = reader.getAttributeValue(null, "id");
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            String name = reader.getLocalName();
            if (name.equals("serial") || name.equals("processor-specific")) {
                reader.nextTag();
            } else if (name.equals("extension")) {
                testsInDoubt.add(id);
                reader.nextTag();
            } else if (name.equals("doubt")) {
                testsInDoubt.add(id);
                reader.getElementText();
            } else {
                parseGrayArea(reader);
                testsInDoubt.add(id);
            }
        }
    }
    
    private static void parseGrayArea(XMLStreamReader reader) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "gray-area");
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            // For the moment we simply skip the details of the gray-area
            reader.require(XMLStreamReader.START_ELEMENT, null, "gray-area-choice");
            reader.nextTag();
        }
    }
    
    private void loadCatalog(Set<String> testsInDoubt) throws XMLStreamException, IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in = XSLTConformanceTestSuite.class.getResourceAsStream("/catalog.xml");
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            while (!reader.isStartElement()) {
                reader.next();
            }
            reader.require(XMLStreamReader.START_ELEMENT, null, "test-suite");
            while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
                parseCatalogTestCatalog(reader, testsInDoubt);
            }
        } finally {
            in.close();
        }
    }
    
    private void parseCatalogTestCatalog(XMLStreamReader reader, Set<String> testsInDoubt) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "test-catalog");
        String submitter = reader.getAttributeValue(null, "submitter");
        String majorPath = null;
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            String name = reader.getLocalName();
            if (name.equals("test-case")) {
                parseCatalogTestCase(reader, submitter, majorPath, testsInDoubt);
            } else {
                String value = reader.getElementText();
                if (name.equals("major-path")) {
                    majorPath = value;
                }
            }
        }
    }
    
    private void parseCatalogTestCase(XMLStreamReader reader, String submitter, String majorPath, Set<String> testsInDoubt) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "test-case");
        String id = reader.getAttributeValue(null, "id");
        String filePath = null;
        String input = null;
        String stylesheet = null;
        String output = null;
        String compare = null;
        String operation = null;
        Map<String,String> discretionaryChoices = null;
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            String name = reader.getLocalName();
            if (name.equals("scenario")) {
                operation = reader.getAttributeValue(null, "operation");
                while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
                    String role = reader.getAttributeValue(null, "role");
                    if (reader.getLocalName().equals("input-file")) {
                        String value = reader.getElementText();
                        if (role.equals("principal-data")) {
                            input = value;
                        } else if (role.equals("principal-stylesheet")) {
                            stylesheet = value;
                        }
                    } else {
                        compare = reader.getAttributeValue(null, "compare");
                        output = reader.getElementText();
                    }
                }
            } else if (name.equals("discretionary")) {
                discretionaryChoices = parseDiscretionary(reader);
            } else {
                String value = reader.getElementText();
                if (name.equals("file-path")) {
                    filePath = value;
                }
            }
        }
        if (!ignoredTests.contains(id)) {
            tests.add(new XSLTConformanceTest(
                    id,
                    operation.equals("execution-error"),
                    testsInDoubt.contains(id),
                    XSLTConformanceTestSuite.class.getResource("/" + majorPath + "/" + filePath + "/" + input),
                    XSLTConformanceTestSuite.class.getResource("/" + majorPath + "/" + filePath + "/" + stylesheet),
                    XSLTConformanceTestSuite.class.getResource("/" + majorPath + "/REF_OUT/" + filePath + "/" + output), compare));
        }
    }
    
    private Map<String,String> parseDiscretionary(XMLStreamReader reader) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "discretionary");
        Map<String,String> discretionaryChoices = new HashMap<String,String>();
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            reader.require(XMLStreamReader.START_ELEMENT, null, "discretionary-choice");
            discretionaryChoices.put(reader.getAttributeValue(null, "name"), reader.getAttributeValue(null, "behavior"));
            reader.nextTag();
        }
        return discretionaryChoices;
    }
    
    public Collection<XSLTConformanceTest> getTests() {
        return Collections.unmodifiableCollection(tests);
    }
    
    public Collection<XSLTConformanceTest> getTests(Filter<? super XSLTConformanceTest> filter) {
        return new FilteredCollection<XSLTConformanceTest>(tests, filter);
    }
}
