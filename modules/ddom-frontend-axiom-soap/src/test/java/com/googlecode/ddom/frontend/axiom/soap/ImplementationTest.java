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
package com.googlecode.ddom.frontend.axiom.soap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.ts.SOAPTestSuiteBuilder;
import org.apache.axiom.ts.soap.envelope.TestAddHeaderToIncompleteEnvelope;
import org.apache.axiom.ts.soap.envelope.TestBodyHeaderOrder;
import org.apache.axiom.ts.soap.envelope.TestDiscardHeader;
import org.apache.axiom.ts.soap.fault.TestSetCode;
import org.apache.axiom.ts.soap.fault.TestSetDetail;
import org.apache.axiom.ts.soap.fault.TestSetReason;
import org.apache.axiom.ts.soap.fault.TestSetRole;
import org.apache.axiom.ts.soap.faultdetail.TestGetAllDetailEntriesWithParser;
import org.apache.axiom.ts.soap.faulttext.TestSetLang;
import org.apache.axiom.ts.soap12.envelope.TestAddElementAfterBody;
import org.apache.axiom.ts.soap12.envelope.TestBuildWithAttachments;
import org.apache.axiom.ts.soap12.envelope.TestMTOMForwardStreaming;
import org.apache.axiom.ts.soap12.fault.TestGetNode;
import org.apache.axiom.ts.soap12.fault.TestMoreChildrenAddition;
import org.apache.axiom.ts.soap12.fault.TestSetNode;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() throws Exception {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(ImplementationTest.class.getClassLoader());
        Model model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("axiom-soap"));
        SOAPTestSuiteBuilder builder = new SOAPTestSuiteBuilder((OMMetaFactory)model.getNodeFactory());

        // TODO
        builder.exclude(TestAddHeaderToIncompleteEnvelope.class);
        builder.exclude(TestDiscardHeader.class);
        builder.exclude(TestGetAllDetailEntriesWithParser.class);
        
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
        builder.exclude(org.apache.axiom.ts.soap11.faultreason.TestAddSOAPText.class);
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
        
        return builder.build();
    }
}
