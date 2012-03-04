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

import javax.xml.XMLConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class NamespaceDeclarationTest {
    @ValidatedTestResource(reference=XercesDOMUtil.class, actual=DDOMUtil.class)
    private DOMUtil domUtil;
    
    @Validated @Test
    public void testGetNamespaceURI() {
        Document doc = domUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertEquals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, nsAttr.getNamespaceURI());
    }

    @Validated @Test
    public void testGetLocalName() {
        Document doc = domUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertEquals("p", nsAttr.getLocalName());
    }

    @Validated @Test
    public void testGetPrefix() {
        Document doc = domUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getPrefix());
    }

    @Validated @Test
    public void testGetName() {
        Document doc = domUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertEquals("xmlns:p", nsAttr.getName());
    }

    @Validated @Test
    public void testGetNamespaceURIForDefaultNamespace() {
        Document doc = domUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertEquals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, nsAttr.getNamespaceURI());
    }

    @Validated @Test
    public void testGetLocalNameForDefaultNamespace() {
        Document doc = domUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getLocalName());
    }

    @Validated @Test
    public void testGetPrefixForDefaultNamespace() {
        Document doc = domUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertNull(nsAttr.getPrefix());
    }

    @Validated @Test
    public void testGetNameForDefaultNamespace() {
        Document doc = domUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getName());
    }
}
