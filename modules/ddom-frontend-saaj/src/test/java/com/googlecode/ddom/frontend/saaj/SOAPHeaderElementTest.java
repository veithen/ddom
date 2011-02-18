/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.frontend.saaj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.junit.Ignore;
import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;

/**
 * Contains SOAP version independent test cases for {@link SOAPHeaderElement}.
 * 
 * @author Andreas Veithen
 */
public abstract class SOAPHeaderElementTest extends AbstractTestCase {
    private final String actorAttributeLocalName;

    public SOAPHeaderElementTest(String soapVersion, String actorAttributeLocalName) {
        super(soapVersion);
        this.actorAttributeLocalName = actorAttributeLocalName;
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
}
