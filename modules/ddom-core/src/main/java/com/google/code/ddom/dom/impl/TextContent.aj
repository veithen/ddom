/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.spi.model.CoreChildNode;

public aspect TextContent {
    declare parents: NodeImpl implements DOMNode;

    public final String NodeImpl.getTextContent() throws DOMException {
        CharSequence content = ((DOMNode)this).collectTextContent(null);
        return content == null ? "" : content.toString();
    }

    public final CharSequence DOMComment.collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final CharSequence DOMDocumentType.collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final CharSequence EntityReferenceImpl.collectTextContent(CharSequence appendTo) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final CharSequence ProcessingInstructionImpl.collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final CharSequence ParentNodeImpl.collectTextContent(CharSequence appendTo) {
        CharSequence content = appendTo;
        for (CoreChildNode node = coreGetFirstChild(); node != null; node = node.coreGetNextSibling()) {
            content = ((DOMNode)node).collectTextContent(content);
        }
        return content;
    }

    public final CharSequence DOMTextNode.collectTextContent(CharSequence appendTo) {
        String data = getData();
        if (appendTo == null) {
            return data;
        } else {
            StringBuilder builder;
            if (appendTo instanceof String) {
                String existing = (String)appendTo;
                builder = new StringBuilder(existing.length() + data.length());
                builder.append(existing);
            } else {
                builder = (StringBuilder)appendTo;
            }
            builder.append(data);
            return builder;
        }
    }

    public final void NodeImpl.setTextContent(String textContent) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
