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
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreText;

public class TestCoreInsertSiblingBeforeWithoutPreviousSibling extends BackendTestCase {
    public TestCoreInsertSiblingBeforeWithoutPreviousSibling(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement parent = nodeFactory.createElement(document, "test");
        CoreText text1 = nodeFactory.createText(document, "text1");
        CoreText text2 = nodeFactory.createText(document, "text2");
        parent.coreAppendChild(text1, Policies.REJECT);
        text1.coreInsertSiblingBefore(text2);
        assertEquals(2, parent.coreGetChildCount());
        assertSame(parent, text2.coreGetParent());
        assertSame(text2, parent.coreGetFirstChild());
    }
}
