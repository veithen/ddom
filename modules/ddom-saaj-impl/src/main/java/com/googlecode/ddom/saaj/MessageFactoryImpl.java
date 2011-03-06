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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.io.IOUtils;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.saaj.impl.AttachmentSet;
import com.googlecode.ddom.mime.MultipartReader;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;
import com.googlecode.ddom.spi.ProviderFinder;

public abstract class MessageFactoryImpl extends MessageFactory {
    private static final MimeType SOAP11_CONTENT_TYPE;
    private static final MimeType SOAP12_CONTENT_TYPE;
    private static final MimeType MULTIPART_RELATED;
    
    static {
        try {
            SOAP11_CONTENT_TYPE = new MimeType("text", "xml");
            SOAP12_CONTENT_TYPE = new MimeType("application", "soap+xml");
            MULTIPART_RELATED = new MimeType("multipart", "related");
        } catch (MimeTypeParseException ex) {
            throw new Error(ex);
        }
    }
    
    private final SOAPVersion soapVersion;
    private final NodeFactory nodeFactory;
    private final CompatibilityPolicy compatibilityPolicy;
    
    public MessageFactoryImpl(SOAPVersion soapVersion) throws SOAPException {
        this.soapVersion = soapVersion;
        ModelRegistry modelRegistry = ModelRegistry.getInstance(SOAPFactoryImpl.class.getClassLoader());
        try {
            nodeFactory = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("saaj")).getNodeFactory();
        } catch (ModelLoaderException ex) {
            throw new SOAPException(ex);
        }
        Map<String,CompatibilityPolicy> policies = ProviderFinder.find(MessageFactoryImpl.class.getClassLoader(), CompatibilityPolicy.class);
        if (policies.isEmpty()) {
            compatibilityPolicy = DefaultCompatibilityPolicy.INSTANCE;
        } else if (policies.size() == 1) {
            compatibilityPolicy = policies.values().iterator().next();
        } else {
            throw new SOAPException("Multiple compatibility policies found in classpath");
        }
    }

    @Override
    public SOAPMessage createMessage() throws SOAPException {
        return compatibilityPolicy.wrapMessage(new SOAPMessageImpl(new MimeHeaders(), new SOAPPartImpl(nodeFactory, soapVersion), new SimpleAttachmentSet()));
    }
    
    @Override
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws IOException, SOAPException {
        String contentTypeString = null;
        if (headers == null) {
            headers = new MimeHeaders();
        } else {
            String[] values = headers.getHeader("Content-Type");
            if (values != null) {
                contentTypeString = values[0];
            }
        }
        SOAPVersion soapVersionFromContentType;
        MimeHeaders soapPartHeaders;
        InputStream soapPartInputStream;
        AttachmentSet attachments;
        if (contentTypeString == null) {
            if (soapVersion == null) {
                throw new SOAPException("No content type specified");
            }
            headers.addHeader("Content-Type", soapVersion.getContentType());
            soapVersionFromContentType = soapVersion;
            soapPartHeaders = headers;
            soapPartInputStream = in;
            attachments = new SimpleAttachmentSet();
        } else {
            MimeType contentType;
            try {
                contentType = new MimeType(contentTypeString);
            } catch (MimeTypeParseException ex) {
                throw new SOAPException("Unable to parse content type", ex);
            }
            soapVersionFromContentType = getSOAPVersionFromContentType(contentType);
            if (soapVersionFromContentType != null) {
                soapPartHeaders = headers;
                soapPartInputStream = in;
                attachments = new SimpleAttachmentSet();
            } else if (contentType.match(MULTIPART_RELATED)) {
                String typeString = contentType.getParameter("type");
                if (typeString == null) {
                    throw new SOAPException("No type parameter");
                }
                MimeType type;
                try {
                    type = new MimeType(typeString);
                } catch (MimeTypeParseException ex) {
                    throw new SOAPException("Unable to parse the type parameter", ex);
                }
                soapVersionFromContentType = getSOAPVersionFromContentType(type);
                if (soapVersionFromContentType == null) {
                    throw new SOAPException("Unrecognized content type");
                }
                String boundary = contentType.getParameter("boundary");
                if (boundary == null) {
                    throw new SOAPException("Content type doesn't specify a boundary");
                }
                // TODO: optimize this and add support for lazy loading of attachments
                attachments = new SimpleAttachmentSet();
                MultipartReader mpr = new MultipartReader(in, boundary);
                if (!mpr.nextPart()) {
                    throw new SOAPException("Message has no SOAP part");
                }
                soapPartHeaders = readMimeHeaders(mpr);
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(mpr.getContent(), baos);
                    soapPartInputStream = new ByteArrayInputStream(baos.toByteArray());
                }
                while (mpr.nextPart()) {
                    // TODO: shouldn't we have a factory method in AttachmentSet?
                    AttachmentPartImpl attachment = new AttachmentPartImpl(readMimeHeaders(mpr));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    IOUtils.copy(mpr.getContent(), baos);
                    attachment.setDataHandler(new DataHandler(new ByteArrayDataSource(baos.toByteArray(), attachment.getContentType())));
                    attachments.add(attachment);
                }
            } else {
                throw new SOAPException("Unrecognized content type");
            }
        }
        // Note: the case where there is a mismatch between the SOAP versions is already covered
        // by getSOAPVersionFromContentType.
        SOAPVersion actualSOAPVersion = soapVersionFromContentType != null ? soapVersionFromContentType : soapVersion;
        return compatibilityPolicy.wrapMessage(new SOAPMessageImpl(headers, new SOAPPartImpl(nodeFactory, actualSOAPVersion, soapPartInputStream), attachments));
    }
    
    private SOAPVersion getSOAPVersionFromContentType(MimeType contentType) throws SOAPException {
        SOAPVersion soapVersionFromContentType;
        if (contentType.match(SOAP11_CONTENT_TYPE)) {
            soapVersionFromContentType = SOAPVersion.SOAP11;
        } else if (contentType.match(SOAP12_CONTENT_TYPE)) {
            soapVersionFromContentType = SOAPVersion.SOAP12;
        } else {
            soapVersionFromContentType = null;
        }
        if (soapVersion != null && soapVersionFromContentType != null && soapVersionFromContentType != soapVersion) {
            throw new SOAPException("The SOAP version specified by the content type " + contentType.getBaseType() + " is not supported by this factory");
        } else {
            return soapVersionFromContentType;
        }
    }
    
    private MimeHeaders readMimeHeaders(MultipartReader mpr) throws IOException {
        MimeHeaders headers = new MimeHeaders();
        while (mpr.nextHeader()) {
            headers.addHeader(mpr.getHeaderName(), mpr.getHeaderValue());
        }
        return headers;
    }
}
