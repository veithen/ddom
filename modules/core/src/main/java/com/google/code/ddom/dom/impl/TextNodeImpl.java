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
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.google.code.ddom.spi.model.DOMDocument;
import com.google.code.ddom.spi.model.ParentNode;
import com.google.code.ddom.spi.model.TextNode;

public abstract class TextNodeImpl extends CharacterDataImpl implements TextNode {
    public TextNodeImpl(DOMDocument document, String data) {
        super(document, data);
    }

    public final Text splitText(int offset) throws DOMException {
        String text = getData();
        if (offset < 0 || offset > text.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(text.substring(0, offset));
        TextNode newNode = createNewTextNode(text.substring(offset));
        ParentNode parent = getParentNode();
        if (parent != null) {
            newNode.internalSetNextSibling(getNextSibling());
            internalSetNextSibling(newNode);
            newNode.internalSetParent(parent);
            parent.notifyChildrenModified(1);
        }
        return newNode;
    }
    
    protected abstract TextNode createNewTextNode(String data);

    public final Node cloneNode(boolean deep) {
        return createNewTextNode(getData());
    }

    public final CharSequence collectTextContent(CharSequence appendTo) {
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

    public final boolean isElementContentWhitespace() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getWholeText() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Text replaceWholeText(String content) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
