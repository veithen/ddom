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
package com.google.code.ddom.backend.testsuite.element;

import org.junit.Assert;

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.backend.testsuite.CoreAssert;
import com.google.code.ddom.backend.testsuite.Policies;
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
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "test");
        CoreText text1 = nodeFactory.createText(document, "A");
        element.coreAppendChild(text1, Policies.REJECT);
        CoreText text2 = nodeFactory.createText(document, "B");
        element.coreAppendChild(text2, Policies.REJECT);
        CoreCDATASection cdata = nodeFactory.createCDATASection(document);
        cdata.coreSetValue("C");
        element.coreAppendChild(cdata, Policies.REJECT);
        CoreText text3 = nodeFactory.createText(document, "D");
        element.coreAppendChild(text3, Policies.REJECT);
        CoreComment comment = nodeFactory.createComment(document, "test");
        element.coreAppendChild(comment, Policies.REJECT);
        CoreText text4 = nodeFactory.createText(document, "E");
        element.coreAppendChild(text4, Policies.REJECT);
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
