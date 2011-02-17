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
package com.googlecode.ddom.saaj;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

import com.google.code.ddom.frontend.saaj.impl.AbstractSOAPMessageImpl;

public class SOAPMessageImpl extends AbstractSOAPMessageImpl {
    private final SOAPPartImpl soapPart;
    private final MimeHeaders headers = new MimeHeaders();
    private final List<AttachmentPartImpl> attachments = new ArrayList<AttachmentPartImpl>();
    
    public SOAPMessageImpl(SOAPPartImpl soapPart) {
        this.soapPart = soapPart;
    }

    @Override
    public final void addAttachmentPart(AttachmentPart attachmentPart) {
        attachments.add((AttachmentPartImpl)attachmentPart);
    }

    @Override
    public final int countAttachments() {
        return attachments.size();
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
    public Iterator getAttachments() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator getAttachments(MimeHeaders arg0) {
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
    public void removeAllAttachments() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttachments(MimeHeaders arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveChanges() throws SOAPException {
        // TODO
//        throw new UnsupportedOperationException();
    }

    @Override
    public boolean saveRequired() {
        // TODO
        return false;
    }

    @Override
    public void setContentDescription(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeTo(OutputStream arg0) throws SOAPException, IOException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
