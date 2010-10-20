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
package com.google.code.ddom.backend.testsuite.parent;

import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreParentNode;
import com.google.code.ddom.core.WrongDocumentException;

public class TestCoreAppendChildrenFromWrongDocument extends ParentNodeTestCase {
    public TestCoreAppendChildrenFromWrongDocument(BackendTestSuiteConfig config, ParentNodeFactory parentNodeFactory) {
        super(config, parentNodeFactory);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document1 = nodeFactory.createDocument();
        CoreParentNode parent = parentNodeFactory.createNode(document1);
        CoreDocument document2 = nodeFactory.createDocument();
        CoreDocumentFragment fragment = document2.coreCreateDocumentFragment();
        fragment.coreAppendChild(document2.coreCreateText("text"));
        try {
            parent.coreAppendChildren(fragment);
            fail("Expected WrongDocumentException");
        } catch (WrongDocumentException ex) {
            // Expected
        }
    }
}
