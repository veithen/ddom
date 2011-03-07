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

import org.apache.cxf.binding.soap.SoapMessage;

import com.googlecode.ddom.frontend.saaj.impl.AttachmentSet;

public class CXFAttachmentSet implements AttachmentSet {
    private final SoapMessage message;

    public CXFAttachmentSet(SoapMessage message) {
        this.message = message;
    }

    public void add(AttachmentPart attachmentPart) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public int count() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator<? extends AttachmentPart> iterator() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void removeAll() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
