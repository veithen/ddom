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
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.stream.NullXmlOutput;
import com.googlecode.ddom.stream.Stream;

/**
 * Tests that an invocation of {@link CoreParentNode#coreGetFirstChild()} on a {@link CoreElement} still returns
 * the first child if the rest of the element has been consumed.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetFirstChildInStateConsumedPartial extends BackendTestCase {
    public TestCoreGetFirstChildInStateConsumedPartial(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement element = parse("<root>test<a/>test</root>").coreGetDocumentElement();
        CoreChildNode firstChild = element.coreGetFirstChild();
        // Consume the content of the element
        new Stream(element.coreGetInput(false), new NullXmlOutput()).flush();
        assertSame(firstChild, element.coreGetFirstChild());
    }
}
