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
import com.google.code.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;

public class TestCoreInsertSiblingsBeforeWithEmptyFragment extends BackendTestCase {
    public TestCoreInsertSiblingsBeforeWithEmptyFragment(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, null, "test", null);
        CoreComment comment = nodeFactory.createComment(document, "test");
        element.coreAppendChild(comment, Policies.REJECT);
        CoreDocumentFragment emptyFragment = nodeFactory.createDocumentFragment(document);
        comment.coreInsertSiblingsBefore(emptyFragment);
        assertSame(comment, element.coreGetFirstChild());
        assertEquals(1, element.coreGetChildCount());
    }
}
