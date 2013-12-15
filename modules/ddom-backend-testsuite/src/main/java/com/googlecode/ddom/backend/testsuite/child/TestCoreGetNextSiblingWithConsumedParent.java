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
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.NodeConsumedException;
import com.googlecode.ddom.stream.NullXmlOutput;
import com.googlecode.ddom.stream.Stream;

/**
 * Tests that {@link CoreChildNode#coreGetNextSibling()} throws a
 * {@link NodeConsumedException} if the next sibling is not available because
 * the parent node has been consumed.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetNextSiblingWithConsumedParent extends BackendTestCase {
    public TestCoreGetNextSiblingWithConsumedParent(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement parent = parse("<root><!--A--><!--B--><!--C--></root>", true).coreGetDocumentElement();
        CoreChildNode firstChild = parent.coreGetFirstChild();
        new Stream(parent.coreGetInput(false), new NullXmlOutput()).flush();
        try {
            firstChild.coreGetNextSibling();
            fail("Expected NodeConsumedException");
        } catch (NodeConsumedException ex) {
            // Expected
        }
    }
}
