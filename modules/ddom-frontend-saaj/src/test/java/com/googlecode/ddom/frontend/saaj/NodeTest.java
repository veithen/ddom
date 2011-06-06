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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.NodeList;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class NodeTest {
    @ValidatedTestResource(reference=RISAAJUtil.class, actual=DDOMSAAJUtil.class)
    private SAAJUtil saajUtil;
    
    @Validated @Test
    public void testGetParentElementOnOrphan() throws Exception {
        SOAPElement element = saajUtil.createSOAPElement(null, "test", null);
        assertNull(element.getParentElement());
    }
    
    @Validated @Test
    public void testGetParentElement() throws Exception {
        SOAPElement parent = saajUtil.createSOAPElement(null, "parent", null);
        SOAPElement child = parent.addChildElement(new QName("child"));
        assertSame(parent, child.getParentElement());
    }
    
    @Validated @Test
    public void testGetParentElementOnDocumentElement() throws Exception {
        SOAPEnvelope envelope = saajUtil.createSOAP11Envelope();
        envelope.getOwnerDocument().appendChild(envelope);
        assertNull(envelope.getParentElement());
    }
    
    // TODO: also test behavior when the node already has a parent
    @Validated @Test
    public void testSetParentElement() throws Exception {
        SOAPElement parent = saajUtil.createSOAPElement(null, "parent", null);
        SOAPElement child1 = parent.addChildElement(new QName("child1"));
        SOAPElement child2 = (SOAPElement)parent.getOwnerDocument().createElementNS(null, "child2");
        child2.setParentElement(parent);
        NodeList children = parent.getChildNodes();
        assertEquals(2, children.getLength());
        assertSame(child1, children.item(0));
        assertSame(child2, children.item(1));
    }
}
