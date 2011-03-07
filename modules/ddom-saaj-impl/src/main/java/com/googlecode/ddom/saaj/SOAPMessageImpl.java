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

import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

import com.googlecode.ddom.frontend.saaj.impl.AbstractSOAPMessageImpl;
import com.googlecode.ddom.frontend.saaj.impl.AttachmentPartImpl;
import com.googlecode.ddom.frontend.saaj.impl.AttachmentSet;
import com.googlecode.ddom.frontend.saaj.impl.MessageProfile;

public class SOAPMessageImpl extends AbstractSOAPMessageImpl {
    private MimeHeaders headers;
    private final SOAPPartImpl soapPart;
    
    public SOAPMessageImpl(MessageProfile profile, MimeHeaders headers, SOAPPartImpl soapPart, AttachmentSet attachments) {
        super(profile, attachments);
        this.headers = headers;
        this.soapPart = soapPart;
    }

    @Override
    public final AttachmentPart createAttachmentPart() {
        return new AttachmentPartImpl();
    }

    @Override
    public AttachmentPart getAttachment(SOAPElement arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentDescription() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final MimeHeaders getMimeHeaders() {
        return headers;
    }

    @Override
    public SOAPPart getSOAPPart() {
        return soapPart;
    }

    @Override
    public void removeAttachments(MimeHeaders arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContentDescription(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
