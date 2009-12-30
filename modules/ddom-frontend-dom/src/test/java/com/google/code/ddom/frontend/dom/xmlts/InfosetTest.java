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

import com.google.code.ddom.collections.AndFilter;
import com.google.code.ddom.frontend.dom.DDOMUtilImpl;
import com.google.code.ddom.frontend.dom.DOMUtilImpl;
import com.google.code.ddom.frontend.dom.XercesDOMUtilImpl;
import com.google.code.ddom.xmlts.Filters;
import com.google.code.ddom.xmlts.XMLConformanceTest;
import com.google.code.ddom.xmlts.XMLConformanceTestSuite;

public class InfosetTest extends TestCase {
    private final XMLConformanceTest test;
    
    public InfosetTest(XMLConformanceTest test) {
        super(test.getId() + "__" + test.getUrl().toExternalForm());
        this.test = test;
    }

    private Document loadDocument(DOMUtilImpl domUtil) {
        Document document = domUtil.parse(test.isUsingNamespaces(), test.getUrl());
        
        // Coalesce Text nodes
        document.getDomConfig().setParameter("cdata-sections", true);
        document.normalize();
        
        return document;
    }
    
    @Override
    protected void runTest() {
        // TODO: need to normalize (using Node#normalize) to avoid differences because of non coalesced text nodes
        // TODO: at some point we should check that the documents are identical rather than equal
        XMLAssert.assertXMLEqual(loadDocument(XercesDOMUtilImpl.INSTANCE), loadDocument(DDOMUtilImpl.INSTANCE));
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
            suite.addTest(new InfosetTest(test));
        }
        return suite;
    }
}
