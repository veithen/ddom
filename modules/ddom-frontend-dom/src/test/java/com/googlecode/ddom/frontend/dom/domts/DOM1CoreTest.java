/*
 * Copyright 2009 Andreas Veithen
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
package com.googlecode.ddom.frontend.dom.domts;

import junit.framework.TestSuite;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.JUnitTestSuiteAdapter;
import org.w3c.domts.level1.core.alltests;
import org.w3c.domts.level1.core.attrremovechild1;
import org.w3c.domts.level1.core.attrreplacechild1;
import org.w3c.domts.level1.core.attrspecifiedvalueremove;
import org.w3c.domts.level1.core.documentcreateelementdefaultattr;
import org.w3c.domts.level1.core.elementassociatedattribute;
import org.w3c.domts.level1.core.elementremoveattribute;
import org.w3c.domts.level1.core.elementremoveattributerestoredefaultvalue;
import org.w3c.domts.level1.core.hc_attrgetvalue2;
import org.w3c.domts.level1.core.hc_attrnormalize;
import org.w3c.domts.level1.core.hc_elementnormalize2;
import org.w3c.domts.level1.core.hc_nodeappendchildchildexists;
import org.w3c.domts.level1.core.hc_nodereplacechildnewchildexists;
import org.w3c.domts.level1.core.hc_nodevalue03;
import org.w3c.domts.level1.core.hc_nodevalue07;
import org.w3c.domts.level1.core.hc_nodevalue08;
import org.w3c.domts.level1.core.namednodemapremovenameditem;
import org.w3c.domts.level1.core.namednodemapremovenameditemgetvalue;
import org.w3c.domts.level1.core.nodeappendchildchildexists;
import org.w3c.domts.level1.core.nodevalue03;
import org.w3c.domts.level1.core.nodevalue07;
import org.w3c.domts.level1.core.nodevalue08;

import com.google.code.ddom.domts.FilteredDOMTestSuite;

public class DOM1CoreTest extends TestSuite {
    public static TestSuite suite() throws Exception {
        DOMTestDocumentBuilderFactory factory = Factories.getFactory();
        FilteredDOMTestSuite suite = new FilteredDOMTestSuite(factory, new alltests(factory));
        
        // Tests accessing DTD information
        suite.addExclude("documenttypegetentities.*");
        suite.addExclude("documenttypegetnotations.*");
        suite.addExclude("entity.*");
        suite.addExclude("hc_entities.*");
        suite.addExclude(hc_nodevalue07.class);
        suite.addExclude(hc_nodevalue08.class);
        suite.addExclude("hc_notations.*");
        suite.addExclude("nodeentity.*");
        suite.addExclude("nodenotation.*");
        suite.addExclude(nodevalue07.class);
        suite.addExclude(nodevalue08.class);
        suite.addExclude("notation.*");
        
        // These tests checks that default attributes appear automatically as specified in a DTD.
        suite.addExclude(attrspecifiedvalueremove.class);
        suite.addExclude(documentcreateelementdefaultattr.class);
        suite.addExclude(elementremoveattribute.class);
        suite.addExclude(elementremoveattributerestoredefaultvalue.class);
        suite.addExclude(namednodemapremovenameditem.class);
        suite.addExclude(namednodemapremovenameditemgetvalue.class);
        
        // Tests relying on Attr#getSpecified
        suite.addExclude("attr(not)?specifiedvalue.*");
        suite.addExclude(elementassociatedattribute.class);
        
        // Tests relying on entity references
        suite.addExclude(".*nomodificationallowederr.*");
        suite.addExclude(".*createentityreference.*");
        suite.addExclude(".*createentref.*");
        suite.addExclude(attrremovechild1.class);
        suite.addExclude(attrreplacechild1.class);
        suite.addExclude(hc_attrgetvalue2.class);
        suite.addExclude(hc_nodevalue03.class);
        suite.addExclude(nodevalue03.class);
        
        // TODO
        suite.addExclude(hc_nodeappendchildchildexists.class);
        suite.addExclude(hc_nodereplacechildnewchildexists.class);
        
        // TODO Will implement normalization later
        suite.addExclude(hc_elementnormalize2.class);
        suite.addExclude(hc_attrnormalize.class);
        
        // TODO: needs to be investigated
        suite.addExclude(nodeappendchildchildexists.class);
        
        return new JUnitTestSuiteAdapter(suite);
    }
}
