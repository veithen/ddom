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
package com.googlecode.ddom.backend.testsuite;

import junit.framework.TestSuite;

import com.googlecode.ddom.backend.testsuite.nsaware.NSAwareNamedNodeFactory;
import com.googlecode.ddom.backend.testsuite.parent.ParentNodeFactory;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreNSAwareNamedNode;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.CoreTypedAttribute;
import com.googlecode.ddom.core.NodeFactory;

public class BackendTestSuiteBuilder {
    private BackendTestSuiteBuilder() {}
    
    public static TestSuite suite(NodeFactory nodeFactory, int builderType) {
        BackendTestSuiteConfig config = new BackendTestSuiteConfig(nodeFactory, builderType);
        TestSuite suite = new TestSuite();
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreDetach(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreDetachIncomplete(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterFromWrongDocument(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnChild(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnOrphan(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnSelf(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterWithIncompleteParent(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterWithInvalidChildType(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterWithNextSibling(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterWithoutNextSibling(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeFromWrongDocument(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnChild(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnOrphan(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnSelf(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeWithInvalidChildType(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeWithoutPreviousSibling(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeWithPreviousSibling(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsAfterFromWrongDocument(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsAfterOnOrphan(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsAfterWithIncomplete(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsBeforeFromWrongDocument(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsBeforeOnOrphan(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsBeforeWithEmptyFragment(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsBeforeWithoutPreviousSibling(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreInsertSiblingsBeforeWithPreviousSibling(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreReplaceWith1FromWrongDocument(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreReplaceWith1OnOrphan(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreReplaceWith1OnSelf(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreReplaceWith2FromWrongDocument(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.child.TestCoreReplaceWith2OnOrphan(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.document.TestCoreGetDocumentElement(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreClearIncomplete(config));
        // TODO
//        suite.addTest(new com.google.code.ddom.backend.testsuite.element.TestCoreCoalesce(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreGetChildrenByTypeAndRemove(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreGetInputWithChildInStateContentSet(config, true));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreGetInputWithChildInStateContentSet(config, false));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreGetInputWithChildInStateSourceSet(config, true));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreGetInputWithChildInStateSourceSet(config, false));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreGetInputWithDetachedChild(config, true));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreGetInputWithDetachedChild(config, false));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreIsEmptyOnEmptyElement(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreIsEmptyOnIncompleteElement(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreSetAttributeAlreadyOwnedByElement(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreSetContentLaziness(config));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.element.TestCoreSetSource(config));
        addNSAwareNamedNodeTests(suite, nodeFactory, new NSAwareNamedNodeFactory() {
            public Class<? extends CoreNSAwareNamedNode> getNodeClass() {
                return CoreNSAwareElement.class;
            }
            
            public CoreNSAwareNamedNode create(NodeFactory nodeFactory, CoreDocument document, String namespaceURI, String localName, String prefix) {
                return nodeFactory.createElement(document, namespaceURI, localName, prefix);
            }
        });
        addNSAwareNamedNodeTests(suite, nodeFactory, new NSAwareNamedNodeFactory() {
            public Class<? extends CoreNSAwareNamedNode> getNodeClass() {
                return CoreNSAwareAttribute.class;
            }
            
            public CoreNSAwareNamedNode create(NodeFactory nodeFactory, CoreDocument document, String namespaceURI, String localName, String prefix) {
                return nodeFactory.createAttribute(document, namespaceURI, localName, prefix, "test", "CDATA");
            }
        });
        addParentNodeTests(suite, config, new ParentNodeFactory() {
            public Class<? extends CoreParentNode> getNodeClass() {
                return CoreTypedAttribute.class;
            }
            
            public CoreParentNode createNode(NodeFactory nodeFactory, CoreDocument document) {
                return nodeFactory.createAttribute(document, "", "attr", "", null, null);
            }
        });
        addParentNodeTests(suite, config, new ParentNodeFactory() {
            public Class<? extends CoreParentNode> getNodeClass() {
                return CoreElement.class;
            }
            
            public CoreParentNode createNode(NodeFactory nodeFactory, CoreDocument document) {
                return nodeFactory.createElement(document, "", "element", "");
            }
        });
        return suite;
    }
    
    private static void addNSAwareNamedNodeTests(TestSuite suite, NodeFactory nodeFactory, NSAwareNamedNodeFactory namedNodeFactory) {
    }
    
    private static void addParentNodeTests(TestSuite suite, BackendTestSuiteConfig config, ParentNodeFactory parentNodeFactory) {
        suite.addTest(new com.googlecode.ddom.backend.testsuite.parent.TestCoreAppendChildFromWrongDocument(config, parentNodeFactory));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.parent.TestCoreAppendChildrenFromWrongDocument(config, parentNodeFactory));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.parent.TestCoreClear(config, parentNodeFactory));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.parent.TestCoreSetValue(config, parentNodeFactory));
        suite.addTest(new com.googlecode.ddom.backend.testsuite.parent.TestCoreSetValueOnNodeWithChildren(config, parentNodeFactory));
    }
}
