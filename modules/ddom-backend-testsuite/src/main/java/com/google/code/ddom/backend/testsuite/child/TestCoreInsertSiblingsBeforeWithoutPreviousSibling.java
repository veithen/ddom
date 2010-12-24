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
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreElement;

public class TestCoreInsertSiblingsBeforeWithoutPreviousSibling extends BackendTestCase {
    public TestCoreInsertSiblingsBeforeWithoutPreviousSibling(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, null, "test", null);
        CoreChildNode child1 = nodeFactory.createCDATASection(document);
        CoreChildNode child2 = nodeFactory.createComment(document, "test");
        element.coreAppendChild(child1);
        element.coreAppendChild(child2);
        CoreDocumentFragment fragment = nodeFactory.createDocumentFragment(document);
        CoreChildNode fragmentChild1 = nodeFactory.createElement(document, null, "test", null);
        CoreChildNode fragmentChild2 = nodeFactory.createProcessingInstruction(document, "pi", "test");
        fragment.coreAppendChild(fragmentChild1);
        fragment.coreAppendChild(fragmentChild2);
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
