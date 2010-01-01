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
package com.google.code.ddom.frontend.dom.xmlts;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.code.ddom.collections.AndFilter;
import com.google.code.ddom.frontend.dom.DDOMUtil;
import com.google.code.ddom.frontend.dom.DOMUtil;
import com.google.code.ddom.frontend.dom.XercesDOMUtil;
import com.google.code.ddom.xmlts.Filters;
import com.google.code.ddom.xmlts.XMLConformanceTest;
import com.google.code.ddom.xmlts.XMLConformanceTestSuite;
import com.google.code.ddom.xmlts.XMLConformanceTestUtils;

public class InfosetTest extends TestCase {
    private final XMLConformanceTest test;
    
    public InfosetTest(XMLConformanceTest test) {
        super(test.getId() + "__" + test.getUrl().toExternalForm());
        this.test = test;
    }

    private Document loadDocument(DOMUtil domUtil) {
        Document document = domUtil.parse(test.isUsingNamespaces(), test.getUrl());
        
        // Coalesce Text nodes
        document.getDomConfig().setParameter("cdata-sections", true);
        document.normalize();
        
        // Xerces removes empty CDATA sections, while Woodstox/DDOM preserves them
        removeEmptyCDATASections(document);
        
        // TODO: once we correctly support DOM3 (see section 1.3.4 of the DOM Level 3 Core spec), this should no longer be necessary
        XMLConformanceTestUtils.removeXmlBaseAttributes(document);
        
        return document;
    }
    
    // TODO: once we implement support for EmptyCDATASectionPolicy, this should no longer be required
    private void removeEmptyCDATASections(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            Node next = child.getNextSibling();
            switch (child.getNodeType()) {
                case Node.CDATA_SECTION_NODE:
                    if (child.getNodeValue().length() == 0) {
                        child.getParentNode().removeChild(child);
                    }
                    break;
                case Node.ELEMENT_NODE:
                    removeEmptyCDATASections(child);
            }
            child = next;
        }
    }
    
    @Override
    protected void runTest() {
        // TODO: at some point we should check that the documents are identical rather than equal
        XMLAssert.assertXMLEqual(loadDocument(XercesDOMUtil.INSTANCE), loadDocument(DDOMUtil.INSTANCE));
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
            suite.addTest(new InfosetTest(test));
        }
        return suite;
    }
}
