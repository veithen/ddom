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
package com.googlecode.ddom.backend.linkedlist;

import javax.xml.namespace.QName;

import com.googlecode.ddom.core.CoreNSAwareNamedNode;

public final class NSAwareNamedNodeHelper {
    private NSAwareNamedNodeHelper() {}
    
    public static QName coreGetQName(CoreNSAwareNamedNode node) {
        String namespaceURI = node.coreGetNamespaceURI();
        if (namespaceURI == null) {
            return new QName(node.coreGetLocalName());
        } else {
            String prefix = node.coreGetPrefix();
            if (prefix == null) {
                return new QName(namespaceURI, node.coreGetLocalName());
            } else {
                return new QName(namespaceURI, node.coreGetLocalName(), prefix);
            }
        }
    }
}