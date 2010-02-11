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
package com.google.code.ddom.backend.testsuite.document;

import org.junit.Assert;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;

public class TestCoreGetDocumentElement extends BackendTestCase {
    public TestCoreGetDocumentElement(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Exception {
        CoreDocument document = documentFactory.createDocument();
        Assert.assertNull(document.coreGetDocumentElement());
        CoreElement element1 = document.coreCreateElement(null, "root1", null);
        document.coreAppendChild(element1);
        Assert.assertSame(element1, document.coreGetDocumentElement());
        CoreElement element2 = document.coreCreateElement(null, "root2", null);
        element1.coreReplaceWith(element2);
        Assert.assertSame(element2, document.coreGetDocumentElement());
    }
}
