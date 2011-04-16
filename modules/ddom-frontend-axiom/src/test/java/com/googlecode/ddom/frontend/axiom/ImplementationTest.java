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
package com.googlecode.ddom.frontend.axiom;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.ts.OMTestSuiteBuilder;
import org.apache.axiom.ts.om.builder.TestInvalidXML;
import org.apache.axiom.ts.om.container.TestSerialize;
import org.apache.axiom.ts.om.element.TestGetXMLStreamReaderCDATAEventFromElement;
import org.apache.axiom.ts.om.element.TestGetXMLStreamReaderCommentEvent;
import org.apache.axiom.ts.om.element.TestGetXMLStreamReaderNextTag;
import org.apache.axiom.ts.om.element.TestSerializationWithTwoNonBuiltOMElements;
import org.apache.axiom.ts.om.element.TestSetTextQName;
import org.apache.axiom.ts.om.factory.TestCreateOMTextFromDataHandlerProvider;
import org.apache.axiom.ts.om.node.TestInsertSiblingAfterLastChild;
import org.apache.axiom.ts.om.text.TestBase64Streaming;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() throws Exception {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(ImplementationTest.class.getClassLoader());
        Model model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("axiom"));
        OMTestSuiteBuilder builder = new OMTestSuiteBuilder((OMMetaFactory)model.getNodeFactory());
        // TODO
        builder.exclude(TestSetTextQName.class);
        builder.exclude(TestInsertSiblingAfterLastChild.class);
        builder.exclude(TestBase64Streaming.class);
//        builder.exclude(TestSerialization.class);
        builder.exclude(org.apache.axiom.ts.om.document.TestSerializeAndConsumeWithIncompleteDescendant.class);
        builder.exclude(org.apache.axiom.ts.om.element.TestSerializeAndConsumeWithIncompleteDescendant.class);
        builder.exclude(TestSerializationWithTwoNonBuiltOMElements.class);
        builder.exclude(org.apache.axiom.ts.om.document.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(org.apache.axiom.ts.om.element.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(TestCreateOMTextFromDataHandlerProvider.class);
        builder.exclude(TestGetXMLStreamReaderCDATAEventFromElement.class);
        builder.exclude(TestGetXMLStreamReaderCommentEvent.class);
        builder.exclude(TestGetXMLStreamReaderNextTag.class);
        builder.exclude(TestInvalidXML.class);
        builder.exclude(TestSerialize.class, "(&(file=spaces.xml)(container=document))");
        builder.exclude(TestSerialize.class, "(&(file=iso-8859-1.xml)(container=document))");
        builder.exclude("(&(file=spaces.xml))"); // No support for DTDs yet
        return builder.build();
    }
}
