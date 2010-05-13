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

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.backend.testsuite.CoreAssert;
import com.google.code.ddom.core.CoreCDATASection;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreComment;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreText;

public class TestCoreCoalesce extends BackendTestCase {
    public TestCoreCoalesce(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Exception {
        CoreDocument document = documentFactory.createDocument();
        CoreElement element = document.coreCreateElement("test");
        CoreText text1 = document.coreCreateText("A");
        element.coreAppendChild(text1);
        CoreText text2 = document.coreCreateText("B");
        element.coreAppendChild(text2);
        CoreCDATASection cdata = document.coreCreateCDATASection("C");
        element.coreAppendChild(cdata);
        CoreText text3 = document.coreCreateText("D");
        element.coreAppendChild(text3);
        CoreComment comment = document.coreCreateComment("test");
        element.coreAppendChild(comment);
        CoreText text4 = document.coreCreateText("E");
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
