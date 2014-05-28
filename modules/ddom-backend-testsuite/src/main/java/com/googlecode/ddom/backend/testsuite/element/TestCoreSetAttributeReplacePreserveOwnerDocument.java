/*
 * Copyright 2013-2014 Andreas Veithen
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
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreElement.ReturnValue;
import com.googlecode.ddom.core.NodeMigrationPolicy;

/**
 * Tests that
 * {@link CoreElement#coreSetAttribute(AttributeMatcher, String, String, CoreAttribute, NodeMigrationPolicy)}
 * preserves the owner document of the replaced attribute if told to do so.
 */
public class TestCoreSetAttributeReplacePreserveOwnerDocument extends BackendTestCase {
    public TestCoreSetAttributeReplacePreserveOwnerDocument(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        // Note: we don't create the owner document, because this is the interesting case
        CoreElement element = nodeFactory.createElement(null, "urn:ns", "test", "ns");
        element.coreSetAttribute(Policies.NSAWARE_ATTRIBUTE_MATCHER, "", "attr", "", "old");
        CoreAttribute orgAttribute = element.coreGetFirstAttribute();
        CoreAttribute newAttribute = nodeFactory.createAttribute(null, "", "attr", "", "new", "CDATA");
        CoreAttribute replacedAttribute = element.coreSetAttribute(Policies.NSAWARE_ATTRIBUTE_MATCHER, newAttribute, Policies.MOVE, false, null, ReturnValue.REPLACED_ATTRIBUTE);
        assertSame(orgAttribute, replacedAttribute);
        assertSame(element.coreGetOwnerDocument(true), orgAttribute.coreGetOwnerDocument(true));
    }
}
