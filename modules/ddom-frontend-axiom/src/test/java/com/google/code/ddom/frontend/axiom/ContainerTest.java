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
package com.google.code.ddom.frontend.axiom;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class ContainerTest {
    @ValidatedTestResource(reference=LLOMAxiomUtil.class, actual=DDOMAxiomUtil.class)
    private AxiomUtil axiomUtil;
    
    @Validated @Test
    public void testAddChild() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement element = factory.createOMElement("test", null);
        OMText text = factory.createOMText("test");
        element.addChild(text);
        Assert.assertSame(element, text.getParent());
        Assert.assertSame(text, element.getFirstOMChild());
    }
    
    /**
     * Test that {@link org.apache.axiom.om.OMContainer#addChild(org.apache.axiom.om.OMNode)}
     * behaves correctly if the child to be added already has a parent.
     */
    // TODO: update Javadoc of OMContainer
    @Validated @Test
    public void testAddChildWithParent() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement element1 = factory.createOMElement("test1", null);
        OMElement element2 = factory.createOMElement("test2", null);
        OMText text = factory.createOMText("test");
        element1.addChild(text);
        element2.addChild(text);
        Assert.assertSame(element2, text.getParent());
        Assert.assertNull(element1.getFirstOMChild());
        Assert.assertSame(text, element2.getFirstOMChild());
    }
}
