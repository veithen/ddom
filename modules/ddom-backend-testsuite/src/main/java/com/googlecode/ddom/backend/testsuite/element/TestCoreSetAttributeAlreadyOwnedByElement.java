/*
 * Copyright 2009-2011,2013-2014 Andreas Veithen
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
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreElement.ReturnValue;
import com.googlecode.ddom.core.NodeMigrationPolicy;

/**
 * Tests that
 * {@link CoreElement#coreSetAttribute(AttributeMatcher, String, String, CoreAttribute, NodeMigrationPolicy)}
 * is a no-op if the specified attribute is already owned by the element.
 */
public class TestCoreSetAttributeAlreadyOwnedByElement extends BackendTestCase {
    public TestCoreSetAttributeAlreadyOwnedByElement(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument();
        CoreElement element = nodeFactory.createElement(document, "urn:ns", "test", "ns");
        CoreAttribute attribute = nodeFactory.createAttribute(document, "", "attr", "", "value", "CDATA");
        element.coreAppendAttribute(attribute, Policies.REJECT);
        element.coreSetAttribute(Policies.NSAWARE_ATTRIBUTE_MATCHER, attribute, Policies.REJECT, false, null, ReturnValue.NONE);
    }
}
