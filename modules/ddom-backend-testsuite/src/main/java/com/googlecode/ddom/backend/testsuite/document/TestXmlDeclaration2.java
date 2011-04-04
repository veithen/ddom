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

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.CoreDocument;

public class TestXmlDeclaration2 extends BackendTestCase {
    public TestXmlDeclaration2(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Exception {
        CoreDocument document = nodeFactory.createDocument();
        document.coreSetXmlVersion("1.1");
        document.coreSetXmlEncoding("UTF-8");
        document.coreSetStandalone(null);
        document.coreSetContent(toXmlSource("<?xml version='1.0' standalone='yes'?><root/>"));
        assertEquals("1.0", document.coreGetXmlVersion());
        assertNull(document.coreGetXmlEncoding());
        assertEquals(Boolean.TRUE, document.coreGetStandalone());
    }
}
