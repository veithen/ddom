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
package com.google.code.ddom.backend.testsuite.child;

import org.junit.Assert;

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.backend.testsuite.CoreAssert;
import com.google.code.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreText;

public class TestCoreDetach extends BackendTestCase {
    public TestCoreDetach(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "test");
        CoreText text1 = nodeFactory.createText(document, "text1");
        element.coreAppendChild(text1, Policies.REJECT);
        CoreText text2 = nodeFactory.createText(document, "text1");
        element.coreAppendChild(text2, Policies.REJECT);
        CoreText text3 = nodeFactory.createText(document, "text1");
        element.coreAppendChild(text3, Policies.REJECT);
        
        text2.coreDetach();
        
        CoreAssert.assertOrphan(text2);
        CoreAssert.assertSiblings(text1, text3);
        Assert.assertEquals(2, element.coreGetChildCount());
        Assert.assertSame(document, text2.coreGetOwnerDocument(false));
    }
}
