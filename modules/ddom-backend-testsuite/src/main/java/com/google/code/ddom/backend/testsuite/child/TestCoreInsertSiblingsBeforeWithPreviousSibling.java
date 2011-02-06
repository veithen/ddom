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
package com.google.code.ddom.backend.testsuite.child;

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.backend.testsuite.CoreAssert;
import com.google.code.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;

public class TestCoreInsertSiblingsBeforeWithPreviousSibling extends BackendTestCase {
    public TestCoreInsertSiblingsBeforeWithPreviousSibling(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "", "test", "");
        CoreComment comment = nodeFactory.createComment(document, "test");
        element.coreAppendChild(comment, Policies.REJECT);
        CoreDocumentFragment fragment = nodeFactory.createDocumentFragment(document);
        CoreChildNode fragmentChild1 = nodeFactory.createElement(document, "", "test", "");
        CoreChildNode fragmentChild2 = nodeFactory.createProcessingInstruction(document, "pi", "test");
        fragment.coreAppendChild(fragmentChild1, Policies.REJECT);
        fragment.coreAppendChild(fragmentChild2, Policies.REJECT);
        comment.coreInsertSiblingsBefore(fragment);
        CoreAssert.assertSiblings(fragmentChild2, comment);
        assertSame(fragmentChild1, element.coreGetFirstChild());
        assertEquals(3, element.coreGetChildCount());
        assertSame(element, fragmentChild1.coreGetParent());
        assertSame(element, fragmentChild2.coreGetParent());
        assertEquals(0, fragment.coreGetChildCount());
        assertNull(fragment.coreGetFirstChild());
    }
}
