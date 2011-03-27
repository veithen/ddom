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
package com.googlecode.ddom.backend.testsuite.element;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

public class TestCoreSetSourceWithPushInput extends BackendTestCase {
    static class PushInput extends XmlInput {
        @Override
        protected void proceed(boolean flush) throws StreamException {
            XmlHandler handler = getHandler();
            handler.startElement("urn:ns", "root", "p");
            handler.startAttribute("", "attr", "", "CDATA");
            handler.processCharacterData("value", false);
            handler.endAttribute();
            handler.attributesCompleted();
            handler.processCharacterData("content", false);
            handler.endElement();
            handler.completed();
        }

        @Override
        public void dispose() {
        }
    }
    
    static class PushSource implements XmlSource {
        public XmlInput getInput(Hints hints) {
            return new PushInput();
        }

        public boolean isDestructive() {
            return false;
        }
    }
    
    public TestCoreSetSourceWithPushInput(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreNSAwareElement element = nodeFactory.createElement(null, null, null, null);
        element.coreSetSource(new PushSource());
        CoreChildNode child = element.coreGetFirstChild();
        assertTrue(child instanceof CoreCharacterData);
        assertEquals("content", ((CoreCharacterData)child).coreGetData());
        assertTrue(element.coreIsComplete());
    }
}
