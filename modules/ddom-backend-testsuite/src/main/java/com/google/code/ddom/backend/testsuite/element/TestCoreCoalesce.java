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
package com.google.code.ddom.backend.testsuite.element;

import org.junit.Assert;

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.backend.testsuite.CoreAssert;
import com.google.code.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;

public class TestCoreCoalesce extends BackendTestCase {
    public TestCoreCoalesce(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Exception {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "test");
        CoreCharacterData text1 = nodeFactory.createCharacterData(document, "A");
        element.coreAppendChild(text1, Policies.REJECT);
        CoreCharacterData text2 = nodeFactory.createCharacterData(document, "B");
        element.coreAppendChild(text2, Policies.REJECT);
        CoreCDATASection cdata = nodeFactory.createCDATASection(document, "C");
        element.coreAppendChild(cdata, Policies.REJECT);
        CoreCharacterData text3 = nodeFactory.createCharacterData(document, "D");
        element.coreAppendChild(text3, Policies.REJECT);
        CoreComment comment = nodeFactory.createComment(document, "test");
        element.coreAppendChild(comment, Policies.REJECT);
        CoreCharacterData text4 = nodeFactory.createCharacterData(document, "E");
        element.coreAppendChild(text4, Policies.REJECT);
        element.coreCoalesce(true);
        
        CoreChildNode child = element.coreGetFirstChild();
        Assert.assertTrue(child instanceof CoreCharacterData);
        Assert.assertEquals("ABCD", ((CoreCharacterData)child).coreGetData());
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
