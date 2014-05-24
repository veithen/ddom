/*
 * Copyright 2014 Andreas Veithen
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
package com.github.veithen.ddom.ts.saaj;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

public class TestExamineMustUnderstandHeaderElements extends SAAJTestCase {
    private final String protocol;
    private final MessageSet messageSet;
    
    public TestExamineMustUnderstandHeaderElements(SAAJImplementation saajImplementation, String protocol, MessageSet messageSet) {
        super(saajImplementation);
        this.protocol = protocol;
        this.messageSet = messageSet;
        addTestParameter("protocol", protocol);
        addTestParameter("soap", messageSet.getVersion().getVersionNumber());
    }

    @Override
    protected void runTest() throws Throwable {
        MessageFactory messageFactory = saajImplementation.newMessageFactory(protocol);
        MimeHeaders mimeHeaders = new MimeHeaders();
        mimeHeaders.addHeader("Content-Type", messageSet.getVersion().getContentType());
        InputStream in = messageSet.getTestMessage("must-understand.xml");
        try {
            SOAPMessage message = messageFactory.createMessage(mimeHeaders, in);
            SOAPHeader header = message.getSOAPHeader();
            Iterator it = header.examineMustUnderstandHeaderElements(null);
            assertTrue(it.hasNext());
            assertTrue(it.next() instanceof SOAPHeaderElement);
            assertFalse(it.hasNext());
        } finally {
            in.close();
        }
    }
}
