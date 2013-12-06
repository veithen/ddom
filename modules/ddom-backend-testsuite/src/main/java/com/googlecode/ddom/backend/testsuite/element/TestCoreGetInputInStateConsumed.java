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
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelStreamException;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.NodeConsumedException;
import com.googlecode.ddom.stream.NullXmlOutput;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.XmlInput;

/**
 * Tests that using {@link CoreParentNode#coreGetInput(boolean)} on an element that has been
 * consumed throws an appropriate exception.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetInputInStateConsumed extends BackendTestCase {
    private final boolean partial;
    private final boolean preserve;
    
    public TestCoreGetInputInStateConsumed(BackendTestSuiteConfig config, boolean partial, boolean preserve) {
        super(config, "partial=" + partial + ",preserve=" + preserve);
        this.partial = partial;
        this.preserve = preserve;
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement element = parse("<root>A<!--B-->C</root>", true).coreGetDocumentElement();
        if (partial) {
            element.coreGetFirstChild();
        }
        // Consume the content of the element
        new Stream(element.coreGetInput(false), new NullXmlOutput()).flush();
        // Now try to serialize the element again
        XmlInput input = element.coreGetInput(preserve);
        try {
            new Stream(input, new NullXmlOutput()).flush();
            fail("Expected StreamException");
        } catch (CoreModelStreamException ex) {
            assertTrue(ex.getCoreModelException() instanceof NodeConsumedException);
        }
    }
}
