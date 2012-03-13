/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.jaxp;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.ts.dom.DOMTestSuiteBuilder;
import org.apache.axiom.ts.dom.document.TestAllowedChildren;
import org.apache.axiom.ts.dom.document.TestNormalizeDocumentNamespace;
import org.apache.axiom.ts.dom.element.TestGetElementsByTagName;
import org.apache.axiom.ts.dom.element.TestGetTextContent;
import org.apache.axiom.ts.dom.text.TestSetPrefix;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() {
        DocumentBuilderFactory dbf = new DocumentBuilderFactoryImpl();
        dbf.setNamespaceAware(true);
        // TODO: in Axiom we should make a clear distinction between DOM and OMDOM test cases
        DOMTestSuiteBuilder builder = new DOMTestSuiteBuilder(dbf, false);
        
        // TODO
        builder.exclude(TestAllowedChildren.class);
        builder.exclude(TestNormalizeDocumentNamespace.class);
        builder.exclude(TestGetElementsByTagName.class);
        builder.exclude(TestGetTextContent.class);
        builder.exclude(TestSetPrefix.class);
        
        return builder.build();
    }
}
