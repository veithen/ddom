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
package com.google.code.ddom.backend.testsuite.child;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreComment;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;

public class TestCoreInsertSiblingsBefore extends BackendTestCase {
    public TestCoreInsertSiblingsBefore(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, null, "test", null);
        CoreComment comment = nodeFactory.createComment(document, "test");
        element.coreAppendChild(comment);
        CoreDocumentFragment fragment = nodeFactory.createDocumentFragment(document);
        CoreChildNode fragmentChild1 = nodeFactory.createElement(document, null, "test", null);
        CoreChildNode fragmentChild2 = nodeFactory.createProcessingInstruction(document, "pi", "test");
        fragment.coreAppendChild(fragmentChild1);
        fragment.coreAppendChild(fragmentChild2);
        comment.coreInsertSiblingsBefore(fragment);
        assertSame(fragmentChild2, comment.coreGetPreviousSibling());
        assertSame(comment, fragmentChild2.coreGetNextSibling());
        assertSame(fragmentChild1, element.coreGetFirstChild());
        assertEquals(3, element.coreGetChildCount());
        assertSame(element, fragmentChild1.coreGetParent());
        assertSame(element, fragmentChild2.coreGetParent());
        // TODO: failure here
//        assertEquals(0, fragment.coreGetChildCount());
//        assertNull(fragment.coreGetFirstChild());
    }
}
