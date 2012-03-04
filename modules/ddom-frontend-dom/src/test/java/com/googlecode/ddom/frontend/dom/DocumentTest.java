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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.xml.XMLConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.core.CoreNSUnawareElement;

/**
 * @author Andreas Veithen
 */
@RunWith(ValidatedTestRunner.class)
public class DocumentTest {
    @ValidatedTestResource(reference=XercesDOMUtil.class, actual=DDOMUtil.class)
    private DOMUtil domUtil;
    
    @Test
    public void testNamespaceUnawareParsing() throws Exception {
        Document doc = DDOMUtil.INSTANCE.parse(false, "<p:root xmlns:p='urn:ns'/>");
        
        Element element = doc.getDocumentElement();
        assertTrue(element instanceof CoreNSUnawareElement);
        assertNull(element.getLocalName());
        assertEquals("p:root", element.getTagName());
        
        Attr attr = (Attr)element.getAttributes().item(0);
        assertTrue(attr instanceof CoreNSUnawareAttribute);
        assertNull(attr.getLocalName());
        assertEquals("xmlns:p", attr.getName());
    }
    
    /**
     * Test that the implementation behaves gracefully after a parse error. In particular, after
     * the first parse error has occurred, the implementation must not try to access the parser
     * again. The reason is that in some StAX implementations, a parse error leaves the parser in
     * an inconsistent state.
     */
//    @Test
    // TODO: this should go to ddom-backend-testsuite
//    public void testGracefulBehaviorAfterParseError() throws Exception {
//        // TODO: do this properly (we should not allow passing XMLStreamReader instances to DeferredDocumentFactory, because we don't have control over the properties)
//        InvocationCounter cter = new InvocationCounter();
//        XMLInputFactory factory = XMLInputFactory.newInstance();
//        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader("<root>This is malformed"));
//        Document doc = (Document)DocumentHelperFactory.INSTANCE.newInstance().parse("dom", cter.createProxy(XMLStreamReader.class, reader));
//        
//        // First loop over the document; this should give an exception
//        try {
//            for (Node node : DOM.descendants(doc)) {
//            }
//            Assert.fail("Expected DOMDeferredParsingException");
//        } catch (DOMDeferredParsingException ex) {
//            // Expected
//        }
//        // This exception is a result of an exception thrown by the StAX parser
//        Assert.assertEquals(1, cter.getExceptionCount());
//        
//        cter.reset();
//
//        // Second loop over the document; this should again give an exception...
//        try {
//            for (Node node : DOM.descendants(doc)) {
//            }
//            Assert.fail("Expected DOMDeferredParsingException");
//        } catch (DOMDeferredParsingException ex) {
//            // Expected
//        }
//        // ... but without any invocation of the underlying StAX parser
//        Assert.assertEquals(0, cter.getInvocationCount());
//    }

    /**
     * Validate the behavior of {@link Document#createElementNS(String, String)} when invoked with
     * an empty namespace URI (instead of <code>null</code>).
     */
    // TODO: same test for attributes (create attribute [DONE], set attribute, etc.), renameNode, etc.
    @Validated @Test
    public void testCreateElementNSWithEmptyNamespaceURI() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("", "test");
        assertNull(element.getNamespaceURI());
    }
    
    @Validated @Test
    public void testCreateAttributeNSWithEmptyNamespaceURI() {
        Document doc = domUtil.newDocument();
        Attr attr = doc.createAttributeNS("", "test");
        assertNull(attr.getNamespaceURI());
    }
    
    /**
     * @see ElementTest#testSetAttributeNSNamespaceDeclaration()
     */
    @Validated @Test
    public void testCreateAttributeNSNamespaceDeclaration() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS(null, "test");
        Attr attr = doc.createAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p");
        attr.setValue("urn:ns");
        element.setAttributeNodeNS(attr);
        assertEquals("urn:ns", element.lookupNamespaceURI("p"));
        assertEquals("p", element.lookupPrefix("urn:ns"));
    }
    
    @Validated @Test
    public void testImportElementWithAttributes() {
        Document doc1 = domUtil.newDocument();
        Element element1 = doc1.createElementNS(null, "test");
        element1.setAttributeNS(null, "attr", "test");
        Document doc2 = domUtil.newDocument();
        Element element2 = (Element)doc2.importNode(element1, false);
        assertEquals("test", element2.getAttributeNS(null, "attr"));
    }
    
    /**
     * Test that {@link Document#importNode(Node, boolean)} correctly imports attributes that
     * represent namespace declarations. This test covers the case where the namespace declaration
     * declares a prefix.
     */
    @Validated @Test
    public void testImportElementWithNamespaceDeclaration() {
        Document doc1 = domUtil.newDocument();
        Element element1 = doc1.createElementNS(null, "test");
        element1.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:p", "urn:test");
        Document doc2 = domUtil.newDocument();
        Element element2 = (Element)doc2.importNode(element1, false);
        assertEquals("urn:test", element2.lookupNamespaceURI("p"));
    }
    
    /**
     * Test that {@link Document#importNode(Node, boolean)} correctly imports attributes that
     * represent namespace declarations. This test covers the case where the namespace declaration
     * declares a default namespace.
     */
    @Validated @Test
    public void testImportElementWithDefaultNamespaceDeclaration() {
        Document doc1 = domUtil.newDocument();
        Element element1 = doc1.createElementNS("urn:ns1", "p:test");
        element1.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns", "urn:ns2");
        Document doc2 = domUtil.newDocument();
        Element element2 = (Element)doc2.importNode(element1, false);
        assertEquals("urn:ns2", element2.lookupNamespaceURI(null));
    }
    
    @Validated @Test
    public void testAdoptNode() {
        Document sourceDocument = domUtil.parse(true, "<root><child/></root>");
        Document targetDocument = domUtil.newDocument();
        Element root = sourceDocument.getDocumentElement();
        assertSame(root, targetDocument.adoptNode(root));
        assertSame(targetDocument, root.getOwnerDocument());
        Node child = root.getFirstChild();
        assertTrue(child instanceof Element);
        assertEquals("child", child.getNodeName());
    }
}
