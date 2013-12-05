/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import java.util.ConcurrentModificationException;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ChildIterator;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.ExceptionTranslator;
import com.googlecode.ddom.core.Selector;

public class TestCoreGetNodesWithConcurrentModification extends BackendTestCase {
    public TestCoreGetNodesWithConcurrentModification(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement parent = nodeFactory.createElement(null, "root");
        CoreElement element1 = parent.coreAppendElement("element1");
        parent.coreAppendElement("element2");
        ChildIterator<CoreElement> it = parent.coreGetNodes(Axis.CHILDREN, Selector.ELEMENT, CoreElement.class, ExceptionTranslator.DEFAULT);
        assertTrue(it.hasNext());
        assertSame(element1, it.next());
        element1.coreDetach();
        try {
            it.hasNext();
            fail("Expected ConcurrentModificationException");
        } catch (ConcurrentModificationException ex) {
            // Expected
        }
    }
}
