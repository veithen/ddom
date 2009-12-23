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

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;

@RunWith(AxiomTestRunner.class)
public class FactoryTest {
    @Validated @Test
    public void createOMNamespace() {
        OMNamespace ns = AxiomUtil.createDocument().getOMFactory().createOMNamespace("urn:test", "t");
        Assert.assertEquals("urn:test", ns.getNamespaceURI());
        Assert.assertEquals("t", ns.getPrefix());
    }
    
    @Validated @Test(expected=IllegalArgumentException.class)
    public void createOMNamespaceWithNullURI() {
        AxiomUtil.createDocument().getOMFactory().createOMNamespace(null, "t");
    }
    
    @Validated @Test
    public void createOMElementFromQNameWithoutNamespace() {
        QName qname = new QName("test");
        OMElement element = AxiomUtil.createDocument().getOMFactory().createOMElement(qname);
        Assert.assertEquals(qname.getLocalPart(), element.getLocalName());
        Assert.assertNull(element.getNamespace());
    }
    
    @Validated @Test
    public void createOMElementFromQNameWithNamespace() {
        QName qname = new QName("test", "urn:test", "t");
        OMElement element = AxiomUtil.createDocument().getOMFactory().createOMElement(qname);
        Assert.assertEquals(qname.getLocalPart(), element.getLocalName());
        OMNamespace ns = element.getNamespace();
        Assert.assertNotNull(ns);
        Assert.assertEquals(qname.getNamespaceURI(), ns.getNamespaceURI());
        Assert.assertEquals(qname.getPrefix(), ns.getPrefix());
    }
}
