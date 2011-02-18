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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * Base class for {@link SOAPMessage} wrappers.
 * 
 * @author Andreas Veithen
 */
public class SOAPMessageWrapper extends SOAPMessage {
    private final SOAPMessage parent;

    public SOAPMessageWrapper(SOAPMessage parent) {
        this.parent = parent;
    }

    public void addAttachmentPart(AttachmentPart AttachmentPart) {
        parent.addAttachmentPart(AttachmentPart);
    }

    public int countAttachments() {
        return parent.countAttachments();
    }

    public AttachmentPart createAttachmentPart() {
        return parent.createAttachmentPart();
    }

    public AttachmentPart createAttachmentPart(DataHandler dataHandler) {
        return parent.createAttachmentPart(dataHandler);
    }

    public AttachmentPart createAttachmentPart(Object content, String contentType) {
        return parent.createAttachmentPart(content, contentType);
    }

    public AttachmentPart getAttachment(SOAPElement element) throws SOAPException {
        return parent.getAttachment(element);
    }

    public Iterator getAttachments() {
        return parent.getAttachments();
    }

    public Iterator getAttachments(MimeHeaders headers) {
        return parent.getAttachments(headers);
    }

    public String getContentDescription() {
        return parent.getContentDescription();
    }

    public MimeHeaders getMimeHeaders() {
        return parent.getMimeHeaders();
    }

    public Object getProperty(String property) throws SOAPException {
        return parent.getProperty(property);
    }

    public SOAPBody getSOAPBody() throws SOAPException {
        return parent.getSOAPBody();
    }

    public SOAPHeader getSOAPHeader() throws SOAPException {
        return parent.getSOAPHeader();
    }

    public SOAPPart getSOAPPart() {
        return parent.getSOAPPart();
    }

    public void removeAllAttachments() {
        parent.removeAllAttachments();
    }

    public void removeAttachments(MimeHeaders headers) {
        parent.removeAttachments(headers);
    }

    public void saveChanges() throws SOAPException {
        parent.saveChanges();
    }

    public boolean saveRequired() {
        return parent.saveRequired();
    }

    public void setContentDescription(String description) {
        parent.setContentDescription(description);
    }

    public void setProperty(String property, Object value) throws SOAPException {
        parent.setProperty(property, value);
    }

    public void writeTo(OutputStream out) throws SOAPException, IOException {
        parent.writeTo(out);
    }
}
