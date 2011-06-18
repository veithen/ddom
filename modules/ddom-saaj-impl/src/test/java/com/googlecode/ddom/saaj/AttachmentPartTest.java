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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;

import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;
import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;

@RunWith(ValidatedTestRunner.class)
public class AttachmentPartTest {
    @ValidatedTestResource(reference=SOAPMessageFactory1_1Impl.class, actual=SOAP11MessageFactory.class)
    private MessageFactory factory;
    
    private AttachmentPart createAttachment() throws SOAPException {
        return factory.createMessage().createAttachmentPart();
    }
    
    @Validated @Test
    public void testSetContent() throws Exception {
        AttachmentPart att = createAttachment();
        String ct = "text/plain; charset=iso-8859-1";
        String content = "This is a test";
        att.setContent(content, ct);
        assertArrayEquals(new String[] { ct }, att.getMimeHeader("Content-Type"));
        DataHandler dh = att.getDataHandler();
        assertNotNull(dh);
        assertEquals(ct, dh.getContentType());
        assertEquals(content, dh.getContent());
    }
    
    @Validated @Test
    public void testGetSizeOnEmptyAttachment() throws Exception {
        AttachmentPart att = createAttachment();
        assertEquals(0, att.getSize());
    }
    
    @Validated @Test
    public void testGetSizeWithObject() throws Exception {
        AttachmentPart att = createAttachment();
        String content = "This is a test";
        att.setContent(content, "text/plain; charset=ascii");
        assertEquals(content.length(), att.getSize());
    }
    
    @Validated @Test
    public void testGetSizeWithRawContent() throws Exception {
        AttachmentPart att = createAttachment();
        String content = "This is a test";
        att.setRawContent(new ByteArrayInputStream(content.getBytes("ascii")), "text/plain; charset=ascii");
        assertEquals(content.length(), att.getSize());
    }
}
