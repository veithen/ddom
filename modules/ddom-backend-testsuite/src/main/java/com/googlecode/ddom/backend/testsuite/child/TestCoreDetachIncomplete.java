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
package com.googlecode.ddom.backend.testsuite.child;

import org.junit.Assert;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNSAwareElement;

/**
 * Test that deferred parsing proceeds correctly after detaching an incomplete node.
 * 
 * @author Andreas Veithen
 */
public class TestCoreDetachIncomplete extends BackendTestCase {
    public TestCoreDetachIncomplete(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = parse("<root><a>test</a><b/></root>", true);
        CoreElement root = document.coreGetDocumentElement();
        CoreElement a = (CoreElement)root.coreGetFirstChild();
        Assert.assertFalse(a.coreIsComplete());
        a.coreDetach();
        if (builderType >= BUILDER_TYPE_2) {
            assertFalse(a.coreIsComplete());
        } else {
            assertTrue(a.coreIsComplete());
        }
        CoreNSAwareElement b = (CoreNSAwareElement)root.coreGetFirstChild();
        Assert.assertNotNull(b);
        Assert.assertEquals("b", b.coreGetLocalName());
    }
}
