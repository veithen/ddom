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
package com.google.code.ddom.backend.testsuite;

import junit.framework.TestSuite;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreNSAwareAttribute;
import com.google.code.ddom.backend.CoreNSAwareElement;
import com.google.code.ddom.backend.CoreNSAwareNamedNode;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.CoreTypedAttribute;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.backend.testsuite.nsaware.NSAwareNamedNodeFactory;
import com.google.code.ddom.backend.testsuite.parent.ParentNodeFactory;

public class BackendTestSuiteBuilder {
    private BackendTestSuiteBuilder() {}
    
    public static TestSuite suite(NodeFactory nodeFactory, int builderType) {
        BackendTestSuiteConfig config = new BackendTestSuiteConfig(nodeFactory, builderType);
        TestSuite suite = new TestSuite();
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreDetach(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreDetachIncomplete(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfter(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnChild(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnOrphan(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnSelf(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterWithIncompleteParent(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterWithInvalidChildType(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBefore(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnChild(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnOrphan(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnSelf(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeWithInvalidChildType(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingsAfterWithIncomplete(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingsBefore(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.document.TestCoreGetDocumentElement(config));
        suite.addTest(new com.google.code.ddom.backend.testsuite.element.TestCoreCoalesce(config));
        addNSAwareNamedNodeTests(suite, nodeFactory, new NSAwareNamedNodeFactory() {
            public Class<? extends CoreNSAwareNamedNode> getNodeClass() {
                return CoreNSAwareElement.class;
            }
            
            public CoreNSAwareNamedNode create(CoreDocument document, String namespaceURI, String localName, String prefix) {
                return document.getNodeFactory().createElement(document, namespaceURI, localName, prefix);
            }
        });
        addNSAwareNamedNodeTests(suite, nodeFactory, new NSAwareNamedNodeFactory() {
            public Class<? extends CoreNSAwareNamedNode> getNodeClass() {
                return CoreNSAwareAttribute.class;
            }
            
            public CoreNSAwareNamedNode create(CoreDocument document, String namespaceURI, String localName, String prefix) {
                return document.getNodeFactory().createAttribute(document, namespaceURI, localName, prefix, "test", "CDATA");
            }
        });
        addParentNodeTests(suite, config, new ParentNodeFactory() {
            public Class<? extends CoreParentNode> getNodeClass() {
                return CoreTypedAttribute.class;
            }
            
            public CoreParentNode createNode(CoreDocument document) {
                return document.getNodeFactory().createAttribute(document, null, "attr", null, null, null);
            }
        });
        addParentNodeTests(suite, config, new ParentNodeFactory() {
            public Class<? extends CoreParentNode> getNodeClass() {
                return CoreElement.class;
            }
            
            public CoreParentNode createNode(CoreDocument document) {
                return document.getNodeFactory().createElement(document, null, "element", null);
            }
        });
        return suite;
    }
    
    private static void addNSAwareNamedNodeTests(TestSuite suite, NodeFactory nodeFactory, NSAwareNamedNodeFactory namedNodeFactory) {
    }
    
    private static void addParentNodeTests(TestSuite suite, BackendTestSuiteConfig config, ParentNodeFactory parentNodeFactory) {
        suite.addTest(new com.google.code.ddom.backend.testsuite.parent.TestCoreClear(config, parentNodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.parent.TestCoreSetValue(config, parentNodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.parent.TestCoreSetValueOnNodeWithChildren(config, parentNodeFactory));
    }
}
