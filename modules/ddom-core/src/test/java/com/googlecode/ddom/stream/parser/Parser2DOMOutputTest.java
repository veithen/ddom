/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.stream.parser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.testutils.suite.MatrixTestCase;
import org.apache.axiom.testutils.suite.MatrixTestSuiteBuilder;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Document;

import com.google.code.ddom.collections.AndFilter;
import com.google.code.ddom.xmlts.Filters;
import com.google.code.ddom.xmlts.XMLConformanceTest;
import com.google.code.ddom.xmlts.XMLConformanceTestSuite;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.dom.DOMInput;
import com.googlecode.ddom.stream.dom.DOMOutput;

public class Parser2DOMOutputTest extends MatrixTestCase {
    private final XMLConformanceTest test;
    private final boolean flush;

    public Parser2DOMOutputTest(XMLConformanceTest test, boolean flush) {
        this.test = test;
        this.flush = flush;
        addTestParameter("id", test.getId());
        addTestParameter("flush", flush);
    }

    @Override
    protected void runTest() throws Throwable {
        DocumentBuilderFactory domFactory = new DocumentBuilderFactoryImpl();
        domFactory.setNamespaceAware(test.isUsingNamespaces());
        domFactory.setExpandEntityReferences(false);
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
        Document original = domBuilder.parse(test.getSystemId());
        Document expected = domBuilder.newDocument();
        // DOMOutput has some intrinsic limitations. To get comparable documents,
        // we need to pass the original/expected document through DOMOutput as well:
        new Stream(new DOMInput(original), new DOMOutput(expected)).flush();
        Document actual = domBuilder.newDocument();
        Parser parser = new Parser(test.getInputStream(), null, test.isUsingNamespaces());
        DOMOutput output = new DOMOutput(actual);
        if (flush) {
            new Stream(parser, output).flush();
        } else {
            EventCountingFilter filter = new EventCountingFilter();
            parser.addFilter(filter);
            Stream stream = new Stream(parser, output);
            while (!stream.isComplete()) {
                stream.proceed();
                filter.reset();
            }
        }
        XMLAssert.assertXMLIdentical(XMLUnit.compareXML(expected, actual), true);
    }
    
    public static TestSuite suite() {
        MatrixTestSuiteBuilder builder = new MatrixTestSuiteBuilder() {
            @Override
            protected void addTests() {
                for (XMLConformanceTest test : XMLConformanceTestSuite.load().getTests(new AndFilter<XMLConformanceTest>(Filters.DEFAULT, Filters.XERCES_2_9_1_FILTER))) {
                    addTest(new Parser2DOMOutputTest(test, false));
                    addTest(new Parser2DOMOutputTest(test, true));
                }
            }
        };
        
        // TODO
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-039)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-044)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-045)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-046)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-049)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-050)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-051)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-052)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-058)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-064)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-066)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-080)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-091)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-094)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-096)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-108)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-110)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-111)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-114)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-117)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-sa-118)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-001)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-002)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-003)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-004)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-005)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-006)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-007)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-008)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-009)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-010)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-011)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-012)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-013)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-014)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-015)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-016)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-017)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-018)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-019)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-020)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-021)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-023)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-024)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-025)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-026)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-027)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-028)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-029)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-030)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=valid-not-sa-031)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=pr-xml-little)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=pr-xml-utf-16)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=pr-xml-utf-8)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=weekly-little)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=weekly-utf-16)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=weekly-utf-8)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=pe01)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=not-sa01)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=not-sa02)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=not-sa03)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=not-sa04)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=notation01)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=optional)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=sa02)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=sa03)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=sa04)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=sa05)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-sgml01)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-lang01)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-lang02)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-lang03)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-lang04)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-lang05)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-lang06)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=v-pe00)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p01pass2)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p08pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p09pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p28pass4)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p28pass5)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p30pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p30pass2)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p31pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p31pass2)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p52pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p60pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p61pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p62pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p63pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=o-p64pass1)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P09-ibm09v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P09-ibm09v05.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P10-ibm10v05.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P10-ibm10v06.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P10-ibm10v07.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P10-ibm10v08.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P11-ibm11v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P11-ibm11v04.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P12-ibm12v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P12-ibm12v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P12-ibm12v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P12-ibm12v04.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P13-ibm13v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P20-ibm20v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P28-ibm28v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P30-ibm30v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P30-ibm30v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P31-ibm31v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P32-ibm32v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P32-ibm32v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P32-ibm32v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P32-ibm32v04.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P33-ibm33v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P34-ibm34v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P35-ibm35v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P36-ibm36v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P37-ibm37v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P38-ibm38v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P49-ibm49v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P50-ibm50v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P51-ibm51v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P61-ibm61v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P61-ibm61v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P62-ibm62v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P62-ibm62v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P62-ibm62v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P62-ibm62v04.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P62-ibm62v05.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P63-ibm63v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P63-ibm63v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P63-ibm63v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P63-ibm63v04.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P63-ibm63v05.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P64-ibm64v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P64-ibm64v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P64-ibm64v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P65-ibm65v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P65-ibm65v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P66-ibm66v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P67-ibm67v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P68-ibm68v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-valid-P69-ibm69v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P02-ibm02v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P02-ibm02v05.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v01.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v02.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v03.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v07.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v08.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v09.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v13.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v14.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v15.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v19.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v20.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v21.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v25.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v26.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=ibm-1-1-valid-P77-ibm77v27.xml)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-e2e-19)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-e2e-29)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-e2e-36)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-e2e-41)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-e2e-50)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-023)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-025)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-027)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-029)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-031)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-033)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-034)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-035)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-047)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-049)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-ns10-007)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-ns10-008)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-ns11-002)");
        builder.exclude(Parser2DOMOutputTest.class, "(id=rmt-ns11-006)");
        
        return builder.build();
    }
}
