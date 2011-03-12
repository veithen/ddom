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
package com.googlecode.ddom.backend.testsuite.child;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.backend.testsuite.CoreAssert;
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;

public class TestCoreInsertSiblingsBeforeWithoutPreviousSibling extends BackendTestCase {
    public TestCoreInsertSiblingsBeforeWithoutPreviousSibling(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "", "test", "");
        CoreChildNode child1 = nodeFactory.createCDATASection(document, "test");
        CoreChildNode child2 = nodeFactory.createComment(document, "data");
        element.coreAppendChild(child1, Policies.REJECT);
        element.coreAppendChild(child2, Policies.REJECT);
        CoreDocumentFragment fragment = nodeFactory.createDocumentFragment(document);
        CoreChildNode fragmentChild1 = nodeFactory.createElement(document, "", "test", "");
        CoreChildNode fragmentChild2 = nodeFactory.createProcessingInstruction(document, "pi", "test");
        fragment.coreAppendChild(fragmentChild1, Policies.REJECT);
        fragment.coreAppendChild(fragmentChild2, Policies.REJECT);
        child2.coreInsertSiblingsBefore(fragment);
        CoreAssert.assertSiblings(child1, fragmentChild1);
        CoreAssert.assertSiblings(fragmentChild2, child2);
        assertEquals(4, element.coreGetChildCount());
        assertSame(element, fragmentChild1.coreGetParent());
        assertSame(element, fragmentChild2.coreGetParent());
        assertEquals(0, fragment.coreGetChildCount());
        assertNull(fragment.coreGetFirstChild());
    }
}