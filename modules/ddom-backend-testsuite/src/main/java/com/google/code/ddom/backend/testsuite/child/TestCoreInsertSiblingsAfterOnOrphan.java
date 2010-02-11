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

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.NoParentException;
import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;

public class TestCoreInsertSiblingsAfterOnOrphan extends BackendTestCase {
    public TestCoreInsertSiblingsAfterOnOrphan(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = documentFactory.createDocument();
        CoreText text1 = document.coreCreateText("text1");
        CoreDocumentFragment fragment = document.coreCreateDocumentFragment();
        fragment.coreAppendChild(document.coreCreateText("text2"));
        try {
            text1.coreInsertSiblingsAfter(fragment);
            fail("Expected NoParentException");
        } catch (NoParentException ex) {
            // Expected
        }
    }
}
