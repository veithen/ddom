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
package com.google.code.ddom.frontend.axiom.testsuite;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.ts.AxiomTestSuiteBuilder;
import org.apache.axiom.ts.om.document.TestGetXMLStreamReader;
import org.apache.axiom.ts.om.element.TestSerialization;
import org.apache.axiom.ts.om.element.TestSerializationWithTwoNonBuiltOMElements;
import org.apache.axiom.ts.om.element.TestSetTextQName;
import org.apache.axiom.ts.om.factory.TestCreateOMTextFromDataHandlerProvider;
import org.apache.axiom.ts.om.node.TestInsertSiblingAfterLastChild;
import org.apache.axiom.ts.om.text.TestBase64Streaming;

import com.google.code.ddom.frontend.axiom.DDOMAxiomUtil;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() {
        AxiomTestSuiteBuilder builder = new AxiomTestSuiteBuilder(DDOMAxiomUtil.INSTANCE.getMetaFactory());
        // TODO
        builder.exclude(TestSetTextQName.class);
        builder.exclude(TestInsertSiblingAfterLastChild.class);
        builder.exclude(TestBase64Streaming.class);
        builder.exclude(TestSerialization.class);
        builder.exclude(org.apache.axiom.ts.om.document.TestSerializeAndConsumeWithIncompleteDescendant.class);
        builder.exclude(org.apache.axiom.ts.om.element.TestSerializeAndConsumeWithIncompleteDescendant.class);
        builder.exclude(TestSerializationWithTwoNonBuiltOMElements.class);
        builder.exclude(TestGetXMLStreamReader.class);
        builder.exclude(org.apache.axiom.ts.om.document.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(org.apache.axiom.ts.om.element.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(TestCreateOMTextFromDataHandlerProvider.class);
        return builder.build();
    }
}
