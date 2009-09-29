package com.google.code.ddom.dom.impl.domts;

import junit.framework.TestSuite;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.JUnitTestSuiteAdapter;
import org.w3c.domts.level1.core.alltests;
import org.w3c.domts.level1.core.attrremovechild1;
import org.w3c.domts.level1.core.attrreplacechild1;
import org.w3c.domts.level1.core.attrspecifiedvalueremove;
import org.w3c.domts.level1.core.cdatasectionnormalize;
import org.w3c.domts.level1.core.documentcreateelementdefaultattr;
import org.w3c.domts.level1.core.elementassociatedattribute;
import org.w3c.domts.level1.core.elementremoveattribute;
import org.w3c.domts.level1.core.elementremoveattributerestoredefaultvalue;
import org.w3c.domts.level1.core.hc_attrgetvalue2;
import org.w3c.domts.level1.core.hc_attrnormalize;
import org.w3c.domts.level1.core.hc_nodeappendchildchildexists;
import org.w3c.domts.level1.core.hc_nodeinsertbeforenewchildexists;
import org.w3c.domts.level1.core.hc_nodereplacechildnewchildexists;
import org.w3c.domts.level1.core.hc_nodevalue03;
import org.w3c.domts.level1.core.hc_nodevalue07;
import org.w3c.domts.level1.core.hc_nodevalue08;
import org.w3c.domts.level1.core.namednodemapremovenameditem;
import org.w3c.domts.level1.core.namednodemapremovenameditemgetvalue;
import org.w3c.domts.level1.core.nodeappendchildchildexists;
import org.w3c.domts.level1.core.nodeinsertbeforenewchildexists;
import org.w3c.domts.level1.core.nodereplacechildnewchildexists;
import org.w3c.domts.level1.core.nodevalue03;
import org.w3c.domts.level1.core.nodevalue07;
import org.w3c.domts.level1.core.nodevalue08;

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
        
        // TODO: bugs in Axiom!
        suite.addExclude(hc_nodeappendchildchildexists.class);
        suite.addExclude(hc_nodeinsertbeforenewchildexists.class);
        suite.addExclude(hc_nodereplacechildnewchildexists.class);
        suite.addExclude(nodeappendchildchildexists.class);
        suite.addExclude(nodeinsertbeforenewchildexists.class);
        suite.addExclude(nodereplacechildnewchildexists.class);
        
        // TODO Incorrect test cases?
        suite.addExclude(cdatasectionnormalize.class);
        
        // TODO Will implement normalization later
        suite.addExclude("hc_elementnormalize.*");
        suite.addExclude(hc_attrnormalize.class);
        
        return new JUnitTestSuiteAdapter(suite);
    }
}
