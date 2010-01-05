/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.backend.testsuite.nsaware;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreNSAwareNamedNode;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.backend.testsuite.BackendTestCase;

public class TestCoreGetPrefixCanonicalization extends BackendTestCase {
    private final NSAwareNamedNodeFactory namedNodeFactory;
    
    public TestCoreGetPrefixCanonicalization(NodeFactory nodeFactory, NSAwareNamedNodeFactory namedNodeFactory) {
        super(nodeFactory, namedNodeFactory.getNodeClass().getSimpleName());
        this.namedNodeFactory = namedNodeFactory;
    }

    @Override
    protected void runTest() throws Throwable {
        CoreDocument document = nodeFactory.createDocument(null);
        String prefix = "p";
        CoreNSAwareNamedNode node1 = namedNodeFactory.create(document, "urn:test", "test", new String(prefix));
        CoreNSAwareNamedNode node2 = namedNodeFactory.create(document, "urn:test", "test", new String(prefix));
        assertSame(node1.coreGetPrefix(), node2.coreGetPrefix());
        assertSame(document.getSymbols().getSymbol(prefix), node1.coreGetPrefix());
    }
}
