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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public abstract class MessageFactoryParseTest {
    protected final MessageSet messageSet;
    
    public MessageFactoryParseTest(MessageSet messageSet) {
        this.messageSet = messageSet;
    }

    protected abstract MessageFactory getFactory();

    /**
     * Tests the behavior of {@link MessageFactory#createMessage(MimeHeaders, InputStream)} when no
     * {@link MimeHeaders} object is passed as argument.
     * 
     * @throws Exception
     */
    public abstract void testWithoutHeaders() throws Exception;
    
    /**
     * Tests the behavior of {@link MessageFactory#createMessage(MimeHeaders, InputStream)} when no
     * content type is specified.
     * 
     * @throws Exception
     */
    public abstract void testWithoutContentType() throws Exception;
    
    @Validated @Test
    public void testSwA() throws Exception {
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "multipart/related; type=\"" + messageSet.getVersion().getContentType() + "\"; boundary=\"----=_Part_0_1486975107.1299323234167\"");
        InputStream in = messageSet.getTestMessage("swa-message.bin");
        try {
            SOAPMessage message = getFactory().createMessage(headers, in);
            assertEquals(new QName("urn:test", "addDocument"),
                    ((SOAPElement)message.getSOAPBody().getChildElements().next()).getElementQName());
            Iterator it = message.getAttachments();
            assertTrue(it.hasNext());
            AttachmentPart att = (AttachmentPart)it.next();
            assertEquals("text/plain", att.getContentType());
            Object content = att.getContent();
            assertTrue(content instanceof String);
            assertEquals(3563, ((String)content).length());
            assertFalse(it.hasNext());
        } finally {
            in.close();
        }
    }
}
