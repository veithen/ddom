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
import com.google.code.ddom.core.CyclicRelationshipException;

public class TestCoreInsertSiblingAfterOnChild extends BackendTestCase {
    public TestCoreInsertSiblingAfterOnChild(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = documentFactory.createDocument();
        CoreElement element = document.coreCreateElement(null, "test", null);
        CoreText text = document.coreCreateText("test");
        element.coreAppendChild(text);
        try {
            text.coreInsertSiblingAfter(element);
            fail("Expected CyclicRelationshipException");
        } catch (CyclicRelationshipException ex) {
            // Expected
        }
    }
}
