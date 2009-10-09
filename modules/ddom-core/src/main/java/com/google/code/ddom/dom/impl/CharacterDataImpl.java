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

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

import com.google.code.ddom.spi.model.CoreDocument;

public abstract class CharacterDataImpl extends LeafNode implements CharacterData {
    private String data;

    public CharacterDataImpl(CoreDocument document, String data) {
        super(document);
        this.data = data;
    }

    public final String getData() {
        return data;
    }

    public final void setData(String data) throws DOMException {
        this.data = data;
    }

    public final String getNodeValue() throws DOMException {
        return getData();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        this.setData(nodeValue);
    }

    public final int getLength() {
        return data.length();
    }

    public final void appendData(String arg) throws DOMException {
        data += arg;
    }

    public final void deleteData(int offset, int count) throws DOMException {
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        data = data.substring(0, offset) + data.substring(Math.min(offset + count, data.length()));
    }

    public final void insertData(int offset, String arg) throws DOMException {
        if (offset < 0 || offset > data.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        data = data.substring(0, offset) + arg + data.substring(offset);
    }

    public final void replaceData(int offset, int count, String arg) throws DOMException {
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        data = data.substring(0, offset) + arg + data.substring(Math.min(offset + count, data.length()));
    }

    public final String substringData(int offset, int count) throws DOMException {
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        return data.substring(offset, Math.min(offset + count, data.length()));
    }
}
