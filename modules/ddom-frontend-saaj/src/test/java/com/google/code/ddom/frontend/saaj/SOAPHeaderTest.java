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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class SOAPHeaderTest {
    @ValidatedTestResource(reference=RISAAJUtil.class, actual=DDOMSAAJUtil.class)
    private SAAJUtil saajUtil;

    @Validated @Test @Ignore
    public void testExtractHeaderElementsPartialConsumption() throws Exception {
        SOAPEnvelope env = saajUtil.createSOAP11Envelope();
        SOAPHeader header = saajUtil.createSOAP11Envelope().addHeader();
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
