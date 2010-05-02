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
package com.google.code.ddom.frontend.dom.mixin;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.dom.intf.AbortNormalizationException;
import com.google.code.ddom.frontend.dom.intf.DOMNode;
import com.google.code.ddom.frontend.dom.intf.NormalizationConfig;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.UserData;

public abstract class NodeSupport implements DOMNode {
    public final boolean isSupported(String feature, String version) {
        return getDOMImplementation().hasFeature(feature, version);
    }

    public final Object getFeature(String feature, String version) {
        return this;
    }

    public final boolean isSameNode(Node other) {
        return other == this;
    }

    public final boolean isEqualNode(Node other) {
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

    public final String getBaseURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final short compareDocumentPosition(Node other) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Object getUserData(String key) {
        Map<String,UserData> userDataMap = getUserDataMap(false);
        if (userDataMap == null) {
            return null;
        } else {
            UserData userData = userDataMap.get(key);
            return userData == null ? null : userData.getData();
        }
    }

    public final Object setUserData(String key, Object data, UserDataHandler handler) {
        UserData userData;
        if (data == null) {
            Map<String,UserData> userDataMap = getUserDataMap(false);
            if (userDataMap != null) {
                userData = userDataMap.remove(key);
            } else {
                userData = null;
            }
        } else {
            Map<String,UserData> userDataMap = getUserDataMap(true);
            userData = userDataMap.put(key, new UserData(data, handler));
        }
        return userData == null ? null : userData.getData();
    }
    
    public final String lookupNamespaceURI(String prefix) {
        try {
            CoreElement contextElement = getNamespaceContext();
            return contextElement == null ? null : contextElement.coreLookupNamespaceURI(prefix, false);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final String lookupPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            return null;
        } else {
            try {
                CoreElement contextElement = getNamespaceContext();
                return contextElement == null ? null : contextElement.coreLookupPrefix(namespaceURI, false);
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
        }
    }

    public final boolean isDefaultNamespace(String namespaceURI) {
        try {
            CoreElement contextElement = getNamespaceContext();
            return contextElement == null ? false : ObjectUtils.equals(namespaceURI, contextElement.coreLookupNamespaceURI(null, false));
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void normalize() {
        try {
            normalize(NormalizationConfig.DEFAULT);
        } catch (AbortNormalizationException ex) {
            // Do nothing, just abort.
        }
    }
}
