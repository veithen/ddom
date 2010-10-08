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
package com.google.code.ddom.core.util;

import javax.xml.namespace.QName;

public final class QNameUtil {
    private QNameUtil() {}
    
    /**
     * Extract the namespace URI from a given {@link QName} using the conventions expected by the
     * core model.
     * 
     * @param qname
     *            the QName object to extract the namespace URI from
     * @return the namespace URI, or <code>null</code> if the QName has no namespace (i.e. if
     *         {@link QName#getNamespaceURI()} returns an empty string
     */
    public static String getNamespaceURI(QName qname) {
        String namespaceURI = qname.getNamespaceURI();
        return namespaceURI.length() == 0 ? null : namespaceURI;
    }
    
    /**
     * Extract the namespace prefix from a given {@link QName} using the conventions expected by the
     * core model.
     * 
     * @param qname
     *            the QName object to extract the prefix from
     * @return the prefix, or <code>null</code> if the QName has no prefix (i.e. if
     *         {@link QName#getPrefix()} returns an empty string
     */
    public static String getPrefix(QName qname) {
        String prefix = qname.getPrefix();
        return prefix.length() == 0 ? null : prefix;
    }
}
