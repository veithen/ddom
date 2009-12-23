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

import java.util.Iterator;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;

@RunWith(AxiomTestRunner.class)
public class ElementTest {
    @Validated @Test
    public void testSetText() {
        OMElement element = AxiomUtil.createDocument().getOMFactory().createOMElement("test", null);
        element.setText("text");
        OMNode child = element.getFirstOMChild();
        Assert.assertTrue(child instanceof OMText);
        Assert.assertSame(element, child.getParent());
        Assert.assertEquals("text", ((OMText)child).getText());
        Assert.assertNull(child.getNextOMSibling());
    }
    
    @Validated @Test
    public void testGetAllAttributes() {
        OMElement element = AxiomUtil.createDocument().getOMFactory().createOMElement("test", null);
        element.addAttribute("attr1", "value1", null);
        element.addAttribute("attr2", "value2", null);
        Iterator it = element.getAllAttributes();
        Assert.assertTrue(it.hasNext());
        OMAttribute attr = (OMAttribute)it.next();
        Assert.assertEquals("attr1", attr.getLocalName());
        Assert.assertTrue(it.hasNext());
        attr = (OMAttribute)it.next();
        Assert.assertEquals("attr2", attr.getLocalName());
        Assert.assertFalse(it.hasNext());
    }
}
