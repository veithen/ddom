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
package com.googlecode.ddom.backend.testsuite.parent;

import org.junit.Assert;

import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreParentNode;

public class TestCoreSetValueOnNodeWithChildren extends ParentNodeTestCase {
    public TestCoreSetValueOnNodeWithChildren(BackendTestSuiteConfig config, ParentNodeFactory parentNodeFactory) {
        super(config, parentNodeFactory);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreParentNode parent = parentNodeFactory.createNode(nodeFactory, document);
        CoreCharacterData child1 = nodeFactory.createCharacterData(document, "text1");
        CoreCharacterData child2 = nodeFactory.createCharacterData(document, "text2");
        parent.coreAppendChild(child1, Policies.REJECT);
        parent.coreAppendChild(child2, Policies.REJECT);
        Assert.assertTrue(parent.coreIsExpanded());
        Assert.assertEquals(2, parent.coreGetChildCount());
        
        parent.coreSetValue("test");
        Assert.assertFalse(parent.coreIsExpanded());
        Assert.assertEquals(1, parent.coreGetChildCount());
        CoreChildNode child = parent.coreGetFirstChild();
        Assert.assertTrue(child instanceof CoreCharacterData);
        Assert.assertEquals("test", ((CoreCharacterData)child).coreGetData());
        Assert.assertTrue(parent.coreIsExpanded());
        
        Assert.assertFalse(child1.coreHasParent());
        Assert.assertFalse(child2.coreHasParent());
    }
}
