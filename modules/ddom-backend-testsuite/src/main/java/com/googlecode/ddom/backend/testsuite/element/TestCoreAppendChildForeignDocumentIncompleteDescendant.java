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
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.NodeMigrationPolicy;

/**
 * Tests the behavior of {@link CoreParentNode#coreAppendChild(CoreChildNode, NodeMigrationPolicy)}
 * when the node to be added is from a different document and has a descendant that is incomplete.
 * 
 * @author Andreas Veithen
 */
public class TestCoreAppendChildForeignDocumentIncompleteDescendant extends BackendTestCase {
    public TestCoreAppendChildForeignDocumentIncompleteDescendant(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument foreignDocument = nodeFactory.createDocument();
        CoreNSAwareElement wrapper = nodeFactory.createElement(foreignDocument, "urn:test", "test", "p");
        CoreNSAwareElement descendant = wrapper.coreAppendElement(null, null, null);
        descendant.coreSetSource(toXmlSource("<imported>text</imported>", true, true));
        // This will make sure that the descendant node transitions from state source set to incomplete
        assertEquals("imported", descendant.coreGetLocalName());
        CoreElement element = nodeFactory.createElement(null, "", "root", "");
        element.coreAppendChild(wrapper, Policies.MOVE);
        CoreNSAwareElement child = (CoreNSAwareElement)element.coreGetFirstChild();
        assertSame(descendant, (CoreNSAwareElement)child.coreGetFirstChild());
        CoreCharacterData text = (CoreCharacterData)descendant.coreGetFirstChild();
        assertEquals("text", text.coreGetData());
        assertNull(text.coreGetNextSibling());
        assertNull(descendant.coreGetNextSibling());
        assertNull(child.coreGetNextSibling());
    }
}
