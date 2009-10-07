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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.xmlts.XMLConformanceTest.Type;

public class XMLConformanceTestSuite {
    private static final Map<String,Type> typeMap = new HashMap<String,Type>();
    
    static {
        typeMap.put("valid", Type.VALID);
        typeMap.put("invalid", Type.INVALID);
        typeMap.put("not-wf", Type.NOT_WELL_FORMED);
        typeMap.put("error", Type.ERROR);
    }
    
    private static XMLConformanceTestSuite instance;
    
    private final List<XMLConformanceTest> tests = new LinkedList<XMLConformanceTest>();
    
    private static String getElementText(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder buffer = new StringBuilder();
        int level = 0;
        while (reader.next() != XMLStreamReader.END_ELEMENT || level != 0) {
            if (reader.isCharacters()) {
                buffer.append(reader.getText());
            } else if (reader.isStartElement()) {
                level++;
            } else if (reader.isEndElement()) {
                level--;
            }
        }
        return buffer.toString();
    }

    public synchronized static XMLConformanceTestSuite load() {
        if (instance == null) {
            XMLConformanceTestSuite suite = new XMLConformanceTestSuite();
            try {
                suite.load(XMLConformanceTestSuite.class.getResource("/xmlconf/xmlconf.xml"));
            } catch (Exception ex) {
                throw new RuntimeException("Could not load test suite", ex);
            }
            instance = suite;
        }
        return instance;
    }
    
    private void load(URL url) throws IOException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in = url.openStream();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(url.toExternalForm(), in);
            while (!reader.isStartElement()) {
                reader.next();
            }
            reader.require(XMLStreamReader.START_ELEMENT, null, "TESTSUITE");
            while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
                parseTestCases(reader, url);
            }
        } finally {
            in.close();
        }
    }
    
    private void parseTestCases(XMLStreamReader reader, URL base) throws IOException, XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "TESTCASES");
        String relativeBase = reader.getAttributeValue(XMLConstants.XML_NS_URI, "base");
        if (relativeBase != null) {
            base = new URL(base, relativeBase);
        }
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            if (reader.getLocalName().equals("TESTCASES")) {
                parseTestCases(reader, base);
            } else {
                parseTest(reader, base);
            }
        }
    }
    
    private void parseTest(XMLStreamReader reader, URL base) throws IOException, XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "TEST");
        String uri = reader.getAttributeValue(null, "URI");
        tests.add(new XMLConformanceTest(
                typeMap.get(reader.getAttributeValue(null, "TYPE")),
                new URL(base, uri),
                getElementText(reader)));
    }
    
    public Collection<XMLConformanceTest> getTests() {
        return Collections.unmodifiableCollection(tests);
    }
    
    public Collection<XMLConformanceTest> getTestsByType(Set<Type> types) {
        List<XMLConformanceTest> result = new LinkedList<XMLConformanceTest>();
        for (XMLConformanceTest test : tests) {
            if (types.contains(test.getType())) {
                result.add(test);
            }
        }
        return result;
    }
    
    public Collection<XMLConformanceTest> getTestsByType(Type type) {
        return getTestsByType(Collections.singleton(type));
    }
}
