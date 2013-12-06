/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
import com.googlecode.ddom.backend.testsuite.StreamAssert;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.stream.Stream;

public class TestCoreGetInputWithDetachedChild extends BackendTestCase {
    private final boolean preserve;
    
    public TestCoreGetInputWithDetachedChild(BackendTestSuiteConfig config, boolean preserve) {
        super(config, "preserve=" + preserve);
        this.preserve = preserve;
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement element = parse("<parent><a>a</a><b>b</b><c>c</c></parent>", true).coreGetDocumentElement();
        element.coreGetFirstChild().coreGetNextSibling().coreDetach();
        StreamAssert output = new StreamAssert();
        new Stream(element.coreGetInput(preserve), output);
        output.assertStartEntity(true);
        output.assertStartElement("", "parent", "");
        output.assertAttributesCompleted();
        output.assertStartElement("", "a", "");
        output.assertAttributesCompleted();
        output.assertCharacterData("a");
        output.assertEndElement();
        output.assertStartElement("", "c", "");
        output.assertAttributesCompleted();
        output.assertCharacterData("c");
        output.assertEndElement();
        output.assertEndElement();
    }
}
