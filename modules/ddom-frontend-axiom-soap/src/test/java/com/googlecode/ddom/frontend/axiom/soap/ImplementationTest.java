/*
 * Copyright 2009-2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.soap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.ts.soap.SOAPTestSuiteBuilder;
import org.apache.axiom.ts.soap.builder.BadInputTest;
import org.apache.axiom.ts.soap.builder.TestDTD;
import org.apache.axiom.ts.soap.envelope.TestAddHeaderToIncompleteEnvelope;
import org.apache.axiom.ts.soap.envelope.TestBodyHeaderOrder;
import org.apache.axiom.ts.soap.envelope.TestClone;
import org.apache.axiom.ts.soap.envelope.TestCloneWithSourcedElement1;
import org.apache.axiom.ts.soap.envelope.TestCloneWithSourcedElement2;
import org.apache.axiom.ts.soap.envelope.TestGetSOAPBodyFirstElementLocalNameAndNSWithParser;
import org.apache.axiom.ts.soap.envelope.TestHasFaultWithParserOptimized;
import org.apache.axiom.ts.soap.envelope.TestSerializeAndConsumeWithOMSEInBody;
import org.apache.axiom.ts.soap.factory.TestCreateSOAPFaultWithParent;
import org.apache.axiom.ts.soap.fault.TestSetCode;
import org.apache.axiom.ts.soap.fault.TestSetDetail;
import org.apache.axiom.ts.soap.fault.TestSetReason;
import org.apache.axiom.ts.soap.fault.TestSetRole;
import org.apache.axiom.ts.soap.fault.TestWrongParent1;
import org.apache.axiom.ts.soap.fault.TestWrongParent2;
import org.apache.axiom.ts.soap.fault.TestWrongParent3;
import org.apache.axiom.ts.soap.faultdetail.TestGetAllDetailEntriesWithParser;
import org.apache.axiom.ts.soap.faulttext.TestSetLang;
import org.apache.axiom.ts.soap.header.TestDiscardIncomplete;
import org.apache.axiom.ts.soap.header.TestDiscardPartiallyBuilt;
import org.apache.axiom.ts.soap.headerblock.TestByteArrayDS;
import org.apache.axiom.ts.soap.message.TestCloneIncomplete;
import org.apache.axiom.ts.soap.message.TestGetCharsetEncodingWithParser;
import org.apache.axiom.ts.soap.message.TestGetOMFactoryWithParser;
import org.apache.axiom.ts.soap.message.TestSetOMDocumentElementNonSOAPEnvelope;
import org.apache.axiom.ts.soap11.misc.TestElementPullStreamAndOMExpansion;
import org.apache.axiom.ts.soap11.misc.TestElementPullStreamAndOMExpansion2;
import org.apache.axiom.ts.soap11.misc.TestElementPullStreamAndOMExpansion3;
import org.apache.axiom.ts.soap12.envelope.TestAddElementAfterBody;
import org.apache.axiom.ts.soap12.envelope.TestBuildWithAttachments;
import org.apache.axiom.ts.soap12.envelope.TestMTOMForwardStreaming;
import org.apache.axiom.ts.soap12.fault.TestGetNode;
import org.apache.axiom.ts.soap12.fault.TestMoreChildrenAddition;
import org.apache.axiom.ts.soap12.fault.TestSetNode;
import org.apache.axiom.ts.soap12.mtom.TestGetXMLStreamReaderMTOMEncoded;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() throws Exception {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(ImplementationTest.class.getClassLoader());
        Model model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("axiom-soap"));
        SOAPTestSuiteBuilder builder = new SOAPTestSuiteBuilder((OMMetaFactory)model.getNodeFactory(), true, false);

        // We don't support this type of optimization
        builder.exclude(TestHasFaultWithParserOptimized.class);
        
        // TODO
        builder.exclude(TestAddHeaderToIncompleteEnvelope.class);
        builder.exclude(TestDiscardIncomplete.class);
        builder.exclude(TestDiscardPartiallyBuilt.class);
        builder.exclude(TestGetAllDetailEntriesWithParser.class);
        builder.exclude(TestClone.class);
        builder.exclude(TestCloneWithSourcedElement1.class);
        builder.exclude(TestCloneWithSourcedElement2.class);
        builder.exclude(org.apache.axiom.ts.soap.message.TestClone.class);
        builder.exclude(TestCloneIncomplete.class);
        builder.exclude(TestElementPullStreamAndOMExpansion.class);
        builder.exclude(TestElementPullStreamAndOMExpansion2.class);
        builder.exclude(TestElementPullStreamAndOMExpansion3.class);
        builder.exclude(TestDTD.class);
        builder.exclude(TestSerializeAndConsumeWithOMSEInBody.class);
        builder.exclude(TestCreateSOAPFaultWithParent.class);
        builder.exclude(TestWrongParent1.class);
        builder.exclude(TestWrongParent2.class);
        builder.exclude(TestWrongParent3.class);
        builder.exclude(org.apache.axiom.ts.soap.headerblock.TestWrongParent1.class);
        builder.exclude(org.apache.axiom.ts.soap.headerblock.TestWrongParent2.class);
        builder.exclude(org.apache.axiom.ts.soap.headerblock.TestWrongParent3.class);
        builder.exclude(TestSetOMDocumentElementNonSOAPEnvelope.class);
        builder.exclude(TestByteArrayDS.class);
        builder.exclude(TestGetCharsetEncodingWithParser.class);
        builder.exclude(TestGetOMFactoryWithParser.class);
        
        // TODO: this requires some more thinking
        builder.exclude(TestBodyHeaderOrder.class);
        builder.exclude(TestAddElementAfterBody.class);
        
        // TODO
        builder.exclude(TestGetNode.class);
        builder.exclude(TestSetNode.class);
        builder.exclude(TestMoreChildrenAddition.class);
        
        // TODO: split this test case up into two parts
        builder.exclude(TestSetDetail.class);
        builder.exclude(TestSetCode.class);
        builder.exclude(TestSetReason.class);
        builder.exclude(TestSetRole.class);
        
        // TODO: fault text is a SOAP 1.2 concept
        builder.exclude(org.apache.axiom.ts.soap11.faultreason.TestGetFirstSOAPText.class);
        builder.exclude(TestSetLang.class, "(spec=soap11)");
        
        // TODO
        builder.exclude(org.apache.axiom.ts.soap11.header.TestExamineMustUnderstandHeaderBlocksWithParser.class);
        builder.exclude(org.apache.axiom.ts.soap12.header.TestExamineMustUnderstandHeaderBlocks.class);
        builder.exclude(org.apache.axiom.ts.soap12.header.TestExamineMustUnderstandHeaderBlocksWithParser.class);
        builder.exclude(org.apache.axiom.ts.soap.header.TestExtractAllHeaderBlocks.class);
        
        // TODO: no MTOM support yet
        builder.exclude(TestBuildWithAttachments.class);
        builder.exclude(TestMTOMForwardStreaming.class);
        builder.exclude(TestGetXMLStreamReaderMTOMEncoded.class);
        
        builder.exclude(BadInputTest.class);
        
        // TODO: this fails because the default Axiom implementations skip serialization of empty SOAP headers
        builder.exclude(TestGetSOAPBodyFirstElementLocalNameAndNSWithParser.class);
        
        return builder.build();
    }
}
