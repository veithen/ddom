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
import com.googlecode.ddom.backend.testsuite.NSAwareAttributeMatcher;
import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.TextCollectorPolicy;

/**
 * Tests that {@link CoreElement#coreGetAttribute(AttributeMatcher, String, String)} causes
 * expansion of the element when it is in state "Content set".
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetAttributeInStateSourceSet extends BackendTestCase {
    public TestCoreGetAttributeInStateSourceSet(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement element = nodeFactory.createElement(null, "", "test", "");
        element.coreSetSource(toXmlSource("<test attr1='value1' attr2='value2'>text</test>", true, true));
        CoreAttribute attr = element.coreGetAttribute(NSAwareAttributeMatcher.INSTANCE, "", "attr2");
        assertNotNull(attr);
        assertEquals("attr2", ((CoreNSAwareAttribute)attr).coreGetLocalName());
        assertEquals("value2", attr.coreGetTextContent(TextCollectorPolicy.DEFAULT));
    }
}
