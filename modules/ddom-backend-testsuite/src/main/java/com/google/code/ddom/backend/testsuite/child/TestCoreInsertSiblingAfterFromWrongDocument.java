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

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreText;
import com.google.code.ddom.core.WrongDocumentException;

public class TestCoreInsertSiblingAfterFromWrongDocument extends BackendTestCase {
    public TestCoreInsertSiblingAfterFromWrongDocument(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document1 = documentFactory.createDocument();
        CoreDocument document2 = documentFactory.createDocument();
        CoreElement parent = document1.coreCreateElement("test");
        CoreText text1 = document1.coreCreateText("text1");
        CoreText text2 = document2.coreCreateText("text2");
        parent.coreAppendChild(text1);
        try {
            text1.coreInsertSiblingAfter(text2);
            fail("Expected WrongDocumentException");
        } catch (WrongDocumentException ex) {
            // Expected
        }
    }
}
