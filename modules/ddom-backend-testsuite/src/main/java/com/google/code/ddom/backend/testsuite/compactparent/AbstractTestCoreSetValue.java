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
package com.google.code.ddom.backend.testsuite.compactparent;

import org.junit.Assert;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreCompactParentNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;

public abstract class AbstractTestCoreSetValue extends BackendTestCase {
    public AbstractTestCoreSetValue(BackendTestSuiteConfig config) {
        super(config);
    }

    protected abstract CoreCompactParentNode createNode(CoreDocument document);

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreCompactParentNode element = createNode(document);
        element.coreSetValue("test");
        Assert.assertFalse(element.coreIsExpanded());
        CoreChildNode child = element.coreGetFirstChild();
        Assert.assertTrue(child instanceof CoreText);
        Assert.assertEquals("test", ((CoreText)child).coreGetData());
        Assert.assertTrue(element.coreIsExpanded());
    }
}
