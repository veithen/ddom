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
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.XmlSource;

/**
 * Tests that {@link CoreParentNode#coreSetContent(XmlSource)} doesn't invoke
 * {@link XmlSource#getInput()} immediately, but that the source is expanded lazily.
 * 
 * @author Andreas Veithen
 */
public class TestCoreSetContentLaziness extends BackendTestCase {
    public TestCoreSetContentLaziness(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        XmlSource orgSource = toXmlSource("<test/>");
        SimpleXmlSource simpleSource = new SimpleXmlSource(orgSource.getInput(XmlSource.Hints.DEFAULTS));
        CoreElement parent = nodeFactory.createElement(null, "parent");
        parent.coreSetContent(simpleSource);
        assertFalse(simpleSource.isAccessed());
        CoreChildNode child = parent.coreGetFirstChild();
        assertTrue(simpleSource.isAccessed());
        assertTrue(child instanceof CoreElement);
    }
}
