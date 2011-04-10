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
package com.googlecode.ddom.backend.testsuite.element;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.backend.testsuite.StreamAssert;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.stream.Stream;

/**
 * Tests the behavior of {@link CoreParentNode#coreGetInput(boolean)} if one of the children is in
 * state "Value set". This means that
 * {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)} has been called on the
 * child node, but the content of that node has not been accessed yet.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetInputWithChildInStateSourceSet extends BackendTestCase {
    private final boolean preserve;
    private final boolean destructive;
    
    public TestCoreGetInputWithChildInStateSourceSet(BackendTestSuiteConfig config, boolean preserve, boolean destructive) {
        super(config, "preserve=" + preserve + ", destructive=" + destructive);
        this.preserve = preserve;
        this.destructive = destructive;
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement parent = nodeFactory.createElement(null, "", "parent", "");
        CoreElement child = parent.coreAppendElement("", "child", "");
        child.coreSetSource(toXmlSource("<child>text</child>", destructive));
        StreamAssert output = new StreamAssert();
        new Stream(parent.coreGetInput(preserve), output);
        output.assertStartEntity(true);
        output.assertStartElement("", "parent", "");
        output.assertStartElement("", "child", "");
        output.assertCharacterData("text");
        output.assertEndElement();
        output.assertEndElement();
        if (preserve && destructive) {
            assertTrue(child.coreIsComplete());
        } else {
            assertFalse(child.coreIsComplete());
        }
    }
}
