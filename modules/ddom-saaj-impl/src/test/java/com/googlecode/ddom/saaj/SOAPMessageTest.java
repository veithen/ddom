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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;
import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;

@RunWith(ValidatedTestRunner.class)
public class SOAPMessageTest {
    @ValidatedTestResource(reference=SOAPMessageFactory1_1Impl.class, actual=SOAP11MessageFactory.class)
    private MessageFactory factory;
    
    @Validated @Test
    public void testCreateAttachmentPart() throws Exception {
        SOAPMessage message = factory.createMessage();
        message.createAttachmentPart();
        // The attachment part is not added to the message
        assertEquals(0, message.countAttachments());
    }
    
    @Validated @Test
    public void testAddAttachmentPart() throws Exception {
        SOAPMessage message = factory.createMessage();
        AttachmentPart attachment = message.createAttachmentPart();
        message.addAttachmentPart(attachment);
        Iterator it = message.getAttachments();
        assertTrue(it.hasNext());
        assertSame(attachment, it.next());
        assertFalse(it.hasNext());
    }
    
    @Validated @Test
    public void testGetAttachmentsFiltered() throws Exception {
        SOAPMessage message = factory.createMessage();
        
        AttachmentPart att1 = message.createAttachmentPart();
        att1.addMimeHeader("Content-Type", "text/plain");
        message.addAttachmentPart(att1);
        
        AttachmentPart att2 = message.createAttachmentPart();
        att2.addMimeHeader("Content-Type", "application/octet-stream");
        message.addAttachmentPart(att2);
        
        AttachmentPart att3 = message.createAttachmentPart();
        att3.addMimeHeader("Content-ID", "<123456@example.com>");
        att3.addMimeHeader("Content-Type", "text/plain");
        message.addAttachmentPart(att3);
        
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/plain");
        Iterator it = message.getAttachments(headers);
        assertTrue(it.hasNext());
        assertSame(att1, it.next());
        assertTrue(it.hasNext());
        assertSame(att3, it.next());
        assertFalse(it.hasNext());
    }
    
    @Validated @Test
    public void testGetSetCharacterSetEncoding() throws Exception {
        SOAPMessage message = factory.createMessage();
        String encoding = "ISO-8859-15";
        message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, encoding);
        assertEquals(encoding, message.getProperty(SOAPMessage.CHARACTER_SET_ENCODING));
    }
    
    /**
     * Tests that calling {@link SOAPMessage#setProperty(String, Object)} doesn't throw an exception
     * for unknown property names. Although that the Javadoc of that method suggests that a
     * {@link SOAPException} should be thrown, this is not the case for the reference
     * implementation.
     * 
     * @throws Exception
     */
    @Validated @Test
    public void testSetPropertyUnknown() throws Exception {
        SOAPMessage message = factory.createMessage();
        message.setProperty("some.unknown.property", "test");
    }
    
    /**
     * Tests the behavior of {@link SOAPPart#getContent()} for a plain SOAP message created from an
     * input stream.
     * 
     * @throws Exception
     * 
     * @see SOAPPartTest#testGetContent()
     */
    @Validated @Test
    public void testWriteTo() throws Exception {
        MimeHeaders headers = new MimeHeaders();
        headers.addHeader("Content-Type", "text/xml; charset=utf-8");
        InputStream in = SOAPPartTest.class.getResourceAsStream("message.xml");
        byte[] orgContent = IOUtils.toByteArray(in);
        SOAPMessage message = factory.createMessage(headers, new ByteArrayInputStream(orgContent));
        
        // Get the content before accessing the SOAP part
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        byte[] content1 = baos.toByteArray();
        assertTrue(Arrays.equals(orgContent, content1));
        
        // Now access the SOAP part and get the content again
        message.getSOAPPart().getEnvelope();
        baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        byte[] content2 = baos.toByteArray();
        // The content is equivalent, but not exactly the same
        assertFalse(Arrays.equals(orgContent, content2));
    }
    
    /**
     * Tests that {@link SOAPMessage#writeTo(java.io.OutputStream)} performs namespace repairing.
     * 
     * @throws Exception
     */
    @Validated @Test
    public void testWriteToNamespaceRepairing() throws Exception {
        SOAPMessage message = factory.createMessage();
        SOAPPart part = message.getSOAPPart();
        SOAPBody body = part.getEnvelope().getBody();
        body.appendChild(part.createElementNS("urn:ns", "p:test"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        String content = baos.toString("UTF-8");
        assertTrue(content.contains("<p:test xmlns:p=\"urn:ns\"/>"));
    }
    
    @Validated @Test
    public void testWriteToWithAttachment() throws Exception {
        SOAPMessage message = factory.createMessage();
        message.getSOAPPart().getEnvelope().getBody().addBodyElement(new QName("urn:ns", "test", "p"));
        AttachmentPart attachment = message.createAttachmentPart();
        attachment.setDataHandler(new DataHandler("This is a test", "text/plain"));
        message.addAttachmentPart(attachment);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        MimeMultipart mp = new MimeMultipart(new ByteArrayDataSource(baos.toByteArray(), "multipart/related"));
        mp.getBodyPart(0);
        mp.getBodyPart(1);
    }
}
