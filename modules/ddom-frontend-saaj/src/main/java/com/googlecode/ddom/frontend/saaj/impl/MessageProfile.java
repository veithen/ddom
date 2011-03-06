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

import java.io.InputStream;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.xml.soap.SOAPException;

import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;

public abstract class MessageProfile {
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
    private final boolean supportsAttachments;
    
    public MessageProfile(SOAPVersion soapVersion, boolean supportsAttachments) {
        this.soapVersion = soapVersion;
        this.supportsAttachments = supportsAttachments;
    }
    
    public final SOAPVersion getSOAPVersion() {
        return soapVersion;
    }
    
    public final boolean supportsAttachments() {
        return supportsAttachments;
    }

    public abstract MessageDeserializer createDeserializer(InputStream in);
    public abstract MessageProfile enableAttachments();
    public abstract String getContentType();

    public static MessageProfile fromContentType(String contentTypeString) throws SOAPException {
        MimeType contentType;
        try {
            contentType = new MimeType(contentTypeString);
        } catch (MimeTypeParseException ex) {
            throw new SOAPException("Unable to parse content type", ex);
        }
        SOAPVersion soapVersion = getSOAPVersion(contentType);
        if (soapVersion != null) {
            return new SOAPProfile(soapVersion);
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
            soapVersion = getSOAPVersion(type);
            if (soapVersion == null) {
                throw new SOAPException("Unrecognized content type");
            }
            String boundary = contentType.getParameter("boundary");
            if (boundary == null) {
                throw new SOAPException("Content type doesn't specify a boundary");
            }
            return new SwAProfile(soapVersion, boundary);
        } else {
            throw new SOAPException("Unrecognized content type");
        }
    }
    
    private static SOAPVersion getSOAPVersion(MimeType contentType) throws SOAPException {
        if (contentType.match(SOAP11_CONTENT_TYPE)) {
            return SOAPVersion.SOAP11;
        } else if (contentType.match(SOAP12_CONTENT_TYPE)) {
            return SOAPVersion.SOAP12;
        } else {
            return null;
        }
    }
}
