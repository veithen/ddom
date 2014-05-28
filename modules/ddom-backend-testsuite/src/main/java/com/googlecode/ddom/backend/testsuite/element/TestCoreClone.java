/*
 * Copyright 2009-2011,2014 Andreas Veithen
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

import javax.xml.namespace.QName;

import com.googlecode.ddom.backend.testsuite.BackendTestCase;
import com.googlecode.ddom.backend.testsuite.BackendTestSuiteConfig;
import com.googlecode.ddom.backend.testsuite.Policies;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreNode;
import com.googlecode.ddom.core.TextCollectorPolicy;

public class TestCoreClone extends BackendTestCase {
    public TestCoreClone(BackendTestSuiteConfig config) {
        super(config);
    }

    @Override
    protected void runTest() throws Throwable {
        CoreNSAwareElement element = nodeFactory.createElement(null, "urn:test", "test", "p");
        element.coreSetAttribute(Policies.NSAWARE_ATTRIBUTE_MATCHER, "", "attr", "", "value");
        element.coreAppendComment("test comment");
        element.coreAppendElement("", "child", "");
        CoreNode clone = element.coreClone(new ClonePolicy() {
            public boolean cloneAttributes() {
                return true;
            }
            
            public boolean cloneChildren(int nodeType) {
                return true;
            }
        });
        assertTrue(clone instanceof CoreNSAwareElement);
        CoreNSAwareElement element2 = (CoreNSAwareElement)clone;
        CoreAttribute attr = element2.coreGetFirstAttribute();
        assertTrue(attr instanceof CoreNSAwareAttribute);
        assertEquals(new QName("attr"), ((CoreNSAwareAttribute)attr).coreGetQName());
        assertEquals("value", attr.coreGetTextContent(TextCollectorPolicy.DEFAULT));
        assertNull(attr.coreGetNextAttribute());
        CoreChildNode child = element2.coreGetFirstChild();
        assertTrue(child instanceof CoreComment);
        assertEquals("test comment", ((CoreComment)child).coreGetTextContent(TextCollectorPolicy.DEFAULT));
        child = child.coreGetNextSibling();
        assertTrue(child instanceof CoreNSAwareElement);
        assertEquals(new QName("child"), ((CoreNSAwareElement)child).coreGetQName());
        assertNull(child.coreGetNextSibling());
    }
}
