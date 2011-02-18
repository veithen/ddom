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
package com.googlecode.ddom.cxf;

import java.util.Iterator;

import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;

import org.apache.cxf.binding.soap.SoapMessage;

import com.googlecode.ddom.frontend.saaj.impl.AbstractSOAPMessageImpl;
import com.googlecode.ddom.model.Model;

public class SOAPMessageImpl extends AbstractSOAPMessageImpl {
    private final Model saajModel;
    private final SoapMessage message;
    private SOAPPart soapPart;
    
    public SOAPMessageImpl(Model saajModel, SoapMessage message) {
        this.saajModel = saajModel;
        this.message = message;
    }

    @Override
    public SOAPPart getSOAPPart() {
        if (soapPart == null) {
            soapPart = new SOAPPartImpl(saajModel, message);
        }
        return soapPart;
    }

    @Override
    public final void addAttachmentPart(AttachmentPart attachmentPart) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final int countAttachments() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final AttachmentPart createAttachmentPart() {
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
}
