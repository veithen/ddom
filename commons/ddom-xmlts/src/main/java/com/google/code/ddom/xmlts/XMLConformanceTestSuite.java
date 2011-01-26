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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.collections.Filter;
import com.google.code.ddom.collections.FilteredCollection;
import com.google.code.ddom.xmlts.XMLConformanceTest.Type;

public class XMLConformanceTestSuite {
    /**
     * Default set of XML versions for which tests are returned. This is XML 1.0 4th edition and XML 1.1.
     */
    @Deprecated
    public static final Set<XMLVersion> DEFAULT_VERSIONS = Collections.unmodifiableSet(EnumSet.of(XMLVersion.XML_1_0_EDITION_4, XMLVersion.XML_1_1));
    
    private static final Map<String,Type> typeMap = new HashMap<String,Type>();
    
    static {
        typeMap.put("valid", Type.VALID);
        typeMap.put("invalid", Type.INVALID);
        typeMap.put("not-wf", Type.NOT_WELL_FORMED);
        typeMap.put("error", Type.ERROR);
    }
    
    private static XMLConformanceTestSuite instance;
    
    private final Map<String,XMLConformanceTest> tests = new LinkedHashMap<String,XMLConformanceTest>();
    
    private XMLConformanceTestSuite() {}
    
    public synchronized static XMLConformanceTestSuite load() {
        if (instance == null) {
            XMLConformanceTestSuite suite = new XMLConformanceTestSuite();
            try {
                suite.load(XMLConformanceTestSuite.class.getResource("/xmlconf/xmlconf.xml"),
                        loadExclusions(XMLConformanceTestSuite.class.getResource("exclusions")));
            } catch (Exception ex) {
                throw new RuntimeException("Could not load test suite", ex);
            }
            instance = suite;
        }
        return instance;
    }
    
    private static Set<String> loadExclusions(URL url) throws IOException {
        Set<String> result = new HashSet<String>();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.length() != 0 && line.charAt(0) != '#') {
                    result.add(line);
                }
            }
        } finally {
            in.close();
        }
        return result;
    }
    
    private void require(Element element, String expected) {
        String actual = element.getTagName();
        if (!actual.equals(expected)) {
            throw new Error("Unexpected element " + actual + "; expected " + expected);
        }
    }
    
    private void load(URL url, Set<String> exclusions) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Element root = documentBuilder.parse(url.toExternalForm()).getDocumentElement();
        require(root, "TESTSUITE");
        Node child = root.getFirstChild();
        while (child != null) {
            if (child instanceof Element) {
                parseTestCases((Element)child, url, exclusions);
            }
            child = child.getNextSibling();
        }
    }
    
    private void parseTestCases(Element element, URL base, Set<String> exclusions) throws IOException {
        require(element, "TESTCASES");
        String relativeBase = element.getAttribute("xml:base");
        if (relativeBase != null) {
            base = new URL(base, relativeBase);
        }
        Node child = element.getFirstChild();
        while (child != null) {
            if (child instanceof Element) {
                Element childElement = (Element)child;
                if (childElement.getTagName().equals("TESTCASES")) {
                    parseTestCases(childElement, base, exclusions);
                } else {
                    parseTest(childElement, base, exclusions);
                }
            }
            child = child.getNextSibling();
        }
    }
    
    private void parseTest(Element element, URL base, Set<String> exclusions) throws IOException {
        require(element, "TEST");
        String id = element.getAttribute("ID");
        String uri = element.getAttribute("URI");
        String output = element.getAttribute("OUTPUT");
        String version = element.getAttribute("VERSION");
        String edition = element.getAttribute("EDITION");
        Set<XMLVersion> versions;
        if (version.length() == 0 || version.equals("1.0")) {
            if (edition.length() == 0) {
                versions = EnumSet.complementOf(EnumSet.of(XMLVersion.XML_1_1));
            } else {
                String[] editionArray = edition.split(" ");
                XMLVersion[] versionArray = new XMLVersion[editionArray.length];
                for (int i=0; i<editionArray.length; i++) {
                    versionArray[i] = XMLVersion.valueOf("XML_1_0_EDITION_" + editionArray[i]);
                }
                versions = EnumSet.copyOf(Arrays.asList(versionArray));
            }
        } else if (version.equals("1.1")) {
            versions = Collections.singleton(XMLVersion.XML_1_1);
        } else {
            throw new RuntimeException("Unrecognized version " + version);
        }
        tests.put(id, new XMLConformanceTest(
                id,
                exclusions.contains(id) ? Type.EXCLUDED : typeMap.get(element.getAttribute("TYPE")),
                versions,
                !"no".equals(element.getAttribute("NAMESPACE")),
                new URL(base, uri),
                output.length() == 0 ? null : new URL(base, output),
                element.getTextContent()));
    }
    
    public XMLConformanceTest getTest(String id) {
        return tests.get(id);
    }
    
    public Collection<XMLConformanceTest> getAllTests() {
        return Collections.unmodifiableCollection(tests.values());
    }
    
    public Collection<XMLConformanceTest> getTests(Filter<? super XMLConformanceTest> filter) {
        return new FilteredCollection<XMLConformanceTest>(tests.values(), filter);
    }
    
    @Deprecated
    public Collection<XMLConformanceTest> getTests(Set<XMLVersion> versions, Set<Type> types) {
        List<XMLConformanceTest> result = new LinkedList<XMLConformanceTest>();
        for (XMLConformanceTest test : tests.values()) {
            if ((versions == null || !Collections.disjoint(test.getXmlVersions(), versions))
                    && (types == null || types.contains(test.getType()))) {
                result.add(test);
            }
        }
        return result;
    }
    
    @Deprecated
    public Collection<XMLConformanceTest> getTests() {
        return getTests(DEFAULT_VERSIONS, null);
    }
    
    @Deprecated
    public Collection<XMLConformanceTest> getTestsForXMLVersions(Set<XMLVersion> versions) {
        return getTests(versions, null);
    }
    
    @Deprecated
    public Collection<XMLConformanceTest> getTestsByType(Set<Type> types) {
        return getTests(DEFAULT_VERSIONS, types);
    }
    
    @Deprecated
    public Collection<XMLConformanceTest> getTestsByType(Type type) {
        return getTests(DEFAULT_VERSIONS, Collections.singleton(type));
    }
}
