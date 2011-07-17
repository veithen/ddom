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
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.NoParentException;

public class TestCoreInsertSiblingBeforeOnOrphan extends BackendTestCase {
    public TestCoreInsertSiblingBeforeOnOrphan(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreCharacterData text1 = nodeFactory.createCharacterData(document, "text1");
        CoreCharacterData text2 = nodeFactory.createCharacterData(document, "text2");
        try {
            text1.coreInsertSiblingBefore(text2, Policies.REQUIRE_SAME_DOCUMENT);
            fail("Expected NoParentException");
        } catch (NoParentException ex) {
            // Expected
        }
    }
}
