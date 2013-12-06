/*
 * Copyright 2009-2012 Andreas Veithen
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
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;

/**
 * Tests the behavior of {@link CoreChildNode#coreDetach(CoreDocument)} if the node to detach is
 * incomplete and the specified document is <code>null</code>.
 * 
 * @author Andreas Veithen
 */
public class TestCoreDetachIncompleteToNewDocument extends BackendTestCase {
    public TestCoreDetachIncompleteToNewDocument(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = parse("<root><a>test</a></root>", true);
        CoreElement root = document.coreGetDocumentElement();
        CoreElement a = (CoreElement)root.coreGetFirstChild();
        assertFalse(a.coreIsComplete());
        a.coreDetach(null);
        assertNotSame(document, a.coreGetOwnerDocument(true));
        // Now access the child node to trigger deferred parsing; if the builder is not set up
        // correctly after moving to the new document, an exception will occur here
        CoreChildNode child = a.coreGetFirstChild();
        assertTrue(child instanceof CoreCharacterData);
        assertEquals("test", ((CoreCharacterData)child).coreGetData());
        assertNull(child.coreGetNextSibling());
    }
}
