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
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.WrongDocumentException;

public class TestCoreReplaceWith1FromWrongDocument extends BackendTestCase {
    public TestCoreReplaceWith1FromWrongDocument(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document1 = nodeFactory.createDocument();
        CoreDocument document2 = nodeFactory.createDocument();
        CoreChildNode node1 = nodeFactory.createComment(document1, "test");
        document1.coreAppendChild(node1);
        CoreChildNode node2 = nodeFactory.createComment(document2, "test");
        try {
            node1.coreReplaceWith(node2);
            fail("Expected WrongDocumentException");
        } catch (WrongDocumentException ex) {
            // Expected
        }
    }
}
