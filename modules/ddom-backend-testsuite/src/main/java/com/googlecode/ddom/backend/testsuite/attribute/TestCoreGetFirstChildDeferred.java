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
package com.googlecode.ddom.backend.testsuite.attribute;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.core.CoreParentNode;

/**
 * Tests that {@link CoreParentNode#coreGetFirstChild()} works correctly on attributes when deferred
 * building is in effect.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetFirstChildDeferred extends BackendTestCase {
    public TestCoreGetFirstChildDeferred(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        // Use namespace unaware parsing to make sure that attributes are built lazily
        CoreElement element = parse("<root a=\'val1\' b=\'val2\'/>", false).coreGetDocumentElement();
        CoreNSUnawareAttribute a = (CoreNSUnawareAttribute)element.coreGetFirstAttribute();
        CoreCharacterData cdata = (CoreCharacterData)a.coreGetFirstChild();
        assertEquals("val1", cdata.coreGetData());
        CoreNSUnawareAttribute b = (CoreNSUnawareAttribute)a.coreGetNextAttribute();
        cdata = (CoreCharacterData)b.coreGetFirstChild();
        assertEquals("val2", cdata.coreGetData());
    }
}
