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
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreNSAwareNamedNode;

/**
 * Tests the behavior of {@link CoreNSAwareNamedNode#coreGetNamespaceURI()} on an element in state
 * Attributes Pending. In this case, the implementation must make sure that the namespace is
 * correctly resolved.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetNamespaceURIInStateAttributesPending extends BackendTestCase {
    public TestCoreGetNamespaceURIInStateAttributesPending(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreNSAwareElement element = (CoreNSAwareElement)parse("<p:test attr='value' xmlns:p='urn:ns'>text</p:test>", true).coreGetDocumentElement();
        assertEquals("urn:ns", element.coreGetNamespaceURI());
    }
}