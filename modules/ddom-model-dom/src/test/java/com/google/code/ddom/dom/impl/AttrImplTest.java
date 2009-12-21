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
package com.google.code.ddom.dom.impl;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;

@RunWith(DOMTestRunner.class)
public class AttrImplTest {
    @Validated @Test
    public void testGetNextSibling() {
        Document doc = DOMUtil.newDocument();
        Element element = doc.createElement("test");
        element.setAttribute("attr1", "val");
        element.setAttribute("attr2", "val");
        Attr attr = (Attr)element.getAttributes().item(0);
        // The attributes of an element are not siblings of each other, so getNextSibling must return null
        Assert.assertNull(attr.getNextSibling());
    }
    
    @Validated @Test
    public void testGetLocalNameAfterCreateAttributeWithPrefix() {
        Document doc = DOMUtil.newDocument();
        Attr attr = doc.createAttribute("p:name");
        Assert.assertNull(attr.getLocalName());
    }
    
    @Validated @Test
    public void testSetPrefixAfterCreateAttributeWithoutPrefix() {
        Document doc = DOMUtil.newDocument();
        Attr attr = doc.createAttribute("name");
        try {
            attr.setPrefix("p");
            Assert.fail("Exception expected");
        } catch (DOMException ex) {
            Assert.assertEquals(DOMException.NAMESPACE_ERR, ex.code);
        }
    }
    
    @Validated @Test
    public void testGetValueWithMultipleChildren() {
        Document doc = DOMUtil.newDocument();
        Attr attr = doc.createAttribute("name");
        attr.setValue("te");
        attr.appendChild(doc.createTextNode("st"));
        Assert.assertEquals("test", attr.getValue());
    }
}
