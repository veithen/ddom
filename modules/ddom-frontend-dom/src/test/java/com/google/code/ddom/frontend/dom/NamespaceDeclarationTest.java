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
package com.google.code.ddom.frontend.dom;

import javax.xml.XMLConstants;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

import com.google.code.ddom.utils.test.Validated;

@RunWith(DOMTestRunner.class)
public class NamespaceDeclarationTest {
    @Validated @Test
    public void testGetNamespaceURI() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, nsAttr.getNamespaceURI());
    }

    @Validated @Test
    public void testGetLocalName() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals("p", nsAttr.getLocalName());
    }

    @Validated @Test
    public void testGetPrefix() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getPrefix());
    }

    @Validated @Test
    public void testGetName() {
        Document doc = DOMUtil.parse(true, "<p:test xmlns:p='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals("xmlns:p", nsAttr.getName());
    }

    @Validated @Test
    public void testGetNamespaceURIForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, nsAttr.getNamespaceURI());
    }

    @Validated @Test
    public void testGetLocalNameForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getLocalName());
    }

    @Validated @Test
    public void testGetPrefixForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertNull(nsAttr.getPrefix());
    }

    @Validated @Test
    public void testGetNameForDefaultNamespace() {
        Document doc = DOMUtil.parse(true, "<test xmlns='urn:ns'/>");
        Attr nsAttr = (Attr)doc.getDocumentElement().getAttributes().item(0);
        Assert.assertEquals(XMLConstants.XMLNS_ATTRIBUTE, nsAttr.getName());
    }
}