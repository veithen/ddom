/*
 * Copyright 2013 Andreas Veithen
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
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreElement;

/**
 * Tests that {@link CoreChildNode#coreDetach()} preserves the owner document in the case where it
 * is created lazily.
 * 
 * @author Andreas Veithen
 */
public class TestCoreDetachWithLazyOwnerDocument extends BackendTestCase {
    public TestCoreDetachWithLazyOwnerDocument(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement element = nodeFactory.createElement(null, "test");
        CoreCharacterData text = nodeFactory.createCharacterData(null, "text");
        element.coreAppendChild(text, Policies.MOVE);
        text.coreDetach();
        assertSame(element.coreGetOwnerDocument(true), text.coreGetOwnerDocument(true));
    }
}
