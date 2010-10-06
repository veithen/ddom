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

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;

import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;

public abstract class SOAPBodyTest extends AbstractTestCase {
    public SOAPBodyTest(String soapVersion) {
        super(soapVersion);
    }

    @Validated @Test
    public final void getChildElementsReification() {
        SOAPBody body = createEmptySOAPBody();
        body.appendChild(body.getOwnerDocument().createElementNS("urn:test", "p:test"));
        Iterator<?> it = body.getChildElements();
        assertTrue(it.hasNext());
        Object child = it.next();
        assertTrue(child instanceof SOAPBodyElement);
    }
    
    @Validated @Test
    public final void addChildElementReification() throws Exception {
        SOAPBody body = createEmptySOAPBody();
        SOAPElement child = body.addChildElement((SOAPElement)body.getOwnerDocument().createElementNS("urn:test", "p:test"));
        assertTrue(child instanceof SOAPBodyElement);
    }
}
