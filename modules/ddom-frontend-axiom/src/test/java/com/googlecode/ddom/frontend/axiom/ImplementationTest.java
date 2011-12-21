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
import org.apache.axiom.ts.om.builder.TestCreateOMBuilderFromDOMSource;
import org.apache.axiom.ts.om.builder.TestGetDocumentElementWithIllFormedDocument;
import org.apache.axiom.ts.om.builder.TestIOExceptionInGetText;
import org.apache.axiom.ts.om.builder.TestInvalidXML;
import org.apache.axiom.ts.om.builder.TestReadAttachmentBeforeRootPartComplete;
import org.apache.axiom.ts.om.builder.TestRootPartStreaming;
import org.apache.axiom.ts.om.builder.TestStandaloneConfiguration;
import org.apache.axiom.ts.om.container.TestSerialize;
import org.apache.axiom.ts.om.document.TestDigest;
import org.apache.axiom.ts.om.element.TestFindNamespaceURIWithPrefixUndeclaring;
import org.apache.axiom.ts.om.element.TestGetAllDeclaredNamespacesRemove;
import org.apache.axiom.ts.om.element.TestGetChildrenWithName4;
import org.apache.axiom.ts.om.element.TestGetTextAsStreamWithSingleTextNode;
import org.apache.axiom.ts.om.element.TestGetTextAsStreamWithoutCaching;
import org.apache.axiom.ts.om.element.TestGetXMLStreamReaderCommentEvent;
import org.apache.axiom.ts.om.element.TestGetXMLStreamReaderNextTag;
import org.apache.axiom.ts.om.element.TestGetXMLStreamReaderWithPreserveNamespaceContext;
import org.apache.axiom.ts.om.factory.TestCreateOMTextFromDataHandlerProvider;
import org.apache.axiom.ts.om.sourcedelement.TestName1DefaultPrefix;
import org.apache.axiom.ts.om.sourcedelement.TestName1QualifiedPrefix;
import org.apache.axiom.ts.om.text.TestBase64Streaming;
import org.apache.axiom.ts.xpath.TestAXIOMXPath;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() throws Exception {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(ImplementationTest.class.getClassLoader());
        Model model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("axiom"));
        OMTestSuiteBuilder builder = new OMTestSuiteBuilder((OMMetaFactory)model.getNodeFactory(), true);
        // TODO
        builder.exclude(TestBase64Streaming.class);
        builder.exclude(org.apache.axiom.ts.om.document.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(org.apache.axiom.ts.om.element.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(TestCreateOMTextFromDataHandlerProvider.class);
        builder.exclude(TestGetXMLStreamReaderCommentEvent.class);
        builder.exclude(TestGetXMLStreamReaderNextTag.class);
        builder.exclude(TestInvalidXML.class);
        builder.exclude(TestSerialize.class, "(&(file=spaces.xml)(container=document))");
        builder.exclude(TestSerialize.class, "(&(file=iso-8859-1.xml)(container=document))");
        builder.exclude("(&(file=spaces.xml))"); // No support for DTDs yet
        builder.exclude(TestCreateOMBuilderFromDOMSource.class, "(file=iso-8859-1.xml)");
        builder.exclude(TestIOExceptionInGetText.class);
        builder.exclude(TestGetChildrenWithName4.class);
        builder.exclude(TestFindNamespaceURIWithPrefixUndeclaring.class);
        builder.exclude(TestStandaloneConfiguration.class);
        builder.exclude(TestGetAllDeclaredNamespacesRemove.class);
        builder.exclude(TestReadAttachmentBeforeRootPartComplete.class);
        builder.exclude(TestRootPartStreaming.class);
        
        // TODO: caused by incorrect code in axiom-api
        builder.exclude(TestDigest.class, "(|(file=digest3.xml)(file=digest4.xml))");

        // TODO: problem with DTDs
        builder.exclude(TestAXIOMXPath.class, "(|(test=VariableLookup)(test=AttributeParent)(test=AttributeAsContext))");
        
        // TODO: recent changes in the Axiom API
        builder.exclude(TestGetDocumentElementWithIllFormedDocument.class);
        builder.exclude(TestGetXMLStreamReaderWithPreserveNamespaceContext.class);
        
        // TODO: issue in the parser
        builder.exclude(TestGetTextAsStreamWithoutCaching.class);
        
        // TODO: missing optimization
        builder.exclude(TestGetTextAsStreamWithSingleTextNode.class);
        
        // TODO: DDOM doesn't support updating the prefix of an OMSourcedElement on the fly (AXIOM-254)
        builder.exclude(TestName1DefaultPrefix.class);
        builder.exclude(TestName1QualifiedPrefix.class);
        
        return builder.build();
    }
}
