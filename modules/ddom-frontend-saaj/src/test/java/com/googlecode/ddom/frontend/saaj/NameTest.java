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

import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class NameTest {
    @ValidatedTestResource(reference=RISAAJUtil.class, actual=DDOMSAAJUtil.class)
    private SAAJUtil saajUtil;

    @Validated @Test
    public void testEquals() throws Exception {
        SOAPEnvelope envelope = saajUtil.createSOAP11Envelope();
        Name name1 = envelope.createName("test", "p1", "urn:ns");
        Name name2 = envelope.createName("test", "p2", "urn:ns");
        assertEquals(name1, name2);
    }
    
    // The reference implementation doesn't implement hashCode
    @Test
    public void testHashCode() throws Exception {
        SOAPEnvelope envelope = saajUtil.createSOAP11Envelope();
        Name name1 = envelope.createName("test", "p1", "urn:ns");
        Name name2 = envelope.createName("test", "p2", "urn:ns");
        assertEquals(name1.hashCode(), name2.hashCode());
    }
}
