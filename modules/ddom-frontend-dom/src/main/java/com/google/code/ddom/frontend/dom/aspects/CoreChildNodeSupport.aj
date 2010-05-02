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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.dom.intf.DOMCoreChildNode;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.NodeUtil;

public aspect CoreChildNodeSupport {
    public final Document DOMCoreChildNode.getOwnerDocument() {
        return (Document)coreGetDocument();
    }
    
    public final Node DOMCoreChildNode.getParentNode() {
        return (Node)coreGetParent();
    }

    public final Node DOMCoreChildNode.getNextSibling() {
        try {
            return NodeUtil.toDOM(coreGetNextSibling());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Node DOMCoreChildNode.getPreviousSibling() {
        try {
            return NodeUtil.toDOM(coreGetPreviousSibling());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
}
