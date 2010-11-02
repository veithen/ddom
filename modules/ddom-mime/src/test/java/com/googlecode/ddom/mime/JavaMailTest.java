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
package com.googlecode.ddom.mime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.activation.DataHandler;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.junit.Test;

public class JavaMailTest {
    private void test(boolean preamble) throws Exception {
        MimeMultipart multipart = new MimeMultipart();
        
        MimeBodyPart bodyPart1 = new MimeBodyPart();
        byte[] content1 = "Test content".getBytes("UTF-8");
        bodyPart1.setDataHandler(new DataHandler(new ByteArrayDataSource(content1, "text/plain")));
        bodyPart1.setContentID("<1@example.com>");
        bodyPart1.setHeader("Content-Type", "text/plain; charset=UTF-8");
        multipart.addBodyPart(bodyPart1);
        
        MimeBodyPart bodyPart2 = new MimeBodyPart();
        byte[] content2 = new byte[10000];
        new Random().nextBytes(content2);
        bodyPart2.setDataHandler(new DataHandler(new ByteArrayDataSource(content2, "application/octet-stream")));
        bodyPart2.setContentID("<2@example.com>");
        bodyPart2.setHeader("Content-Type", "application/octet-stream");
        multipart.addBodyPart(bodyPart2);
        
        if (preamble) {
            multipart.setPreamble("This is a MIME multipart.");
        }
        
        String boundary = new ContentType(multipart.getContentType()).getParameter("boundary");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        multipart.writeTo(baos);
        MultipartInputStream mpis = new MultipartInputStream(new ByteArrayInputStream(baos.toByteArray()), boundary);
        mpis.nextPart();
    }

    @Test
    public void testWithoutPreamble() throws Exception {
        test(false);
    }

    @Test
    public void testWithPreamble() throws Exception {
        test(true);
    }
}
