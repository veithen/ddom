/*
 * Copyright 2009-2012,2014 Andreas Veithen
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

import org.apache.axiom.om.dom.DOMMetaFactory;
import org.apache.axiom.ts.omdom.OMDOMTestSuiteBuilder;
import org.apache.axiom.ts.omdom.text.TestCloneNodeBinary;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;

public class OMDOMImplementationTest extends TestCase {
    public static TestSuite suite() throws Exception {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(ImplementationTest.class.getClassLoader());
        ModelDefinitionBuilder mdb = new ModelDefinitionBuilder();
        mdb.addFrontend("axiom");
        mdb.addFrontend("dom");
        Model model = modelRegistry.getModel(mdb.buildModelDefinition());
        OMDOMTestSuiteBuilder builder = new OMDOMTestSuiteBuilder((DOMMetaFactory)model.getNodeFactory());
        
        // TODO
        builder.exclude(TestCloneNodeBinary.class);
        
        return builder.build();
    }
}
