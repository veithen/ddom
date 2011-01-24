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
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreText;
import com.googlecode.ddom.core.WrongDocumentException;

public class TestCoreInsertSiblingsAfterFromWrongDocument extends BackendTestCase {
    public TestCoreInsertSiblingsAfterFromWrongDocument(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document1 = nodeFactory.createDocument();
        CoreDocument document2 = nodeFactory.createDocument();
        CoreElement parent = nodeFactory.createElement(document1, "test");
        CoreText text1 = nodeFactory.createText(document1, "text1");
        CoreDocumentFragment fragment = nodeFactory.createDocumentFragment(document2);
        fragment.coreAppendChild(nodeFactory.createText(document2, "text2"), Policies.REJECT);
        parent.coreAppendChild(text1, Policies.REJECT);
        try {
            text1.coreInsertSiblingsAfter(fragment);
            fail("Expected WrongDocumentException");
        } catch (WrongDocumentException ex) {
            // Expected
        }
    }
}
