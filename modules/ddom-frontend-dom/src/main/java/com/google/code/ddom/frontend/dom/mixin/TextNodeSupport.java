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
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.DOMTextNode;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.CoreTextNode;

@Mixin({CoreTextNode.class, CoreCDATASection.class})
public abstract class TextNodeSupport implements DOMTextNode {
    public final Text splitText(int offset) throws DOMException {
        String text = getData();
        if (offset < 0 || offset > text.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(text.substring(0, offset));
        DOMTextNode newNode = createNewTextNode(text.substring(offset));
        CoreParentNode parent = coreGetParent();
        if (parent != null) {
            try {
                coreInsertSiblingAfter(newNode);
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
        }
        return newNode;
    }
    
    public final String getWholeText() {
        DOMTextNode first = getWholeTextStartNode();
        DOMTextNode last = getWholeTextEndNode();
        if (first == last) {
            return first.coreGetData();
        } else {
            try {
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
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
        }
    }

    private DOMTextNode getWholeTextStartNode() {
        try {
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
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    private DOMTextNode getWholeTextEndNode() {
        try {
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
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final Text replaceWholeText(String content) throws DOMException {
        DOMTextNode newText;
        if (content.length() > 0) {
            newText = (DOMTextNode)coreGetNodeFactory().createText(coreGetOwnerDocument(true), content);
        } else {
            newText = null;
        }
        if (coreHasParent()) {
            try {
                DOMTextNode first = getWholeTextStartNode();
                DOMTextNode last = getWholeTextEndNode();
                if (newText != null) {
                    first.coreInsertSiblingBefore(newText);
                }
                DOMTextNode current = first;
                DOMTextNode next;
                do {
                    next = current == last ? null : (DOMTextNode)current.coreGetNextSibling();
                    current.coreDetach();
                    current = next;
                } while (next != null);
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
        }
        return newText;
    }

    public final Node cloneNode(boolean deep) {
        return createNewTextNode(getData());
    }
}
