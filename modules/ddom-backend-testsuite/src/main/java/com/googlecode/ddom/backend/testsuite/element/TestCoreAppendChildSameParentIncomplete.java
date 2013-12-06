/*
 * Copyright 2009-2013 Andreas Veithen
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
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.NodeMigrationPolicy;

/**
 * Tests the behavior of {@link CoreParentNode#coreAppendChild(CoreChildNode, NodeMigrationPolicy)}
 * when invoked on an incomplete element and if the node to be added is already a child of the
 * element.
 * 
 * @author Andreas Veithen
 */
public class TestCoreAppendChildSameParentIncomplete extends BackendTestCase {
    public TestCoreAppendChildSameParentIncomplete(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = parse("<parent><a/><b/><c/></parent>");
        CoreNSAwareElement parent = (CoreNSAwareElement)document.coreGetDocumentElement();
        CoreNSAwareElement a = (CoreNSAwareElement)parent.coreGetFirstChild();
        CoreNSAwareElement b = (CoreNSAwareElement)a.coreGetNextSibling();
        parent.coreAppendChild(b, Policies.MOVE);
        CoreNSAwareElement nextSibling = (CoreNSAwareElement)a.coreGetNextSibling();
        assertEquals("c", nextSibling.coreGetLocalName());
        nextSibling = (CoreNSAwareElement)nextSibling.coreGetNextSibling();
        assertSame(b, nextSibling);
    }
}
