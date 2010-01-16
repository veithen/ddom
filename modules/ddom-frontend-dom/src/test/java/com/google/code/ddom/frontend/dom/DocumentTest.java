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

import java.io.Reader;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.Options;
import com.google.code.ddom.backend.CoreNSUnawareAttribute;
import com.google.code.ddom.backend.CoreNSUnawareElement;
import com.google.code.ddom.stream.options.NamespaceAwareness;
import com.google.code.ddom.utils.dom.DOM;
import com.google.code.ddom.utils.test.InvocationCounter;
import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

/**
 * @author Andreas Veithen
 */
@RunWith(ValidatedTestRunner.class)
public class DocumentTest {
    @ValidatedTestResource(reference=XercesDOMUtil.class, actual=DDOMUtil.class)
    private DOMUtil domUtil;
    
    @Test
    public void testNamespaceUnawareParsing() throws Exception {
        Reader reader = new StringReader("<p:root xmlns:p='urn:ns'>");
        Options options = new Options();
        options.set(NamespaceAwareness.DISABLE);
        Document doc = (Document)DocumentHelper.newInstance().parse("dom", reader, options);
        
        Element element = doc.getDocumentElement();
        Assert.assertTrue(element instanceof CoreNSUnawareElement);
        Assert.assertNull(element.getLocalName());
        Assert.assertEquals("p:root", element.getTagName());
        
        Attr attr = (Attr)element.getAttributes().item(0);
        Assert.assertTrue(attr instanceof CoreNSUnawareAttribute);
        Assert.assertNull(attr.getLocalName());
        Assert.assertEquals("xmlns:p", attr.getName());
    }
    
    /**
     * Test that the implementation behaves gracefully after a parse error. In particular, after
     * the first parse error has occurred, the implementation must not try to access the parser
     * again. The reason is that in some StAX implementations, a parse error leaves the parser in
     * an inconsistent state.
     */
    @Test
    // TODO: this should go to ddom-backend-testsuite
    public void testGracefulBehaviorAfterParseError() throws Exception {
        // TODO: do this properly (we should not allow passing XMLStreamReader instances to DeferredDocumentFactory, because we don't have control over the properties)
        InvocationCounter cter = new InvocationCounter();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader("<root>This is malformed"));
        Document doc = (Document)DocumentHelper.newInstance().parse("dom", cter.createProxy(XMLStreamReader.class, reader));
        
        // First loop over the document; this should give an exception
        try {
            for (Node node : DOM.descendants(doc)) {
            }
            Assert.fail("Expected DOMDeferredParsingException");
        } catch (DOMDeferredParsingException ex) {
            // Expected
        }
        // This exception is a result of an exception thrown by the StAX parser
        Assert.assertEquals(1, cter.getExceptionCount());
        
        cter.reset();

        // Second loop over the document; this should again give an exception...
        try {
            for (Node node : DOM.descendants(doc)) {
            }
            Assert.fail("Expected DOMDeferredParsingException");
        } catch (DOMDeferredParsingException ex) {
            // Expected
        }
        // ... but without any invocation of the underlying StAX parser
        Assert.assertEquals(0, cter.getInvocationCount());
    }

    /**
     * Validate the behavior of {@link Document#createElementNS(String, String)} when invoked with
     * an empty namespace URI (instead of <code>null</code>).
     */
    // TODO: same test for attributes (create attribute, set attribute, etc.), renameNode, etc.
    @Validated @Test
    public void testCreateElementNSWithEmptyNamespaceURI() {
        Document doc = domUtil.newDocument();
        Element element = doc.createElementNS("", "test");
        Assert.assertNull(element.getNamespaceURI());
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
        Assert.assertEquals("urn:ns", element.lookupNamespaceURI("p"));
        Assert.assertEquals("p", element.lookupPrefix("urn:ns"));
    }
}
