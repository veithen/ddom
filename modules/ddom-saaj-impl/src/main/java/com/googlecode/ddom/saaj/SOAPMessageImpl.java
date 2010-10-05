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
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

public class SOAPMessageImpl extends SOAPMessage {
    private final SOAPPartImpl soapPart;
    private final List<AttachmentPartImpl> attachments = new ArrayList<AttachmentPartImpl>();
    
    public SOAPMessageImpl(SOAPPartImpl soapPart) {
        this.soapPart = soapPart;
    }

    @Override
    public SOAPHeader getSOAPHeader() throws SOAPException {
        return getSOAPPart().getEnvelope().getHeader();
    }

    @Override
    public SOAPBody getSOAPBody() throws SOAPException {
        return getSOAPPart().getEnvelope().getBody();
    }

    @Override
    public void addAttachmentPart(AttachmentPart arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public int countAttachments() {
        return attachments.size();
    }

    @Override
    public AttachmentPart createAttachmentPart() {
        // TODO
        throw new UnsupportedOperationException();
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
    public MimeHeaders getMimeHeaders() {
        // TODO
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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
