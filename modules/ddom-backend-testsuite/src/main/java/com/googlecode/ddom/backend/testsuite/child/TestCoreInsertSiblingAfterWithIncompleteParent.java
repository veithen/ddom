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
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreNSAwareElement;

public class TestCoreInsertSiblingAfterWithIncompleteParent extends BackendTestCase {
    public TestCoreInsertSiblingAfterWithIncompleteParent(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = parse("<root><a>test</a><b>test</b></root>");
        CoreCharacterData text = nodeFactory.createCharacterData(document, "text1");
        CoreNSAwareElement a = (CoreNSAwareElement)document.coreGetDocumentElement().coreGetFirstChild();
        a.coreInsertSiblingAfter(text);
        if (builderType >= BUILDER_TYPE_2) {
            assertFalse(a.coreIsComplete());
        }
        assertSame(text, a.coreGetNextSibling());
        CoreNSAwareElement b = (CoreNSAwareElement)text.coreGetNextSibling();
        assertEquals("b", b.coreGetLocalName());
    }
}
