/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;

import com.google.code.ddom.core.CoreCharacterData;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.DOMCharacterData;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

@Mixin(CoreCharacterData.class)
public abstract class CharacterDataSupport implements DOMCharacterData {
    public final String getData() {
        return coreGetData();
    }
    
    public final void setData(String data) throws DOMException {
        coreSetData(data);
    }
    
    public final int getLength() {
        return getData().length();
    }

    public final void appendData(String arg) throws DOMException {
        setData(getData() + arg);
    }

    public final void deleteData(int offset, int count) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(data.substring(0, offset) + data.substring(Math.min(offset + count, data.length())));
    }

    public final void insertData(int offset, String arg) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(data.substring(0, offset) + arg + data.substring(offset));
    }

    public final void replaceData(int offset, int count, String arg) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(data.substring(0, offset) + arg + data.substring(Math.min(offset + count, data.length())));
    }

    public final String substringData(int offset, int count) throws DOMException {
        String data = getData();
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        return data.substring(offset, Math.min(offset + count, data.length()));
    }

    public final String getTextContent() {
        return coreGetData();
    }

    public final void setTextContent(String textContent) {
        coreSetData(textContent);
    }
    
    public final String getNodeValue() throws DOMException {
        return getData();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        this.setData(nodeValue);
    }
    
    public final CoreElement getNamespaceContext() {
        return coreGetParentElement();
    }
}
