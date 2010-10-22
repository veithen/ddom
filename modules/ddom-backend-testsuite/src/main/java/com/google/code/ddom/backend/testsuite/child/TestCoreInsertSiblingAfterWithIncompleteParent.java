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
package com.google.code.ddom.backend.testsuite.child;

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.CoreText;

public class TestCoreInsertSiblingAfterWithIncompleteParent extends BackendTestCase {
    public TestCoreInsertSiblingAfterWithIncompleteParent(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = parse("<root><a>test</a><b>test</b></root>");
        CoreText text = nodeFactory.createText(document, "text1");
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
