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
package com.googlecode.ddom.frontend.saaj.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;

import org.apache.commons.io.IOUtils;

import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;
import com.googlecode.ddom.mime.MultipartReader;

public class SwAProfile extends MessageProfile {
    private final String boundary;
    
    public SwAProfile(SOAPVersion soapVersion, String boundary) {
        super(soapVersion, true);
        this.boundary = boundary;
    }

    @Override
    public MessageDeserializer createDeserializer(InputStream in) {
        // TODO: optimize this and add support for lazy loading of attachments
        final MultipartReader mpr = new MultipartReader(in, boundary);
        return new MessageDeserializer() {
            public MimeHeaders getSOAPPartHeaders() throws IOException, SOAPException {
                if (!mpr.nextPart()) {
                    throw new SOAPException("Message has no SOAP part");
                }
                return readMimeHeaders(mpr);
            }

            public InputStream getSOAPPartInputStream() throws IOException, SOAPException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(mpr.getContent(), baos);
                return new ByteArrayInputStream(baos.toByteArray());
            }
            
            public AttachmentSet getAttachments() throws IOException, SOAPException {
                AttachmentSet attachments = new SimpleAttachmentSet();
                while (mpr.nextPart()) {
                    // TODO: shouldn't we have a factory method in AttachmentSet?
                    AttachmentPartImpl attachment = new AttachmentPartImpl(readMimeHeaders(mpr));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(mpr.getContent(), baos);
                    attachment.setDataHandler(new DataHandler(new ByteArrayDataSource(baos.toByteArray(), attachment.getContentType())));
                    attachments.add(attachment);
                }
                return attachments;
            }
        };
    }
    
    static MimeHeaders readMimeHeaders(MultipartReader mpr) throws IOException {
        MimeHeaders headers = new MimeHeaders();
        while (mpr.nextHeader()) {
            headers.addHeader(mpr.getHeaderName(), mpr.getHeaderValue());
        }
        return headers;
    }

    @Override
    public MessageProfile enableAttachments() {
        return this;
    }

    @Override
    public String getContentType() {
        MimeType contentType;
        try {
            contentType = new MimeType("multipart", "related");
        } catch (MimeTypeParseException ex) {
            throw new Error(ex);
        }
        contentType.setParameter("type", getSOAPVersion().getContentType());
        contentType.setParameter("boundary", boundary);
        return contentType.toString();
    }
}
