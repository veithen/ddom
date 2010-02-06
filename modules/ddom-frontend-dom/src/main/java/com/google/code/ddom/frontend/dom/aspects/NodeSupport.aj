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

import org.apache.commons.lang.ObjectUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.frontend.dom.intf.DOMDocument;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect NodeSupport {
    public final DOMImplementation DOMCoreNode.getDOMImplementation() {
        return ((DOMDocument)coreGetDocument()).getImplementation();
    }
    
    public final boolean DOMNode.isSupported(String feature, String version) {
        return getDOMImplementation().hasFeature(feature, version);
    }

    public final Object DOMNode.getFeature(String feature, String version) {
        return this;
    }

    public final boolean DOMNode.isSameNode(Node other) {
        return other == this;
    }

    public final boolean DOMNode.isEqualNode(Node other) {
        // Note: We may not assume that the "other" node has been created by DDOM. Therefore we
        //       must only use standard DOM methods on that node.
        if (getNodeType() != other.getNodeType()
                || !ObjectUtils.equals(getNodeName(), other.getNodeName())
                || !ObjectUtils.equals(getLocalName(), other.getLocalName())
                || !ObjectUtils.equals(getNamespaceURI(), other.getNamespaceURI())
                || !ObjectUtils.equals(getPrefix(), other.getPrefix())
                || !ObjectUtils.equals(getNodeValue(), other.getNodeValue())) {
            return false;
        }
        NodeList children = getChildNodes();
        NodeList otherChildren = other.getChildNodes();
        if ((children == null) != (otherChildren == null)) {
            return false;
        }
        if (children != null) {
            int length = children.getLength();
            if (length != otherChildren.getLength()) {
                return false;
            }
            for (int i=0; i<length; i++) {
                if (!children.item(i).isEqualNode(otherChildren.item(i))) {
                    return false;
                }
            }
        }
        // TODO: attributes (seems that this is not covered by the DOM3 test suite)
        return true;
    }

    public final String DOMNode.getBaseURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final short DOMNode.compareDocumentPosition(Node other) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
