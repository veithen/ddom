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
package com.googlecode.ddom.frontend.axiom;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.ts.om.OMTestSuiteBuilder;
import org.apache.axiom.ts.om.builder.TestCloseWithXMLStreamReader;
import org.apache.axiom.ts.om.builder.TestCreateOMBuilderFromDOM;
import org.apache.axiom.ts.om.builder.TestCreateOMBuilderFromSAXSource;
import org.apache.axiom.ts.om.builder.TestGetDocumentElement;
import org.apache.axiom.ts.om.builder.TestGetDocumentElementWithDiscardDocumentIllFormedEpilog;
import org.apache.axiom.ts.om.builder.TestGetDocumentElementWithIllFormedDocument;
import org.apache.axiom.ts.om.builder.TestIOExceptionInGetText;
import org.apache.axiom.ts.om.builder.TestInvalidXML;
import org.apache.axiom.ts.om.builder.TestNextBeforeGetDocumentElement;
import org.apache.axiom.ts.om.builder.TestReadAttachmentBeforeRootPartComplete;
import org.apache.axiom.ts.om.builder.TestRootPartStreaming;
import org.apache.axiom.ts.om.builder.TestStandaloneConfiguration;
import org.apache.axiom.ts.om.container.TestGetXMLStreamReader;
import org.apache.axiom.ts.om.container.TestSerialize;
import org.apache.axiom.ts.om.document.TestClone;
import org.apache.axiom.ts.om.document.TestDigest;
import org.apache.axiom.ts.om.document.TestGetSAXResult;
import org.apache.axiom.ts.om.document.TestRemoveChildren;
import org.apache.axiom.ts.om.document.sr.TestCharacterDataReaderFromParser;
import org.apache.axiom.ts.om.document.sr.TestCloseWithoutCaching;
import org.apache.axiom.ts.om.document.sr.TestDTDReader;
import org.apache.axiom.ts.om.document.sr.TestDTDReaderFromParser;
import org.apache.axiom.ts.om.element.TestAddChildDiscarded;
import org.apache.axiom.ts.om.element.TestBuildDiscarded;
import org.apache.axiom.ts.om.element.TestCloneOMElementNamespaceRepairing;
import org.apache.axiom.ts.om.element.TestDiscardDocumentElement;
import org.apache.axiom.ts.om.element.TestDiscardIncomplete;
import org.apache.axiom.ts.om.element.TestDiscardPartiallyBuilt;
import org.apache.axiom.ts.om.element.TestFindNamespaceURIWithPrefixUndeclaring;
import org.apache.axiom.ts.om.element.TestGetAllDeclaredNamespacesRemove;
import org.apache.axiom.ts.om.element.TestGetChildrenWithName4;
import org.apache.axiom.ts.om.element.TestGetFirstOMChildAfterDiscard;
import org.apache.axiom.ts.om.element.TestGetSAXResultWithDTD;
import org.apache.axiom.ts.om.element.TestGetSAXSourceIdentityTransform;
import org.apache.axiom.ts.om.element.TestGetSAXSourceIdentityTransformOnFragment;
import org.apache.axiom.ts.om.element.TestGetTextAsStreamWithSingleTextNode;
import org.apache.axiom.ts.om.element.TestGetTextAsStreamWithoutCaching;
import org.apache.axiom.ts.om.element.TestRemoveAttribute;
import org.apache.axiom.ts.om.element.TestRemoveAttributeNotOwner;
import org.apache.axiom.ts.om.element.sr.TestClose;
import org.apache.axiom.ts.om.element.sr.TestCloseAndContinueBuilding;
import org.apache.axiom.ts.om.element.sr.TestCommentEvent;
import org.apache.axiom.ts.om.element.sr.TestGetDataHandlerFromElement;
import org.apache.axiom.ts.om.factory.TestCreateOMDocTypeWithoutParent;
import org.apache.axiom.ts.om.factory.TestCreateOMEntityReference;
import org.apache.axiom.ts.om.factory.TestCreateOMEntityReferenceWithNullParent;
import org.apache.axiom.ts.om.factory.TestCreateOMTextFromDataHandlerProvider;
import org.apache.axiom.ts.om.factory.TestCreateOMTextWithNullParent;
import org.apache.axiom.ts.om.node.TestGetNextOMSiblingAfterDiscard;
import org.apache.axiom.ts.om.sourcedelement.TestByteArrayDS;
import org.apache.axiom.ts.om.sourcedelement.TestCharArrayDS;
import org.apache.axiom.ts.om.sourcedelement.TestCloneNonDestructive;
import org.apache.axiom.ts.om.sourcedelement.TestCloneUnknownName;
import org.apache.axiom.ts.om.sourcedelement.TestCloseOnComplete;
import org.apache.axiom.ts.om.sourcedelement.TestGetDocumentFromBuilder;
import org.apache.axiom.ts.om.sourcedelement.TestGetLocalName;
import org.apache.axiom.ts.om.sourcedelement.TestGetNamespace;
import org.apache.axiom.ts.om.sourcedelement.TestGetNamespaceURI;
import org.apache.axiom.ts.om.sourcedelement.TestGetNextOMSiblingIncomplete;
import org.apache.axiom.ts.om.sourcedelement.TestGetObject;
import org.apache.axiom.ts.om.sourcedelement.TestGetPrefix;
import org.apache.axiom.ts.om.sourcedelement.TestGetReaderException;
import org.apache.axiom.ts.om.sourcedelement.TestGetSAXSourceWithPushOMDataSource;
import org.apache.axiom.ts.om.sourcedelement.TestHasName;
import org.apache.axiom.ts.om.sourcedelement.TestInputStreamDS;
import org.apache.axiom.ts.om.sourcedelement.TestName1DefaultPrefix;
import org.apache.axiom.ts.om.sourcedelement.TestName1QualifiedPrefix;
import org.apache.axiom.ts.om.sourcedelement.TestPushOMDataSourceExpansion;
import org.apache.axiom.ts.om.sourcedelement.TestRemoveChildrenUnexpanded;
import org.apache.axiom.ts.om.sourcedelement.TestSetDataSourceOnAlreadyExpandedElement;
import org.apache.axiom.ts.om.text.TestBase64StreamingWithGetSAXSource;
import org.apache.axiom.ts.om.text.TestBase64StreamingWithSerialize;
import org.apache.axiom.ts.om.text.TestGetTextCharactersFromDataHandler;
import org.apache.axiom.ts.om.xop.XOPRoundtripTest;
import org.apache.axiom.ts.om.xpath.TestAXIOMXPath;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() throws Exception {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(ImplementationTest.class.getClassLoader());
        Model model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("axiom"));
        OMTestSuiteBuilder builder = new OMTestSuiteBuilder((OMMetaFactory)model.getNodeFactory(), true);
        
        // OMText#getNamespace() is deprecated; we don't support it
        builder.exclude(org.apache.axiom.ts.om.text.TestGetNamespace.class);
        builder.exclude(org.apache.axiom.ts.om.text.TestGetNamespaceNoNamespace.class);
        
        // TODO
        builder.exclude(TestBase64StreamingWithGetSAXSource.class);
        builder.exclude(TestBase64StreamingWithSerialize.class);
        builder.exclude(org.apache.axiom.ts.om.document.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(org.apache.axiom.ts.om.element.TestIsCompleteAfterAddingIncompleteChild.class);
        builder.exclude(TestCreateOMTextFromDataHandlerProvider.class);
        builder.exclude(TestCommentEvent.class);
        builder.exclude(TestInvalidXML.class);
        builder.exclude(TestSerialize.class, "(&(file=spaces.xml)(container=document))");
        builder.exclude(TestSerialize.class, "(&(file=iso-8859-1.xml)(container=document))");
        builder.exclude("(&(file=spaces.xml))"); // No support for DTDs yet
        builder.exclude(TestCreateOMBuilderFromDOM.class, "(file=iso-8859-1.xml)");
        builder.exclude(TestIOExceptionInGetText.class);
        builder.exclude(TestGetChildrenWithName4.class);
        builder.exclude(TestFindNamespaceURIWithPrefixUndeclaring.class);
        builder.exclude(TestStandaloneConfiguration.class);
        builder.exclude(TestGetAllDeclaredNamespacesRemove.class);
        builder.exclude(TestReadAttachmentBeforeRootPartComplete.class);
        builder.exclude(TestRootPartStreaming.class);
        builder.exclude(TestCloseWithXMLStreamReader.class);
        builder.exclude(TestNextBeforeGetDocumentElement.class);
        builder.exclude(TestRemoveChildren.class);
        builder.exclude(TestCharacterDataReaderFromParser.class);
        builder.exclude(TestDTDReader.class);
        builder.exclude(TestDTDReaderFromParser.class);
        builder.exclude(TestCloseWithoutCaching.class);
        builder.exclude(TestRemoveAttribute.class);
        builder.exclude(TestRemoveAttributeNotOwner.class);
        builder.exclude(org.apache.axiom.ts.om.element.TestRemoveChildren.class);
        builder.exclude(TestClose.class);
        builder.exclude(TestCloseAndContinueBuilding.class);
        builder.exclude(TestGetDataHandlerFromElement.class);
        builder.exclude(TestCreateOMTextWithNullParent.class);
        builder.exclude(TestCloseOnComplete.class);
        builder.exclude(TestGetDocumentFromBuilder.class);
        builder.exclude(TestGetNextOMSiblingIncomplete.class);
        builder.exclude(TestGetReaderException.class);
        builder.exclude(TestGetSAXSourceWithPushOMDataSource.class);
        builder.exclude(TestGetObject.class);
        builder.exclude(TestPushOMDataSourceExpansion.class);
        builder.exclude(org.apache.axiom.ts.om.sourcedelement.sr.TestCloseWithoutCaching.class);
        builder.exclude(org.apache.axiom.ts.om.xop.TestSerialize.class);
        builder.exclude(TestRemoveChildrenUnexpanded.class);
        builder.exclude(TestGetTextCharactersFromDataHandler.class);
        builder.exclude(XOPRoundtripTest.class);
        
        // TODO: caused by incorrect code in axiom-api
        builder.exclude(TestDigest.class, "(|(file=digest3.xml)(file=digest4.xml))");

        // TODO: problem with DTDs
        builder.exclude(TestAXIOMXPath.class, "(|(test=VariableLookup)(test=AttributeParent)(test=AttributeAsContext))");
        
        // TODO: recent changes in the Axiom API
        builder.exclude(TestGetDocumentElementWithIllFormedDocument.class);
        
        // TODO: issue in the parser
        builder.exclude(TestGetTextAsStreamWithoutCaching.class);
        
        // TODO: missing optimization
        builder.exclude(TestGetTextAsStreamWithSingleTextNode.class);
        
        // TODO: DDOM doesn't support updating the prefix of an OMSourcedElement on the fly (AXIOM-254)
        builder.exclude(TestName1DefaultPrefix.class);
        builder.exclude(TestName1QualifiedPrefix.class);
        
        // TODO: OMSourcedElement doesn't report correct state
        builder.exclude(org.apache.axiom.ts.om.sourcedelement.TestSerialize.class);
        builder.exclude(TestSetDataSourceOnAlreadyExpandedElement.class);
        builder.exclude(TestInputStreamDS.class);
        
        // TODO: OMSourcedElement with unknown name
        builder.exclude(TestGetLocalName.class, "(|(variant=qname-aware-source)(variant=unknown-name))");
        builder.exclude(TestGetNamespace.class, "(|(variant=qname-aware-source)(variant=unknown-name)(variant=lossy-prefix)(variant=unknown-prefix))");
        builder.exclude(TestGetPrefix.class, "(|(variant=qname-aware-source)(variant=unknown-name)(variant=lossy-prefix)(variant=unknown-prefix))");
        builder.exclude(TestGetNamespaceURI.class, "(|(variant=qname-aware-source)(variant=unknown-name))");
        builder.exclude(TestHasName.class, "(|(variant=qname-aware-source)(variant=unknown-name))");
        
        // TODO: we don't handle the discardDocument=true case correctly
        builder.exclude(TestGetDocumentElement.class, "(discardDocument=true)");
        builder.exclude(TestGetDocumentElementWithDiscardDocumentIllFormedEpilog.class);
        
        // TODO: we don't support entity references yet
        builder.exclude(TestCreateOMBuilderFromDOM.class, "(|(file=entity-reference-*.xml)(file=dtd.xml))");
        builder.exclude(TestCreateOMBuilderFromSAXSource.class, "(|(file=entity-reference-*.xml)(file=dtd.xml))");
        builder.exclude(TestGetXMLStreamReader.class, "(|(file=entity-reference-*.xml)(file=dtd.xml))");
        builder.exclude(TestSerialize.class, "(|(file=entity-reference-*.xml)(file=dtd.xml))");
        
        // TODO: implement discard
        builder.exclude(TestAddChildDiscarded.class);
        builder.exclude(TestBuildDiscarded.class);
        builder.exclude(TestDiscardDocumentElement.class);
        builder.exclude(TestDiscardIncomplete.class);
        builder.exclude(TestDiscardPartiallyBuilt.class);
        builder.exclude(TestGetFirstOMChildAfterDiscard.class);
        builder.exclude(TestGetNextOMSiblingAfterDiscard.class);
        
        // TODO: implement getSAXResult
        builder.exclude(TestGetSAXResult.class);
        builder.exclude(TestGetSAXResultWithDTD.class);
        builder.exclude(TestGetSAXSourceIdentityTransform.class);
        builder.exclude(TestGetSAXSourceIdentityTransformOnFragment.class);
        
        // TODO: implement OMDocType
        builder.exclude(TestCreateOMDocTypeWithoutParent.class);
        
        // TODO: implement OMEntityReference
        builder.exclude(TestCreateOMEntityReference.class);
        builder.exclude(TestCreateOMEntityReferenceWithNullParent.class);
        
        // TODO: clone
        builder.exclude(TestClone.class);
        builder.exclude(TestCloneOMElementNamespaceRepairing.class);
        builder.exclude(TestCloneNonDestructive.class);
        builder.exclude(TestCloneUnknownName.class);
        
        // TODO: the OMDataSource implementations rely on the default Axiom implementation
        builder.exclude(TestByteArrayDS.class);
        builder.exclude(TestCharArrayDS.class);
        
        return builder.build();
    }
}
