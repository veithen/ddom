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

import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.spi.model.CoreParentNode;
import com.google.code.ddom.spi.model.CoreTextNode;

public aspect TextSupport {
    declare parents: TextNodeImpl implements DOMTextNode;
    declare parents: CDATASectionImpl implements CDATASection;
    
    public final Text DOMTextNode.splitText(int offset) throws DOMException {
        String text = getData();
        if (offset < 0 || offset > text.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(text.substring(0, offset));
        CoreTextNode newNode = createNewTextNode(text.substring(offset));
        CoreParentNode parent = coreGetParent();
        if (parent != null) {
            newNode.internalSetNextSibling(coreGetNextSibling());
            internalSetNextSibling(newNode);
            newNode.internalSetParent(parent);
            parent.notifyChildrenModified(1);
        }
        return (Text)newNode; // TODO
    }
    
    public final String DOMTextNode.getWholeText() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Text DOMTextNode.replaceWholeText(String content) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean DOMTextNode.isElementContentWhitespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
