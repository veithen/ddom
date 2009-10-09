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
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;

public aspect CharacterDataSupport {
    declare parents: CharacterDataImpl implements CharacterData;
    declare parents: CommentImpl implements Comment;
    
    public final String CharacterDataImpl.getData() {
        return coreGetData();
    }
    
    public final void CharacterDataImpl.setData(String data) throws DOMException {
        coreSetData(data);
    }
    
    public final int CharacterDataImpl.getLength() {
        return getData().length();
    }

    public final void CharacterDataImpl.appendData(String arg) throws DOMException {
        setData(getData() + arg);
    }

    public final void CharacterDataImpl.deleteData(int offset, int count) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(data.substring(0, offset) + data.substring(Math.min(offset + count, data.length())));
    }

    public final void CharacterDataImpl.insertData(int offset, String arg) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(data.substring(0, offset) + arg + data.substring(offset));
    }

    public final void CharacterDataImpl.replaceData(int offset, int count, String arg) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(data.substring(0, offset) + arg + data.substring(Math.min(offset + count, data.length())));
    }

    public final String CharacterDataImpl.substringData(int offset, int count) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        return data.substring(offset, Math.min(offset + count, data.length()));
    }
}
