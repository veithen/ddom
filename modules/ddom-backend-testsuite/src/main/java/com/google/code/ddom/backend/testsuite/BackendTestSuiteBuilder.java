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

import com.google.code.ddom.backend.NodeFactory;

import junit.framework.TestSuite;

public class BackendTestSuiteBuilder {
    private BackendTestSuiteBuilder() {}
    
    public static TestSuite suite(NodeFactory nodeFactory) {
        TestSuite suite = new TestSuite();
        suite.addTest(new com.google.code.ddom.backend.testsuite.attribute.TestCoreSetValue(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreDetach(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfter(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnChild(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnOrphan(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingAfterOnSelf(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBefore(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnChild(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnOrphan(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.child.TestCoreInsertSiblingBeforeOnSelf(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.document.TestCoreGetDocumentElement(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.element.TestCoreCoalesce(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.element.TestCoreSetValue(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.parent.TestCoreInsertChildAfterSelf(nodeFactory));
        suite.addTest(new com.google.code.ddom.backend.testsuite.parent.TestCoreInsertChildBeforeSelf(nodeFactory));
        return suite;
    }
}
