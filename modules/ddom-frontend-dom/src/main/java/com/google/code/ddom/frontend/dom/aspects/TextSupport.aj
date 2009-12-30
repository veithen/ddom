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
import org.w3c.dom.Text;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.CoreTextNode;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect TextSupport {
    public final Text DOMTextNode.splitText(int offset) throws DOMException {
        String text = getData();
        if (offset < 0 || offset > text.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(text.substring(0, offset));
        DOMTextNode newNode = createNewTextNode(text.substring(offset));
        CoreParentNode parent = coreGetParent();
        if (parent != null) {
            // TODO: use coreInsertSiblingAfter here!
            newNode.internalSetNextSibling(coreGetNextSibling());
            internalSetNextSibling(newNode);
            newNode.internalSetParent(parent);
            parent.notifyChildrenModified(1);
        }
        return newNode;
    }
    
    public final String DOMTextNode.getWholeText() {
        DOMTextNode first = getWholeTextStartNode();
        DOMTextNode last = getWholeTextEndNode();
        if (first == last) {
            return first.coreGetData();
        } else {
            StringBuilder buffer = new StringBuilder();
            DOMTextNode current = first;
            while (true) {
                buffer.append(current.coreGetData());
                if (current == last) {
                    break;
                } else {
                    current = (DOMTextNode)current.coreGetNextSibling();
                }
            }
            return buffer.toString();
        }
    }

    private DOMTextNode DOMTextNode.getWholeTextStartNode() {
        DOMTextNode first = this;
        while (true) {
            CoreChildNode sibling = first.coreGetPreviousSibling();
            if (sibling instanceof DOMTextNode) {
                first = (DOMTextNode)sibling;
            } else {
                break;
            }
        }
        return first;
    }
    
    private DOMTextNode DOMTextNode.getWholeTextEndNode() {
        DOMTextNode last = this;
        while (true) {
            CoreChildNode sibling = last.coreGetNextSibling();
            if (sibling instanceof DOMTextNode) {
                last = (DOMTextNode)sibling;
            } else {
                break;
            }
        }
        return last;
    }
    
    public final Text DOMTextNode.replaceWholeText(String content) throws DOMException {
        DOMTextNode newText;
        if (content.length() > 0) {
            CoreDocument document = getDocument();
            newText = (DOMTextNode)document.getNodeFactory().createText(document, content);
        } else {
            newText = null;
        }
        if (coreGetParent() != null) {
            DOMTextNode first = getWholeTextStartNode();
            DOMTextNode last = getWholeTextEndNode();
            if (newText != null) {
                try {
                    first.coreInsertSiblingBefore(newText);
                } catch (CoreModelException ex) {
                    throw DOMExceptionUtil.translate(ex);
                }
            }
            DOMTextNode current = first;
            DOMTextNode next;
            do {
                next = current == last ? null : (DOMTextNode)current.coreGetNextSibling();
                current.coreDetach();
                current = next;
            } while (next != null);
        }
        return newText;
    }

    public final boolean DOMTextNode.isElementContentWhitespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
