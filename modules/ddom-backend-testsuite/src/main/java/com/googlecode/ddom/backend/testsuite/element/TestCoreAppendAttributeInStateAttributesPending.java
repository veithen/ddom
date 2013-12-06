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
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.core.NodeMigrationPolicy;

/**
 * Tests that {@link CoreElement#coreAppendAttribute(CoreAttribute, NodeMigrationPolicy)} adds the
 * new attribute in the right place if the existing attributes have not been built yet.
 * 
 * @author Andreas Veithen
 */
public class TestCoreAppendAttributeInStateAttributesPending extends BackendTestCase {
    public TestCoreAppendAttributeInStateAttributesPending(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = parse("<root a='test'/>", false);
        CoreElement element = document.coreGetDocumentElement();
        CoreNSUnawareAttribute b = nodeFactory.createAttribute(document, "b", "test", "CDATA");
        element.coreAppendAttribute(b, Policies.REQUIRE_SAME_DOCUMENT);
        CoreNSUnawareAttribute first = (CoreNSUnawareAttribute)element.coreGetFirstAttribute();
        assertEquals("a", first.coreGetName());
        assertSame(b, first.coreGetNextAttribute());
    }
}
