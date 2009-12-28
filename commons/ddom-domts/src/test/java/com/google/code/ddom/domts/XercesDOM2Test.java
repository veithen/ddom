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
package com.google.code.ddom.domts;

import junit.framework.TestSuite;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.JAXPDOMTestDocumentBuilderFactory;
import org.w3c.domts.JUnitTestSuiteAdapter;
import org.w3c.domts.level2.core.alltests;
import org.w3c.domts.level2.core.createAttributeNS06;
import org.w3c.domts.level2.core.createDocument08;
import org.w3c.domts.level2.core.createDocumentType04;
import org.w3c.domts.level2.core.getNamedItemNS03;
import org.w3c.domts.level2.core.getNamedItemNS04;
import org.w3c.domts.level2.core.namednodemapgetnameditemns01;
import org.w3c.domts.level2.core.setAttributeNS10;

public class XercesDOM2Test extends TestSuite {
    public static TestSuite suite() throws Exception {
        DOMTestDocumentBuilderFactory factory = new JAXPDOMTestDocumentBuilderFactory(
                new DocumentBuilderFactoryImpl(),
                JAXPDOMTestDocumentBuilderFactory.getConfiguration1());

        FilteredDOMTestSuite suite = new FilteredDOMTestSuite(factory, new alltests(factory));
        suite.addExclude(createAttributeNS06.class);
        suite.addExclude(createDocument08.class);
        suite.addExclude(createDocumentType04.class);
        suite.addExclude(getNamedItemNS03.class);
        suite.addExclude(getNamedItemNS04.class);
        suite.addExclude(namednodemapgetnameditemns01.class);
        suite.addExclude(setAttributeNS10.class);
        return new JUnitTestSuiteAdapter(suite);
    }
}
