package com.google.code.ddom.dom.impl.domts;

import junit.framework.TestSuite;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.JUnitTestSuiteAdapter;
import org.w3c.domts.level2.core.alltests;
import org.w3c.domts.level2.core.documentimportnode14;
import org.w3c.domts.level2.core.documentimportnode19;
import org.w3c.domts.level2.core.documentimportnode20;
import org.w3c.domts.level2.core.documentimportnode21;
import org.w3c.domts.level2.core.documentimportnode22;
import org.w3c.domts.level2.core.documenttypeinternalSubset01;
import org.w3c.domts.level2.core.elementsetattributenodens06;
import org.w3c.domts.level2.core.getNamedItemNS03;
import org.w3c.domts.level2.core.getNamedItemNS04;
import org.w3c.domts.level2.core.importNode07;
import org.w3c.domts.level2.core.internalSubset01;
import org.w3c.domts.level2.core.namednodemapgetnameditemns01;
import org.w3c.domts.level2.core.namednodemapremovenameditemns02;
import org.w3c.domts.level2.core.namednodemapremovenameditemns05;
import org.w3c.domts.level2.core.nodenormalize01;
import org.w3c.domts.level2.core.prefix08;
import org.w3c.domts.level2.core.removeAttributeNS01;
import org.w3c.domts.level2.core.removeAttributeNS02;
import org.w3c.domts.level2.core.removeNamedItemNS03;
import org.w3c.domts.level2.core.setAttributeNS03;
import org.w3c.domts.level2.core.setAttributeNodeNS02;
import org.w3c.domts.level2.core.setNamedItemNS04;

public class DOM2CoreTest extends TestSuite {
    public static TestSuite suite() throws Exception {
        DOMTestDocumentBuilderFactory factory = Factories.getFactory();
        FilteredDOMTestSuite suite = new FilteredDOMTestSuite(factory, new alltests(factory));
        
        // Tests accessing DTD information
        suite.addExclude(documentimportnode19.class);
        suite.addExclude(documentimportnode22.class);
        suite.addExclude(getNamedItemNS03.class);
        suite.addExclude(getNamedItemNS04.class);
        suite.addExclude("hc_entities.*");
        suite.addExclude("hc_notations.*");
        suite.addExclude("importNode(09|12|13)");
        suite.addExclude(namednodemapgetnameditemns01.class);
        suite.addExclude(namednodemapremovenameditemns05.class);
        suite.addExclude("namednodemapsetnameditemns(05|09|10|11)");
        
        // These tests checks that default attributes appear automatically as specified in a DTD.
        suite.addExclude(documentimportnode14.class);
        suite.addExclude(namednodemapremovenameditemns02.class);
        suite.addExclude(removeAttributeNS02.class);
        suite.addExclude(importNode07.class);
        
        suite.addExclude(documentimportnode20.class); // uses entities
        suite.addExclude(documentimportnode21.class); // entity references
        suite.addExclude(documenttypeinternalSubset01.class);
        suite.addExclude(elementsetattributenodens06.class); // entity references
        suite.addExclude("hc_nodedocumentfragmentnormalize.*");
        suite.addExclude("importNode(10|11)"); // entity references
        suite.addExclude(internalSubset01.class);
        suite.addExclude(nodenormalize01.class);
        suite.addExclude(prefix08.class); // entity references
        suite.addExclude(removeAttributeNS01.class); // entity references
        suite.addExclude(removeNamedItemNS03.class); // entity references
        suite.addExclude(setAttributeNS03.class); // entity references
        suite.addExclude(setAttributeNodeNS02.class); // entity references
        suite.addExclude(setNamedItemNS04.class); // entity references
        
        return new JUnitTestSuiteAdapter(suite);
    }
}
