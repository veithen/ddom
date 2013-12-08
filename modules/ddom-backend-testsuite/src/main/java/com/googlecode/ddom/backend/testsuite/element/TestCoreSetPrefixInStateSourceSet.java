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
import com.googlecode.ddom.backend.testsuite.StreamAssert;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.stream.Stream;

public class TestCoreSetPrefixInStateSourceSet extends BackendTestCase {
    public TestCoreSetPrefixInStateSourceSet(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreNSAwareElement element = nodeFactory.createElement(null, null, null, null);
        element.coreSetSource(toXmlSource("<p:test xmlns:p='urn:ns'/>", true, true));
        element.coreSetPrefix("other");
        StreamAssert output = new StreamAssert();
        new Stream(element.coreGetInput(true), output);
        output.assertStartEntity(true);
        output.assertStartElement("urn:ns", "test", "other");
        output.assertNamespaceDeclaration("p", "urn:ns");
        output.assertAttributesCompleted();
        output.assertEndElement();
        output.assertCompleted();
    }
}