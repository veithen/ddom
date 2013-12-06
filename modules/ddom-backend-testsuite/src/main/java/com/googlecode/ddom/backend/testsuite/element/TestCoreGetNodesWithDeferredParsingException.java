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

import java.util.Iterator;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelRuntimeException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ExceptionTranslator;
import com.googlecode.ddom.core.Selector;

public class TestCoreGetNodesWithDeferredParsingException extends BackendTestCase {
    public TestCoreGetNodesWithDeferredParsingException(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreElement element = parse("<root><a></invalid></root>", true).coreGetDocumentElement();
        Iterator<CoreChildNode> it = element.coreGetNodes(Axis.CHILDREN, Selector.ANY, CoreChildNode.class, ExceptionTranslator.DEFAULT);
        try {
            while (it.hasNext()) {
                it.next();
            }
            fail("Expected CoreModelRuntimeException");
        } catch (CoreModelRuntimeException ex) {
            assertTrue(ex.getCoreModelException() instanceof DeferredParsingException);
        }
    }
}
