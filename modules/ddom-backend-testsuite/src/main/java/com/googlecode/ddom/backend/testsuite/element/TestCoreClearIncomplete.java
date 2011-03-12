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
package com.googlecode.ddom.backend.testsuite.element;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.backend.testsuite.CoreAssert;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.TextCollectorPolicy;

/**
 * Tests the behavior of {@link CoreParentNode#coreClear()} on an element that is not complete.
 * 
 * @author Andreas Veithen
 */
public class TestCoreClearIncomplete extends BackendTestCase {
    public TestCoreClearIncomplete(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement element = parse("<parent><a>a</a><b>b</b><c>c</c></parent>").coreGetDocumentElement();
        CoreElement b = (CoreElement)element.coreGetFirstChild().coreGetNextSibling();
        assertFalse(element.coreIsComplete());
        assertFalse(b.coreIsComplete());
        element.coreClear();
        if (builderType >= BUILDER_TYPE_2) {
            assertFalse(b.coreIsComplete());
        }
        // Check that the content of b is not discarded
        assertEquals("b", b.coreGetTextContent(TextCollectorPolicy.DEFAULT));
        CoreAssert.assertOrphan(b);
        // Build the element
        element.coreGetNextSibling();
        // Check that the element is empty
        assertEquals(0, element.coreGetChildCount());
        assertNull(element.coreGetFirstChild());
    }
}
