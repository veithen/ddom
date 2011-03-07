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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;

import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;

public class SOAPProfile extends MessageProfile {
    public SOAPProfile(SOAPVersion soapVersion) {
        super(soapVersion, false);
    }

    @Override
    public MessageDeserializer createDeserializer(final InputStream in) {
        return new MessageDeserializer() {
            public MimeHeaders getSOAPPartHeaders() throws IOException, SOAPException {
                return new MimeHeaders();
            }

            public InputStream getSOAPPartInputStream() throws IOException, SOAPException {
                return in;
            }
            
            public AttachmentSet getAttachments() throws IOException, SOAPException {
                return new SimpleAttachmentSet();
            }
        };
    }

    @Override
    public MessageProfile enableAttachments() {
        // TODO: generate proper boundary
        return new SwAProfile(getSOAPVersion(), "boundary");
    }

    @Override
    public MessageProfile disableAttachments() {
        return this;
    }

    @Override
    public String getContentType() {
        return getSOAPVersion().getContentType();
    }
}
