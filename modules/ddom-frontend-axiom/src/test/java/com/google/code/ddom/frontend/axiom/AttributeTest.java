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

import org.apache.axiom.om.OMAttribute;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class AttributeTest {
    @ValidatedTestResource(reference=LLOMAxiomUtil.class, actual=DDOMAxiomUtil.class)
    private AxiomUtil axiomUtil;
    
    @Validated @Test
    public void testGetQNameWithoutNamespace() {
        OMAttribute attr = axiomUtil.createDocument().getOMFactory().createOMAttribute("name", null, "value");
        QName qname = attr.getQName();
        Assert.assertEquals("name", qname.getLocalPart());
        Assert.assertEquals("", qname.getNamespaceURI());
        Assert.assertEquals("", qname.getPrefix());
    }
    
    @Ignore // TODO
    @Validated @Test
    public void testQNameCaching() {
        OMAttribute attr = axiomUtil.createDocument().getOMFactory().createOMAttribute("name", null, "value");
        QName qname = attr.getQName();
        Assert.assertSame(qname, attr.getQName());
    }
}
