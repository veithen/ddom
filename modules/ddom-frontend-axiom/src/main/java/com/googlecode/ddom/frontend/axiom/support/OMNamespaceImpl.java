/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.support;

import org.apache.axiom.om.OMNamespace;

import com.googlecode.ddom.util.lang.ObjectUtils;

public final class OMNamespaceImpl implements OMNamespace {
    private final String namespaceURI;
    private final String prefix;
    
    public OMNamespaceImpl(String namespaceURI, String prefix) {
        this.namespaceURI = namespaceURI;
        this.prefix = prefix;
    }

    public boolean equals(String namespaceURI, String prefix) {
        return this.namespaceURI.equals(namespaceURI) && ObjectUtils.equals(this.prefix, prefix);
    }

    public String getName() {
        return namespaceURI;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof OMNamespace) {
            OMNamespace other = (OMNamespace)obj;
            return ObjectUtils.equals(prefix, other.getPrefix()) && namespaceURI.equals(other.getNamespaceURI());
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return namespaceURI.hashCode() ^ (prefix != null ? prefix.hashCode() : 0);
    }
}
