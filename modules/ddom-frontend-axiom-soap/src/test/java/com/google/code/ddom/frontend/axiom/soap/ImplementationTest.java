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
package com.google.code.ddom.frontend.axiom.soap;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.ts.SOAPTestSuiteBuilder;
import org.apache.axiom.ts.soap.envelope.TestAddHeaderToIncompleteEnvelope;
import org.apache.axiom.ts.soap.envelope.TestBodyHeaderOrder;
import org.apache.axiom.ts.soap.envelope.TestDiscardHeader;
import org.apache.axiom.ts.soap.factory.TestGetDefaultFaultEnvelope;
import org.apache.axiom.ts.soap.fault.TestGetDetailWithParser;
import org.apache.axiom.ts.soap.fault.TestGetRoleWithParser;
import org.apache.axiom.ts.soap.fault.TestSetCode;
import org.apache.axiom.ts.soap.fault.TestSetDetail;
import org.apache.axiom.ts.soap.fault.TestSetReason;
import org.apache.axiom.ts.soap.fault.TestSetRole;
import org.apache.axiom.ts.soap.faultdetail.TestGetAllDetailEntriesWithParser;
import org.apache.axiom.ts.soap12.envelope.TestAddElementAfterBody;
import org.apache.axiom.ts.soap12.fault.TestGetNode;
import org.apache.axiom.ts.soap12.fault.TestGetNodeWithParser;
import org.apache.axiom.ts.soap12.fault.TestMoreChildrenAddition;
import org.apache.axiom.ts.soap12.fault.TestSetNode;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.model.ModelDefinitionBuilder;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() {
        DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance();
        OMMetaFactory metaFactory = documentHelper.getAPIObject(ModelDefinitionBuilder.buildModelDefinition("axiom-soap"), OMMetaFactory.class);
        SOAPTestSuiteBuilder builder = new SOAPTestSuiteBuilder(metaFactory);

        // TODO
        builder.exclude(TestAddHeaderToIncompleteEnvelope.class);
        builder.exclude(TestDiscardHeader.class);
        builder.exclude(TestGetAllDetailEntriesWithParser.class);
        builder.exclude(TestGetDetailWithParser.class);
        builder.exclude(TestGetRoleWithParser.class);
        builder.exclude(TestGetNodeWithParser.class);
        
        // TODO: this requires some more thinking
        builder.exclude(TestBodyHeaderOrder.class);
        builder.exclude(TestAddElementAfterBody.class);
        
        // TODO
        builder.exclude(TestGetDefaultFaultEnvelope.class);
        builder.exclude(TestGetNode.class);
        builder.exclude(TestSetNode.class);
        builder.exclude(TestMoreChildrenAddition.class);
        
        // TODO: split this test case up into two parts
        builder.exclude(TestSetDetail.class);
        builder.exclude(TestSetCode.class);
        builder.exclude(TestSetReason.class);
        builder.exclude(TestSetRole.class);
        
        return builder.build();
    }
}
