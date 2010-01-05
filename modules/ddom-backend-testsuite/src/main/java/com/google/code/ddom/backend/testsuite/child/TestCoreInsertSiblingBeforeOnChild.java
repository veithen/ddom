/*
 * Copyright 2009 Andreas Veithen
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

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.CyclicRelationshipException;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.backend.testsuite.BackendTestCase;

public class TestCoreInsertSiblingBeforeOnChild extends BackendTestCase {
    public TestCoreInsertSiblingBeforeOnChild(NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument(null);
        CoreElement element = nodeFactory.createElement(document, null, "test", null);
        CoreText text = nodeFactory.createText(document, "test");
        element.coreAppendChild(text);
        try {
            text.coreInsertSiblingBefore(element);
            fail("Expected CyclicRelationshipException");
        } catch (CyclicRelationshipException ex) {
            // Expected
        }
    }
}