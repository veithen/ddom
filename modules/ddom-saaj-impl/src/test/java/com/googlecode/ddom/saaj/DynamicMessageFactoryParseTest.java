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

import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.Ignore;
import org.junit.Test;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.sun.xml.messaging.saaj.soap.dynamic.SOAPMessageFactoryDynamicImpl;

public abstract class DynamicMessageFactoryParseTest extends MessageFactoryParseTest {
    @ValidatedTestResource(reference=SOAPMessageFactoryDynamicImpl.class, actual=DynamicMessageFactory.class)
    private MessageFactory factory;
    
    public DynamicMessageFactoryParseTest(MessageSet messageSet) {
        super(messageSet);
    }

    protected final MessageFactory getFactory() {
        return factory;
    }

    // One would expect a SOAPException here, but the RI simply throws NullPointerException
    @Validated @Test(expected=Exception.class)
    public final void testWithoutHeaders() throws Exception {
        InputStream in = messageSet.getTestMessage("message.xml");
        try {
            getFactory().createMessage(null, in);
        } finally {
            in.close();
        }
    }

    @Validated @Test(expected=SOAPException.class)
    public final void testWithoutContentType() throws Exception {
        InputStream in = messageSet.getTestMessage("message.xml");
        try {
            getFactory().createMessage(new MimeHeaders(), in);
        } finally {
            in.close();
        }
    }
    
    @Validated @Test(expected=SOAPException.class) @Ignore // TODO
    public final void testWithSOAPVersionMismatch() throws Exception {
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", messageSet.getAltMessageSet().getVersion().getContentType());
        InputStream in = messageSet.getTestMessage("message.xml");
        try {
            SOAPMessage message = getFactory().createMessage(headers, in);
            // The exception will actually be thrown here. Note that even the RI defers building
            // of the object model until it is actually accessed.
            message.getSOAPPart().getEnvelope();
        } finally {
            in.close();
        }
    }
}
