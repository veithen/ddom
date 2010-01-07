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
package com.google.code.ddom.frontend.saaj;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Element;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class SOAPElementTest {
    @ValidatedTestResource(reference=RISAAJUtil.class, actual=DDOMSAAJUtil.class)
    private SAAJUtil saajUtil;

    /**
     * Test that a namespace declaration added using
     * {@link SOAPElement#addNamespaceDeclaration(String, String)} can be retrieved using
     * {@link Element#getAttributeNS(String, String)}.
     * 
     * @throws SOAPException
     */
    @Validated @Test
    public void testAddNamespaceDeclaration() throws SOAPException {
        SOAPElement element = saajUtil.createSOAPElement("urn:test", "test", "p");
        element.addNamespaceDeclaration("ns", "urn:ns");
        Assert.assertEquals("urn:ns", element.getAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "ns"));
    }

    /**
     * Test that a call do {@link SOAPElement#addNamespaceDeclaration(String, String)} for a prefix
     * that is already declared on the same element will replace the existing namespace declaration.
     * 
     * @throws SOAPException
     */
    @Validated @Test
    public void testAddNamespaceDeclarationForExistingPrefix() throws SOAPException {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.addNamespaceDeclaration("p", "urn:ns1");
        element.addNamespaceDeclaration("p", "urn:ns2");
        Assert.assertEquals("urn:ns2", element.lookupNamespaceURI("p"));
        Assert.assertNull(element.lookupPrefix("urn:ns1"));
        Assert.assertEquals("p", element.lookupPrefix("urn:ns2"));
    }
    
    /**
     * Test that {@link SOAPElement#getAllAttributes()} returns the correct {@link Name} for an
     * attribute with a namespace.
     */
    @Validated @Test
    public void testGetAllAttributesWithNamespace() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.setAttributeNS("urn:ns", "p:test", "value");
        Iterator it = element.getAllAttributes();
        Assert.assertTrue(it.hasNext());
        Name name = (Name)it.next();
        Assert.assertEquals("urn:ns", name.getURI());
        Assert.assertEquals("p", name.getPrefix());
        Assert.assertEquals("test", name.getLocalName());
        Assert.assertEquals("p:test", name.getQualifiedName());
    }
    
    /**
     * Test that {@link SOAPElement#getAllAttributes()} returns the correct {@link Name} for an
     * attribute without namespace.
     */
    @Validated @Test
    public void testGetAllAttributesWithoutNamespace() {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        element.setAttributeNS(null, "test", "value");
        Iterator it = element.getAllAttributes();
        Assert.assertTrue(it.hasNext());
        Name name = (Name)it.next();
        Assert.assertTrue(StringUtils.isEmpty(name.getURI()));
        Assert.assertTrue(StringUtils.isEmpty(name.getPrefix()));
        Assert.assertEquals("test", name.getLocalName());
        Assert.assertEquals("test", name.getQualifiedName());
    }
}
