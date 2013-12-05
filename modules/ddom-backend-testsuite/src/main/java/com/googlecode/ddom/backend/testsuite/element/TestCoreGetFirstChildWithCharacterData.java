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

import java.io.StringReader;

import org.w3c.dom.CharacterData;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.backend.testsuite.Event;
import com.googlecode.ddom.backend.testsuite.EventTracker;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.parser.Parser;

/**
 * Tests that {@link CoreParentNode#coreGetFirstChild()} only reads the character data if the node
 * is an element that has a text node as first child.
 * <p>
 * Rationale: the support for compact parent nodes implies that normally the {@link CharacterData}
 * node is only created when the builder determines that there are additional child nodes. However,
 * when {@link CoreParentNode#coreGetFirstChild()} is used, then the {@link CharacterData} node
 * needs to be created anyway and it is not necessary to read beyond the text node.
 * 
 * @author Andreas Veithen
 */
public class TestCoreGetFirstChildWithCharacterData extends BackendTestCase {
    public TestCoreGetFirstChildWithCharacterData(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        XmlInput input = new Parser(new StringReader("<a>B<!--C--></a>"), false);
        EventTracker eventTracker = new EventTracker();
        input.addFilter(eventTracker);
        CoreDocument document = nodeFactory.createDocument();
        document.coreSetContent(new SimpleXmlSource(input));
        CoreElement element = document.coreGetDocumentElement();
        element.coreGetFirstChild();
        assertEquals(Event.CHARACTER_DATA, eventTracker.getLastEvent());
    }
}
