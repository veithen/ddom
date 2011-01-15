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
import org.apache.axiom.ts.soap.body.TestGetFaultWithParser;
import org.apache.axiom.ts.soap.body.TestHasFaultWithParser;
import org.apache.axiom.ts.soap.envelope.TestAddHeaderToIncompleteEnvelope;
import org.apache.axiom.ts.soap.envelope.TestBodyHeaderOrder;
import org.apache.axiom.ts.soap.envelope.TestDiscardHeader;
import org.apache.axiom.ts.soap.envelope.TestGetBodyWithParser;
import org.apache.axiom.ts.soap.envelope.TestGetHeaderWithParser;
import org.apache.axiom.ts.soap.faultdetail.TestGetAllDetailEntriesWithParser;
import org.apache.axiom.ts.soap12.envelope.TestAddElementAfterBody;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.model.ModelDefinitionBuilder;

public class ImplementationTest extends TestCase {
    public static TestSuite suite() {
        DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance();
        OMMetaFactory metaFactory = documentHelper.getAPIObject(ModelDefinitionBuilder.buildModelDefinition("axiom-soap"), OMMetaFactory.class);
        SOAPTestSuiteBuilder builder = new SOAPTestSuiteBuilder(metaFactory);
        
        // TODO: Axiom test suite doesn't use OMXMLBuilderFactory here
        builder.exclude(TestGetFaultWithParser.class);
        builder.exclude(TestHasFaultWithParser.class);
        builder.exclude(TestAddHeaderToIncompleteEnvelope.class);
        builder.exclude(TestDiscardHeader.class);
        builder.exclude(TestGetBodyWithParser.class);
        builder.exclude(TestGetHeaderWithParser.class);
        builder.exclude(TestGetAllDetailEntriesWithParser.class);
        
        // TODO: this requires some more thinking
        builder.exclude(TestBodyHeaderOrder.class);
        builder.exclude(TestAddElementAfterBody.class);
        
        return builder.build();
    }
}
