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
package com.googlecode.ddom.backend.testsuite.element;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.CoreNSUnawareElement;
import com.googlecode.ddom.core.ElementNameMismatchException;

public class TestCoreSetSourceNSUnawareWithNameMismatch extends BackendTestCase {
    public TestCoreSetSourceNSUnawareWithNameMismatch(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreNSUnawareElement element = nodeFactory.createElement(null, "test");
        element.coreSetSource(toXmlSource("<other>text</other>", false, true));
        try {
            element.coreGetFirstChild();
            fail("Expected ElementNameMismatchException");
        } catch (ElementNameMismatchException ex) {
            // Expected
        }
    }
}
