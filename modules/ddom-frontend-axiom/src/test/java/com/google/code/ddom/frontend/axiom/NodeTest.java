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
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class NodeTest {
    @ValidatedTestResource(reference=LLOMAxiomUtil.class, actual=DDOMAxiomUtil.class)
    private AxiomUtil axiomUtil;
    
    @Validated @Test
    public void testInsertSiblingAfter() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement parent = factory.createOMElement("test", null);
        OMText text1 = factory.createOMText("text1");
        OMText text2 = factory.createOMText("text2");
        parent.addChild(text1);
        text1.insertSiblingAfter(text2);
        Assert.assertSame(parent, text2.getParent());
    }
    
    @Validated @Test
    public void testInsertSiblingBefore() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement parent = factory.createOMElement("test", null);
        OMText text1 = factory.createOMText("text1");
        OMText text2 = factory.createOMText("text2");
        parent.addChild(text1);
        text1.insertSiblingBefore(text2);
        Assert.assertSame(parent, text2.getParent());
        Assert.assertSame(text2, parent.getFirstOMChild());
    }
    
    @Validated @Test(expected=OMException.class)
    public void testInsertSiblingAfterOnOrphan() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMText text = factory.createOMText("test");
        OMElement element = factory.createOMElement("test", null);
        text.insertSiblingAfter(element);
    }

    @Validated @Test(expected=OMException.class)
    public void testInsertSiblingBeforeOnOrphan() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMText text = factory.createOMText("test");
        OMElement element = factory.createOMElement("test", null);
        text.insertSiblingBefore(element);
    }
    
    @Validated @Test(expected=OMException.class)
    public void testInsertSiblingAfterOnSelf() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement parent = factory.createOMElement("test", null);
        OMText text = factory.createOMText("test");
        parent.addChild(text);
        text.insertSiblingAfter(text);
    }

    @Validated @Test(expected=OMException.class)
    public void testInsertSiblingBeforeOnSelf() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement parent = factory.createOMElement("test", null);
        OMText text = factory.createOMText("test");
        parent.addChild(text);
        text.insertSiblingBefore(text);
    }

    // TODO: this actually doesn't throw an exception in Axiom!
    // @Validated
    @Test(expected=OMException.class)
    public void testInsertSiblingAfterOnChild() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement element = factory.createOMElement("test", null);
        OMText text = factory.createOMText("test");
        element.addChild(text);
        text.insertSiblingAfter(element);
    }

    // TODO: this actually doesn't throw an exception in Axiom!
    // @Validated
    @Test(expected=OMException.class)
    public void testInsertSiblingBeforeOnChild() {
        OMFactory factory = axiomUtil.createDocument().getOMFactory();
        OMElement element = factory.createOMElement("test", null);
        OMText text = factory.createOMText("test");
        element.addChild(text);
        text.insertSiblingBefore(element);
    }
}
