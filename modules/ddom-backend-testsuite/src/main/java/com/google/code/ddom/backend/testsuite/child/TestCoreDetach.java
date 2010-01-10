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

import org.junit.Assert;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.CoreAssert;

public class TestCoreDetach extends BackendTestCase {
    public TestCoreDetach(NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "test");
        CoreText text1 = nodeFactory.createText(document, "text1");
        element.coreAppendChild(text1);
        CoreText text2 = nodeFactory.createText(document, "text1");
        element.coreAppendChild(text2);
        CoreText text3 = nodeFactory.createText(document, "text1");
        element.coreAppendChild(text3);
        
        text2.coreDetach();
        
        CoreAssert.assertOrphan(text2);
        Assert.assertSame(text3, text1.coreGetNextSibling());
        Assert.assertSame(text1, text3.coreGetPreviousSibling());
        Assert.assertEquals(2, element.coreGetChildCount());
    }
}
