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
package com.googlecode.ddom.saaj;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;
import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl;

@RunWith(ValidatedTestRunner.class)
public class SOAPFactoryTest {
    @ValidatedTestResource(reference=SOAPFactory1_1Impl.class, actual=SOAPFactoryImpl.class)
    private SOAPFactory factory;

    @Validated @Test @Ignore // TODO
    public void test() throws SOAPException {
        SOAPElement parent = factory.createElement(new QName("urn:test", "parent"));
        SOAPElement orgChild = factory.createElement(new QName("urn:test", "child"));
        SOAPElement child = parent.addChildElement(orgChild);
        Assert.assertEquals(orgChild.getElementQName(), child.getElementQName());
        Assert.assertSame(child, parent.getFirstChild());
    }
}
