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
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XSLTConformanceTestSuite {
    private static XSLTConformanceTestSuite instance;
    
    private XSLTConformanceTestSuite() {}
    
    public synchronized static XSLTConformanceTestSuite load() {
        if (instance == null) {
            XSLTConformanceTestSuite suite = new XSLTConformanceTestSuite();
            try {
                suite.doLoad();
            } catch (Exception ex) {
                throw new RuntimeException("Could not load test suite", ex);
            }
            instance = suite;
        }
        return instance;
    }
    
    private void doLoad() throws XMLStreamException, IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in = XSLTConformanceTestSuite.class.getResourceAsStream("/catalog.xml");
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(in);
            while (!reader.isStartElement()) {
                reader.next();
            }
            reader.require(XMLStreamReader.START_ELEMENT, null, "test-suite");
            while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
                parseTestCatalog(reader);
            }
        } finally {
            in.close();
        }
    }
    
    private void parseTestCatalog(XMLStreamReader reader) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "test-catalog");
        String majorPath = null;
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            String name = reader.getLocalName();
            if (name.equals("test-case")) {
                parseTestCase(reader);
            } else {
                String value = reader.getElementText();
                if (name.equals("major-path")) {
                    majorPath = value;
                }
            }
        }
    }
    
    private void parseTestCase(XMLStreamReader reader) throws XMLStreamException {
        reader.require(XMLStreamReader.START_ELEMENT, null, "test-case");
        String input = null;
        String stylesheet = null;
        String output = null;
        String compare = null;
        Map<String,String> discretionaryChoices = null;
        while (reader.nextTag() == XMLStreamReader.START_ELEMENT) {
            String name = reader.getLocalName();
            if (name.equals("scenario")) {
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
                reader.getElementText();
            }
        }
        System.out.println(input + " " + stylesheet + " " + output + " " + compare + " " + discretionaryChoices);
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
    
    public static void main(String[] args) {
        load();
    }
}
