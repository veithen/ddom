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
package com.google.code.ddom.backend.testsuite.parent;

import org.junit.Assert;

import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreParentNode;
import com.google.code.ddom.core.CoreText;

public class TestCoreSetValue extends ParentNodeTestCase {
    public TestCoreSetValue(BackendTestSuiteConfig config, ParentNodeFactory parentNodeFactory) {
        super(config, parentNodeFactory);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreParentNode parent = parentNodeFactory.createNode(nodeFactory, document);
        parent.coreSetValue("test");
        Assert.assertFalse(parent.coreIsExpanded());
        CoreChildNode child = parent.coreGetFirstChild();
        Assert.assertTrue(child instanceof CoreText);
        Assert.assertEquals("test", ((CoreText)child).coreGetData());
        Assert.assertTrue(parent.coreIsExpanded());
        Assert.assertEquals(1, parent.coreGetChildCount());
    }
}
