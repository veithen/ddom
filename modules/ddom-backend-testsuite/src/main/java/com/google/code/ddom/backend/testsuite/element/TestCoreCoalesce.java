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
package com.google.code.ddom.backend.testsuite.element;

import org.junit.Assert;

import com.google.code.ddom.backend.CoreCDATASection;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreComment;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.CoreAssert;

public class TestCoreCoalesce extends BackendTestCase {
    public TestCoreCoalesce(NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    @Override
    protected void runTest() throws Exception {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "test");
        CoreText text1 = nodeFactory.createText(document, "A");
        element.coreAppendChild(text1);
        CoreText text2 = nodeFactory.createText(document, "B");
        element.coreAppendChild(text2);
        CoreCDATASection cdata = nodeFactory.createCDATASection(document, "C");
        element.coreAppendChild(cdata);
        CoreText text3 = nodeFactory.createText(document, "D");
        element.coreAppendChild(text3);
        CoreComment comment = nodeFactory.createComment(document, "test");
        element.coreAppendChild(comment);
        CoreText text4 = nodeFactory.createText(document, "E");
        element.coreAppendChild(text4);
        element.coreCoalesce(true);
        
        CoreChildNode child = element.coreGetFirstChild();
        Assert.assertTrue(child instanceof CoreText);
        Assert.assertEquals("ABCD", ((CoreText)child).coreGetData());
        child = child.coreGetNextSibling();
        Assert.assertSame(comment, child);
        child = child.coreGetNextSibling();
        Assert.assertSame(text4, child);
        
        CoreAssert.assertOrphan(text1);
        CoreAssert.assertOrphan(text2);
        CoreAssert.assertOrphan(cdata);
        CoreAssert.assertOrphan(text3);
        Assert.assertEquals(3, element.coreGetChildCount());
    }
}
