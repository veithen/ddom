/*
 * Copyright 2013 Andreas Veithen
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
package com.googlecode.ddom.backend.testsuite.element;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreNSAwareElement;

public class TestCoreSetSourceWithExtraContent extends BackendTestCase {
    public TestCoreSetSourceWithExtraContent(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        document.coreSetXmlVersion("1.1");
        document.coreSetXmlEncoding("utf-8");
        document.coreSetStandalone(Boolean.FALSE);
        CoreNSAwareElement element = document.coreAppendElement("urn:ns", "test", "p");
        element.coreSetSource(toXmlSource("<?xml version='1.0' encoding='ascii'?> <!--comment--> <?pi test?> <p:test xmlns:p='urn:ns'>text</p:test> <!--comment-->", true, true));
        CoreChildNode child = element.coreGetFirstChild();
        assertTrue(child instanceof CoreCharacterData);
        assertEquals("text", ((CoreCharacterData)child).coreGetData());
        assertNull(child.coreGetNextSibling());
        // coreSetSource should not have messed with the properties of the document
        assertEquals("1.1", document.coreGetXmlVersion());
        assertEquals("utf-8", document.coreGetXmlEncoding());
        assertEquals(Boolean.FALSE, document.coreGetStandalone());
    }
}
