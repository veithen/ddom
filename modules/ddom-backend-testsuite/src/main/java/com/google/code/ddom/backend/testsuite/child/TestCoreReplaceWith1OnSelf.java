/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.backend.testsuite.child;

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.backend.testsuite.CoreAssert;
import com.google.code.ddom.backend.testsuite.Policies;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;

public class TestCoreReplaceWith1OnSelf extends BackendTestCase {
    public TestCoreReplaceWith1OnSelf(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreChildNode child1 = nodeFactory.createDocumentTypeDeclaration(document, "test", null, "test.dtd");
        CoreChildNode child2 = nodeFactory.createComment(document, "comment");
        CoreChildNode child3 = nodeFactory.createElement(document, null, "root", null);
        document.coreAppendChild(child1, Policies.REJECT);
        document.coreAppendChild(child2, Policies.REJECT);
        document.coreAppendChild(child3, Policies.REJECT);
        child2.coreReplaceWith(child2);
        CoreAssert.assertSiblings(child1, child2);
        CoreAssert.assertSiblings(child2, child3);
        assertEquals(3, document.coreGetChildCount());
        assertSame(document, child2.coreGetParent());
    }
}
