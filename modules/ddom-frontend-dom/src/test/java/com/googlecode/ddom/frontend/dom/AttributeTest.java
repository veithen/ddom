/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.frontend.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class AttributeTest {
    @ValidatedTestResource(reference=XercesDOMUtil.class, actual=DDOMUtil.class)
    private DOMUtil domUtil;
    
    @Validated @Test
    public void testGetNextSibling() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElement("test");
        element.setAttribute("attr1", "val");
        element.setAttribute("attr2", "val");
        Attr attr = (Attr)element.getAttributes().item(0);
        // The attributes of an element are not siblings of each other, so getNextSibling must return null
        assertNull(attr.getNextSibling());
    }
    
    @Validated @Test
    public void testGetLocalNameAfterCreateAttributeWithPrefix() {
        Document doc = domUtil.newDocument();
        Attr attr = doc.createAttribute("p:name");
        assertNull(attr.getLocalName());
    }
    
    @Validated @Test
    public void testSetPrefixAfterCreateAttributeWithoutPrefix() {
        Document doc = domUtil.newDocument();
        Attr attr = doc.createAttribute("name");
        try {
            attr.setPrefix("p");
            fail("Exception expected");
        } catch (DOMException ex) {
            assertEquals(DOMException.NAMESPACE_ERR, ex.code);
        }
    }
    
    @Validated @Test
    public void testGetValueWithMultipleChildren() {
        Document doc = domUtil.newDocument();
        Attr attr = doc.createAttribute("name");
        attr.setValue("te");
        attr.appendChild(doc.createTextNode("st"));
        assertEquals("test", attr.getValue());
    }
}
