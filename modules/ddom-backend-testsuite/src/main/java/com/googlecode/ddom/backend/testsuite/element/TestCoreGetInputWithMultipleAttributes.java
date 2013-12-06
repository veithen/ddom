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
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.stream.Stream;

/**
 * Tests {@link CoreParentNode#coreGetInput(boolean)} on an element with multiple attributes where
 * deferred building is in effect.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetInputWithMultipleAttributes extends BackendTestCase {
    private final boolean preserve;
    
    public TestCoreGetInputWithMultipleAttributes(BackendTestSuiteConfig config, boolean preserve) {
        super(config, "preserve=" + preserve);
        this.preserve = preserve;
    }

    @Override
    protected void runTest() throws Throwable {
        // Use namespace unaware parsing to make sure that attributes are built lazily
        CoreElement element = parse("<root a=\'val1\' b=\'val2\'/>", false).coreGetDocumentElement();
        StreamAssert output = new StreamAssert();
        new Stream(element.coreGetInput(preserve), output);
        output.assertStartEntity(true);
        output.assertStartElement("root");
        output.assertStartAttribute("a");
        output.assertCharacterData("val1");
        output.assertEndAttribute();
        output.assertStartAttribute("b");
        output.assertCharacterData("val2");
        output.assertEndAttribute();
        output.assertAttributesCompleted();
        output.assertEndElement();
    }
}