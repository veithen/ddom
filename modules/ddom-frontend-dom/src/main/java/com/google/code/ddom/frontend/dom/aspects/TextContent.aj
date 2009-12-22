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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.DOMException;

import com.google.code.ddom.frontend.dom.intf.DOMNode;
import com.google.code.ddom.spi.model.CoreChildNode;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect TextContent {
    public final String DOMNode.getTextContent() throws DOMException {
        CharSequence content = collectTextContent(null);
        return content == null ? "" : content.toString();
    }

    public final CharSequence DOMComment.collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final CharSequence DOMDocumentType.collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final CharSequence DOMEntityReference.collectTextContent(CharSequence appendTo) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final CharSequence DOMProcessingInstruction.collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final CharSequence DOMParentNode.collectTextContent(CharSequence appendTo) {
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

    public final void DOMNode.setTextContent(String textContent) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
