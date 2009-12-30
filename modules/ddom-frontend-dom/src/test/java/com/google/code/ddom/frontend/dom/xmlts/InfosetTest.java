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

    @Override
    protected void runTest() {
        Document expected = XercesDOMUtilImpl.INSTANCE.parse(test.isUsingNamespaces(), test.getUrl());
        Document actual = DDOMUtilImpl.INSTANCE.parse(test.isUsingNamespaces(), test.getUrl());
        // TODO: need to normalize (using Node#normalize) to avoid differences because of non coalesced text nodes
        // TODO: at some point we should check that the documents are identical rather than equal
        XMLAssert.assertXMLEqual(expected, actual);
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite();
        for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
            suite.addTest(new InfosetTest(test));
        }
        return suite;
    }
}
