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

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.frontend.dom.intf.*;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.EmptyNodeList;

public aspect LeafNodeSupport {
    public final boolean DOMLeafNode.hasAttributes() {
        return false;
    }

    public final NamedNodeMap DOMLeafNode.getAttributes() {
        return null;
    }

    public final Node DOMLeafNode.getFirstChild() {
        return null;
    }
    
    public final Node DOMLeafNode.getLastChild() {
        return null;
    }

    public final boolean DOMLeafNode.hasChildNodes() {
        return false;
    }
    
    public final NodeList DOMLeafNode.getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    public final Node DOMLeafNode.appendChild(@SuppressWarnings("unused") Node newChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
    }

    public final Node DOMLeafNode.insertBefore(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node refChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node DOMLeafNode.removeChild(@SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node DOMLeafNode.replaceChild(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }
}
