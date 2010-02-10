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

import org.junit.Assert;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;

public class TestCoreClear extends ParentNodeTestCase {
    public TestCoreClear(BackendTestSuiteConfig config, ParentNodeFactory parentNodeFactory) {
        super(config, parentNodeFactory);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreParentNode parent = parentNodeFactory.createNode(document);
        CoreText child1 = nodeFactory.createText(document, "text1");
        CoreText child2 = nodeFactory.createText(document, "text2");
        parent.coreAppendChild(child1);
        parent.coreAppendChild(child2);
        Assert.assertEquals(2, parent.coreGetChildCount());
        parent.coreClear();
        Assert.assertEquals(0, parent.coreGetChildCount());
        Assert.assertNull(parent.coreGetFirstChild());
        Assert.assertFalse(child1.coreHasParent());
        Assert.assertFalse(child2.coreHasParent());
    }
}