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
package com.google.code.ddom.backend.testsuite.element;

import com.google.code.ddom.backend.testsuite.BackendTestCase;
import com.google.code.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.google.code.ddom.backend.testsuite.NSAwareAttributeMatcher;
import com.google.code.ddom.backend.testsuite.Policies;
import com.google.code.ddom.core.CoreAttribute;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreElement;

/**
 * Tests that
 * {@link CoreElement#coreSetAttribute(com.google.code.ddom.core.AttributeMatcher, String, String, CoreAttribute, com.google.code.ddom.core.NodeMigrationPolicy)}
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
        CoreAttribute attribute = nodeFactory.createAttribute(document, null, "attr", null, "value", "CDATA");
        element.coreAppendAttribute(attribute, Policies.REJECT);
        element.coreSetAttribute(NSAwareAttributeMatcher.INSTANCE, null, "attr", attribute, Policies.REJECT);
    }
}
