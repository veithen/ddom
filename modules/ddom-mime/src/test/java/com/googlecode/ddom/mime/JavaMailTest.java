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
package com.googlecode.ddom.mime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class JavaMailTest {
    private void setHeaders(MimeBodyPart part, Map<String,String> headers) throws MessagingException {
        for (Map.Entry<String,String> entry : headers.entrySet()) {
            part.setHeader(entry.getKey(), entry.getValue());
        }
    }
    
    private Map<String,String> readHeaders(MultipartReader mpr) throws IOException {
        Map<String,String> headers = new HashMap<String,String>();
        while (mpr.nextHeader()) {
            headers.put(mpr.getHeaderName(), mpr.getHeaderValue());
        }
        return headers;
    }
    
    private void test(boolean preamble) throws Exception {
        MimeMultipart multipart = new MimeMultipart();
        
        MimeBodyPart bodyPart1 = new MimeBodyPart();
        StringBuilder buffer = new StringBuilder();
        for (int i=0; i<1000; i++) {
            buffer.append('(');
            buffer.append(i);
            buffer.append(')');
        }
        String content1 = buffer.toString();
        bodyPart1.setDataHandler(new DataHandler(new ByteArrayDataSource(content1.getBytes("UTF-8"), "text/plain")));
        Map<String,String> headers1 = new HashMap<String,String>();
        headers1.put("Content-ID", "<1@example.com>");
        headers1.put("Content-Type", "text/plain; charset=UTF-8");
        setHeaders(bodyPart1, headers1);
        multipart.addBodyPart(bodyPart1);
        
        MimeBodyPart bodyPart2 = new MimeBodyPart();
        byte[] content2 = new byte[10000];
        new Random().nextBytes(content2);
        bodyPart2.setDataHandler(new DataHandler(new ByteArrayDataSource(content2, "application/octet-stream")));
        Map<String,String> headers2 = new HashMap<String,String>();
        headers2.put("Content-ID", "<2@example.com>");
        headers2.put("Content-Type", "application/octet-stream");
        setHeaders(bodyPart2, headers2);
        multipart.addBodyPart(bodyPart2);
        
        if (preamble) {
            multipart.setPreamble("This is a MIME multipart.");
        }
        
        String boundary = new ContentType(multipart.getContentType()).getParameter("boundary");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        multipart.writeTo(baos);
        MultipartReader mpr = new MultipartReader(new ByteArrayInputStream(baos.toByteArray()), boundary);
        assertTrue(mpr.nextPart());
        assertEquals(headers1, readHeaders(mpr));
        assertEquals(content1, IOUtils.toString(mpr.getContent(), "UTF-8"));
        assertTrue(mpr.nextPart());
        assertEquals(headers2, readHeaders(mpr));
        assertTrue(Arrays.equals(content2, IOUtils.toByteArray(mpr.getContent())));
        assertFalse(mpr.nextPart());
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
