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
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;

public class TestCoreInsertSiblingsAfterWithIncomplete extends BackendTestCase {
    public TestCoreInsertSiblingsAfterWithIncomplete(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "", "test", "");
        CoreComment comment = nodeFactory.createComment(document, "test");
        element.coreAppendChild(comment, Policies.REJECT);
        // TODO: this doesn't work with our parser
//        CoreDocumentFragment fragment = parse(document, "<?pi?><a>test</a>");
        CoreDocumentFragment fragment = parse(document, "<!--comment--><a>test</a>", true);
        comment.coreInsertSiblingsAfter(fragment);
        if (builderType >= BUILDER_TYPE_2) {
            assertFalse(element.coreIsComplete());
        }
        CoreChildNode sibling = comment.coreGetNextSibling();
        assertTrue(sibling instanceof CoreComment);
        sibling = sibling.coreGetNextSibling();
        assertTrue(sibling instanceof CoreElement);
    }
}
