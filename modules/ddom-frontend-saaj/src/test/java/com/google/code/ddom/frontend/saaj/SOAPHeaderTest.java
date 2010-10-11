/*
 * Copyright 2009-2010 Andreas Veithen
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.junit.Ignore;
import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;

/**
 * Contains SOAP version independent test cases for {@link SOAPHeader} and {@link SOAPHeaderElement}.
 * 
 * @author Andreas Veithen
 */
public abstract class SOAPHeaderTest extends AbstractTestCase {
    private final String actorAttributeLocalName;
    
    public SOAPHeaderTest(String soapVersion, String actorAttributeLocalName) {
        super(soapVersion);
        this.actorAttributeLocalName = actorAttributeLocalName;
    }

    @Validated @Test
    public final void testGetChildElementsReification() {
        SOAPHeader header = createEmptySOAPHeader();
        header.appendChild(header.getOwnerDocument().createElementNS("urn:test", "p:test"));
        Iterator<?> it = header.getChildElements();
        assertTrue(it.hasNext());
        Object child = it.next();
        assertTrue(child instanceof SOAPHeaderElement);
    }
    
    // TODO: same tests for other addChildElement variants!
    @Validated @Test
    public final void testAddChildElementReification() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPElement child = header.addChildElement((SOAPElement)header.getOwnerDocument().createElementNS("urn:test", "p:test"));
        assertTrue(child instanceof SOAPHeaderElement);
    }
    
    @Validated @Test(expected=SOAPException.class) @Ignore // TODO: later
    public final void testAddChildElementWithoutNamespace() throws Exception {
        createEmptySOAPHeader().addChildElement("test");
    }
    
    @Validated @Test
    public final void testAddHeaderElementUsingName() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPHeader header = env.addHeader();
        SOAPHeaderElement element = header.addHeaderElement(env.createName("test", "ns", "http://example.org"));
        assertSame(header.getFirstChild(), element);
        assertEquals("test", element.getLocalName());
        assertEquals("ns", element.getPrefix());
        assertEquals("http://example.org", element.getNamespaceURI());
    }
    
    // TODO: the RI adds various namespace declarations in this test; check if this is mandated by the specs
    @Validated @Test
    public final void testGetSetActor() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        element.setActor("urn:my:actor");
        assertEquals("urn:my:actor", element.getAttributeNS(header.getNamespaceURI(), actorAttributeLocalName));
        assertEquals("urn:my:actor", element.getActor());
    }
    
    /**
     * Tests that {@link SOAPHeaderElement#setActor(String)} works correctly even on an element that
     * has been detached from its parent. Note that this means that a {@link SOAPHeaderElement}
     * implicitly retains information about the SOAP version it belongs to.
     * 
     * @throws Exception
     */
    @Validated @Test
    public final void testSetActorOnDetachedHeaderElement() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        element.detachNode();
        assertNull(element.getParentNode());
        element.setActor("urn:my:actor");
        assertEquals("urn:my:actor", element.getAttributeNS(header.getNamespaceURI(), actorAttributeLocalName));
    }
    
    @Validated @Test
    public final void testGetActorUnset() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        assertNull(element.getActor());
    }
    
    @Validated @Test @Ignore // TODO: SAAJ RI gives strange results here
    public final void testSetActorNull() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        element.setActor("urn:actor");
        element.setActor(null);
        System.out.println(element.getActor());
        assertNull(element.getActor());
        assertFalse(element.hasAttributeNS(header.getNamespaceURI(), actorAttributeLocalName));
    }
    
    @Validated @Test
    public final void testGetMustUnderstandUnset() throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        assertFalse(element.getMustUnderstand());
    }
    
    protected final void testGetMustUnderstandFromExistingAttribute(String value, boolean expected) throws Exception {
        SOAPHeader header = createEmptySOAPHeader();
        SOAPHeaderElement element = (SOAPHeaderElement)header.addChildElement("test", "p", "urn:ns");
        element.setAttributeNS(header.getNamespaceURI(), "soap:mustUnderstand", value);
        assertEquals(expected, element.getMustUnderstand());
    }
    
    @Validated @Test
    public final void testGetMustUnderstandFromExistingAttribute0() throws Exception {
        testGetMustUnderstandFromExistingAttribute("0", false);
    }
    
    @Validated @Test
    public final void testGetMustUnderstandFromExistingAttribute1() throws Exception {
        testGetMustUnderstandFromExistingAttribute("1", true);
    }
    
    @Validated @Test
    public final void testGetMustUnderstandFromExistingAttributeFalse() throws Exception {
        testGetMustUnderstandFromExistingAttribute("false", false);
    }
    
    @Validated @Test
    public final void testGetMustUnderstandFromExistingAttributeTrue() throws Exception {
        testGetMustUnderstandFromExistingAttribute("true", true);
    }
    
    @Validated @Test
    public void testGetMustUnderstandFromExistingAttributeInvalid() throws Exception {
        testGetMustUnderstandFromExistingAttribute("invalid", false);
    }
    
    @Validated @Test @Ignore // TODO
    public final void testExtractHeaderElementsPartialConsumption() throws Exception {
        SOAPEnvelope env = createSOAPEnvelope();
        SOAPHeader header = env.addHeader();
        SOAPHeaderElement element1 = header.addHeaderElement(env.createName("test", "p", "urn:ns1"));
        element1.setActor("urn:my-actor");
        SOAPHeaderElement element2 = header.addHeaderElement(env.createName("test", "p", "urn:ns2"));
        element2.setActor("urn:my-actor");
        Iterator it = header.extractHeaderElements("urn:my-actor");
        // Only consume the first element from the iterator
        assertTrue(it.hasNext());
        assertSame(element1, it.next());
        // Although only one element has been consumed, all matching elements have been removed
        assertNull(header.getFirstChild());
    }
}
