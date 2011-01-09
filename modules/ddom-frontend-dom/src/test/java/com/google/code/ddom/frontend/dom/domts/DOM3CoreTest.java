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
package com.google.code.ddom.frontend.dom.domts;

import junit.framework.TestSuite;

import org.w3c.domts.DOMTestDocumentBuilderFactory;
import org.w3c.domts.JUnitTestSuiteAdapter;
import org.w3c.domts.level3.core.alltests;
import org.w3c.domts.level3.core.canonicalform06;
import org.w3c.domts.level3.core.documentrenamenode09;
import org.w3c.domts.level3.core.documentrenamenode19;
import org.w3c.domts.level3.core.documentrenamenode27;
import org.w3c.domts.level3.core.documentrenamenode28;
import org.w3c.domts.level3.core.documentsetxmlstandalone01;
import org.w3c.domts.level3.core.domconfigurationcansetparameter03;
import org.w3c.domts.level3.core.domconfigurationcansetparameter06;
import org.w3c.domts.level3.core.elementsetidattributens05;
import org.w3c.domts.level3.core.namespacedeclarations02;
import org.w3c.domts.level3.core.nodeinsertbefore14;
import org.w3c.domts.level3.core.nodeinsertbefore15;
import org.w3c.domts.level3.core.nodeinsertbefore25;
import org.w3c.domts.level3.core.nodeisdefaultnamespace04;
import org.w3c.domts.level3.core.nodeisequalnode13;
import org.w3c.domts.level3.core.nodeisequalnode18;
import org.w3c.domts.level3.core.nodelookupnamespaceuri04;
import org.w3c.domts.level3.core.nodelookupprefix04;
import org.w3c.domts.level3.core.noderemovechild07;

import com.google.code.ddom.domts.FilteredDOMTestSuite;

public class DOM3CoreTest extends TestSuite {
    public static TestSuite suite() throws Exception {
        DOMTestDocumentBuilderFactory factory = Factories.getFactory();
        FilteredDOMTestSuite suite = new FilteredDOMTestSuite(factory, new alltests(factory));
        
        // Tests accessing DTD information
        suite.addExclude("documentadoptnode(17|18|19|20)");
        suite.addExclude(documentrenamenode28.class);
        suite.addExclude(domconfigurationcansetparameter03.class);
        suite.addExclude("entityget.*");
        suite.addExclude("nodeissamenode(07|08|09)");
        suite.addExclude(nodelookupnamespaceuri04.class);
        suite.addExclude(nodelookupprefix04.class);
        suite.addExclude(noderemovechild07.class);
        suite.addExclude("noderemovechild(24|25|26|27)");
        suite.addExclude("nodereplacechild(10|21|25|28|31|38)");
        suite.addExclude("nodegettextcontent(05|18)");
        suite.addExclude("nodeisequalnode(25|26|27)");
        suite.addExclude(nodeisdefaultnamespace04.class);
        suite.addExclude("nodesettextcontent(04|13)");
        
        // Tests using entity references
        suite.addExclude(documentrenamenode27.class);
        suite.addExclude("entities.*");
        suite.addExclude("infoset(01|02)");
        suite.addExclude("textreplacewholetext(06|07|08)");
        suite.addExclude(nodeinsertbefore25.class);
        suite.addExclude("noderemovechild(13|14|15)");
        suite.addExclude("nodereplacechild(22|23|24)");
        suite.addExclude("nodegettextcontent(15|16|17)");
        suite.addExclude("elementsetidattribute(node|ns)09");
        
        // Tests relying on Attr#getSchemaTypeInfo
        suite.addExclude("attrgetschematypeinfo.*");
        suite.addExclude("elementgetschematypeinfo.*");
        suite.addExclude("typeinfo.*");
        
        // Tests relying on Text#isElementContentWhitespace
        suite.addExclude(domconfigurationcansetparameter06.class);
        
        // Tests relying on entity references
        suite.addExclude("documentadoptnode(06|16)");
        
        // Tests using setIdAttributeNode on a namespace declaration (!)
        suite.addExclude("elementsetidattributenode(01|04|08)");
        suite.addExclude("elementsetidattributens(01|03|10|11|13|14)");
        
        // This test case is actually no longer valid because the NameChar and
        // NameStartChar productions have changed in the Fifth Edition of the
        // XML 1.0 specs (and are now the same as in XML 1.1).
        suite.addExclude(canonicalform06.class);
        
        // TODO: later
        suite.addExclude(".*normalizedocument.*");
        suite.addExclude("documentadoptnode(10|11|12|24)");
        suite.addExclude(".*documenturi.*");
        suite.addExclude(".*baseuri.*");
        suite.addExclude(".*inputencoding.*");
        suite.addExclude(".*xmlencoding.*");
        suite.addExclude(".*stricterrorchecking.*");
        suite.addExclude(".*handleerror.*");
        suite.addExclude(".*comparedocumentposition.*");
        suite.addExclude(".*getfeature.*");
        suite.addExclude(".*userdatahandler.*");
        suite.addExclude(".*wellformed.*");
        suite.addExclude(".*userdata.*");
        suite.addExclude(".*infoset.*");
        suite.addExclude(namespacedeclarations02.class);
        suite.addExclude(documentrenamenode09.class);
        suite.addExclude(nodeisequalnode13.class); // Uses cloneNode
        suite.addExclude(nodeisequalnode18.class); // Uses adoptNode
        
        // TODO: uses "*" as namespace in setAttributeNS
        suite.addExclude(elementsetidattributens05.class);
        
        // TODO: conflict during deferred building
        suite.addExclude(documentsetxmlstandalone01.class);
        
        // TODO: wrong exception thrown
        suite.addExclude(documentrenamenode19.class);
        
        // TODO: only the wrong exception type is thrown here
        suite.addExclude(nodeinsertbefore14.class);
        suite.addExclude(nodeinsertbefore15.class);
        
        return new JUnitTestSuiteAdapter(suite);
    }
}
