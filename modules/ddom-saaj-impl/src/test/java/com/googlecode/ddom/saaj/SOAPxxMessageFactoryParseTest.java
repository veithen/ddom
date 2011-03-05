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
package com.googlecode.ddom.saaj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;

public abstract class SOAPxxMessageFactoryParseTest extends MessageFactoryParseTest {
    public SOAPxxMessageFactoryParseTest(MessageSet messageSet) {
        super(messageSet);
    }

    private void testNoContentType(MimeHeaders orgHeaders) throws Exception {
        InputStream in = messageSet.getTestMessage("message.xml");
        try {
            SOAPMessage message = getFactory().createMessage(null, in);
            MimeHeaders headers = message.getMimeHeaders();
            assertNotNull(headers);
            String[] contentType = headers.getHeader("Content-Type");
            assertNotNull(contentType);
            assertEquals(1, contentType.length);
            assertEquals(messageSet.getVersion().getContentType(), contentType[0]);
        } finally {
            in.close();
        }
    }

    @Validated @Test
    public final void testWithoutHeaders() throws Exception {
        testNoContentType(null);
    }

    @Validated @Test
    public final void testWithoutContentType() throws Exception {
        testNoContentType(new MimeHeaders());
    }
    
    @Validated @Test(expected=SOAPException.class)
    public final void testWithWrongSOAPVersion() throws Exception {
        MimeHeaders headers = new MimeHeaders();
        MessageSet altMessageSet = messageSet.getAltMessageSet();
        headers.addHeader("Content-Type", altMessageSet.getVersion().getContentType());
        InputStream in = altMessageSet.getTestMessage("message.xml");
        try {
            getFactory().createMessage(headers, in);
        } finally {
            in.close();
        }
    }
}
