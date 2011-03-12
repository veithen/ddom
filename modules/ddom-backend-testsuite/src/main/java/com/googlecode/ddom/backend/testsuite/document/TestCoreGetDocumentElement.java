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
package com.googlecode.ddom.backend.testsuite.document;

import org.junit.Assert;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;

public class TestCoreGetDocumentElement extends BackendTestCase {
    public TestCoreGetDocumentElement(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Exception {
        CoreDocument document = nodeFactory.createDocument();
        Assert.assertNull(document.coreGetDocumentElement());
        CoreElement element1 = nodeFactory.createElement(document, "", "root1", "");
        document.coreAppendChild(element1, Policies.REJECT);
        Assert.assertSame(element1, document.coreGetDocumentElement());
        CoreElement element2 = nodeFactory.createElement(document, "", "root2", "");
        element1.coreReplaceWith(element2);
        Assert.assertSame(element2, document.coreGetDocumentElement());
    }
}
