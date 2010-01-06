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

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMText;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class ElementTest {
    @ValidatedTestResource(reference=LLOMAxiomUtil.class, actual=DDOMAxiomUtil.class)
    private AxiomUtil axiomUtil;
    
    @Validated @Test
    public void testGetQNameWithoutNamespace() {
        OMElement element = axiomUtil.createDocument().getOMFactory().createOMElement("name", null);
        QName qname = element.getQName();
        Assert.assertEquals("name", qname.getLocalPart());
        Assert.assertEquals("", qname.getNamespaceURI());
        Assert.assertEquals("", qname.getPrefix());
    }
    
    @Validated @Test
    public void testQNameCaching1() {
        OMElement element = axiomUtil.createDocument().getOMFactory().createOMElement("name", null);
        QName qname = element.getQName();
        Assert.assertSame(qname, element.getQName());
    }
    
//    @Validated @Test
//    public void testQNameCaching2() {
//        QName qname = new QName("test", "urn:test", "t");
//        OMElement element = axiomUtil.createDocument().getOMFactory().createOMElement(qname);
//        Assert.assertSame(qname, element.getQName());
//    }
    
    @Validated @Test
    public void testSetText() {
        OMElement element = axiomUtil.createDocument().getOMFactory().createOMElement("test", null);
        element.setText("text");
        OMNode child = element.getFirstOMChild();
        Assert.assertTrue(child instanceof OMText);
        Assert.assertSame(element, child.getParent());
        Assert.assertEquals("text", ((OMText)child).getText());
        Assert.assertNull(child.getNextOMSibling());
    }
    
    /**
     * Test {@link OMElement#getAllAttributes()} on a programmatically created document.
     */
    @Validated @Test
    public void testGetAllAttributes1() {
        OMElement element = axiomUtil.createDocument().getOMFactory().createOMElement("test", null);
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

    /**
     * Test {@link OMElement#getAllAttributes()} on a parsed document. Also check that the iterator
     * doesn't attempt to return namespace declarations.
     */
    @Validated @Test
    public void testGetAllAttributes2() {
        OMElement element = axiomUtil.parse("<e xmlns:p='urn:test' p:attr='test'/>").getOMDocumentElement();
        Iterator it = element.getAllAttributes();
        Assert.assertTrue(it.hasNext());
        OMAttribute attr = (OMAttribute)it.next();
        Assert.assertEquals("urn:test", attr.getNamespace().getNamespaceURI());
        Assert.assertEquals("attr", attr.getLocalName());
        Assert.assertFalse(it.hasNext());
    }
    
    @Validated @Test
    public void testGetAllDeclaredNamespaces() {
        OMElement element = axiomUtil.parse("<e xmlns:p='urn:test' p:attr='test'/>").getOMDocumentElement();
        Iterator it = element.getAllDeclaredNamespaces();
        Assert.assertTrue(it.hasNext());
        OMNamespace ns = (OMNamespace)it.next();
        Assert.assertEquals("p", ns.getPrefix());
        Assert.assertEquals("urn:test", ns.getNamespaceURI());
        Assert.assertFalse(it.hasNext());
    }
}
