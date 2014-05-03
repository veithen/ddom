/*
 * Copyright 2009-2010,2014 Andreas Veithen
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
package com.googlecode.ddom.frontend.dom.xmlts;

import junit.framework.TestSuite;

import org.apache.axiom.testutils.suite.MatrixTestCase;
import org.apache.axiom.testutils.suite.MatrixTestSuiteBuilder;
import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

import com.google.code.ddom.collections.AndFilter;
import com.google.code.ddom.xmlts.Filters;
import com.google.code.ddom.xmlts.XMLConformanceTest;
import com.google.code.ddom.xmlts.XMLConformanceTestSuite;
import com.google.code.ddom.xmlts.XMLConformanceTestUtils;
import com.googlecode.ddom.frontend.dom.DDOMUtil;
import com.googlecode.ddom.frontend.dom.DOMUtil;
import com.googlecode.ddom.frontend.dom.XercesDOMUtil;

// TODO: this test should use the DDOM parser, not Woodstox
public class InfosetTest extends MatrixTestCase {
    private final XMLConformanceTest test;
    
    public InfosetTest(XMLConformanceTest test) {
        this.test = test;
        addTestParameter("id", test.getId());
    }

    private Document loadDocument(DOMUtil domUtil) {
        Document document = domUtil.parse(test.isUsingNamespaces(), test.getUrl());
        
        XMLConformanceTestUtils.coalesceTextNodes(document);
        
        // Xerces removes empty CDATA sections, while Woodstox/DDOM preserves them
        // TODO: once we implement support for EmptyCDATASectionPolicy, this should no longer be required
        XMLConformanceTestUtils.removeEmptyCDATASections(document);
        
        // TODO: once we correctly support DOM3 (see section 1.3.4 of the DOM Level 3 Core spec), this should no longer be necessary
        XMLConformanceTestUtils.removeXmlBaseAttributes(document);
        
        return document;
    }
    
    @Override
    protected void runTest() {
        // TODO: at some point we should check that the documents are identical rather than equal
        XMLAssert.assertXMLEqual(loadDocument(XercesDOMUtil.INSTANCE), loadDocument(DDOMUtil.INSTANCE));
    }

    public static TestSuite suite() {
        MatrixTestSuiteBuilder builder = new MatrixTestSuiteBuilder() {
            @Override
            protected void addTests() {
                for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
                    addTest(new InfosetTest(test));
                }
            }
        };
        return builder.build();
    }
}
