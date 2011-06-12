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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.AttachmentPart;

public class SimpleAttachmentSet implements AttachmentSet {
    private final List<AttachmentPartImpl> attachments = new ArrayList<AttachmentPartImpl>();
    
    public void add(AttachmentPart attachmentPart) {
        attachments.add((AttachmentPartImpl)attachmentPart);
    }

    public int count() {
        return attachments.size();
    }

    public Iterator<? extends AttachmentPart> iterator() {
        return attachments.iterator();
    }

    public void removeAll() {
        attachments.clear();
    }
}
