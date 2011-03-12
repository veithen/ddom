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
package com.googlecode.ddom.backend.testsuite.element;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ChildIterator;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreElement;

public class TestCoreGetChildrenByTypeAndRemove extends BackendTestCase {
    public TestCoreGetChildrenByTypeAndRemove(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement parent = nodeFactory.createElement(null, "root");
        CoreComment comment = parent.coreAppendComment("comment1");
        CoreElement element1 = parent.coreAppendElement("element1");
        CoreElement element2 = parent.coreAppendElement("element2");
        ChildIterator<CoreElement> it = parent.coreGetChildrenByType(Axis.CHILDREN, CoreElement.class);
        assertTrue(it.hasNext());
        assertSame(element1, it.next());
        it.remove();
        assertTrue(it.hasNext());
        assertSame(element2, it.next());
        assertSame(element2, comment.coreGetNextSibling());
    }
}
