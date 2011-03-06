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
import java.io.InputStream;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;

public class AttachmentPartImpl extends AttachmentPart {
    private final MimeHeaders headers;
    private DataHandler dataHandler;
    
    public AttachmentPartImpl(MimeHeaders headers) {
        this.headers = headers;
    }
    
    public AttachmentPartImpl() {
        this(new MimeHeaders());
    }
    
    @Override
    public final DataHandler getDataHandler() throws SOAPException {
        return dataHandler;
    }

    @Override
    public final void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public final void removeAllMimeHeaders() {
        headers.removeAllHeaders();
    }

    public final void removeMimeHeader(String header) {
        headers.removeHeader(header);
    }

    public final String[] getMimeHeader(String name) {
        return headers.getHeader(name);
    }

    public final void setMimeHeader(String name, String value) {
        headers.setHeader(name, value);
    }

    public final void addMimeHeader(String name, String value) {
        headers.addHeader(name, value);
    }

    @SuppressWarnings("unchecked")
    public final Iterator getAllMimeHeaders() {
        return headers.getAllHeaders();
    }

    @SuppressWarnings("unchecked")
    public final Iterator getMatchingMimeHeaders(String[] names) {
        return headers.getMatchingHeaders(names);
    }

    @SuppressWarnings("unchecked")
    public final Iterator getNonMatchingMimeHeaders(String[] names) {
        return headers.getNonMatchingHeaders(names);
    }

    @Override
    public void clearContent() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getBase64Content() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final Object getContent() throws SOAPException {
        // TODO: null check?
        try {
            return dataHandler.getContent();
        } catch (IOException ex) {
            throw new SOAPException("Unable to get the content of the data handler", ex);
        }
    }

    @Override
    public InputStream getRawContent() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getRawContentBytes() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSize() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBase64Content(InputStream arg0, String arg1) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContent(Object arg0, String arg1) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRawContent(InputStream arg0, String arg1) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRawContentBytes(byte[] arg0, int arg1, int arg2, String arg3)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
