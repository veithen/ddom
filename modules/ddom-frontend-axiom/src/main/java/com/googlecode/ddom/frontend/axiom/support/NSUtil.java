/*
 * Copyright 2009-2011,2014 Andreas Veithen
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

// TODO: do we really need this?
public class NSUtil {
    private NSUtil() {}
    
    public static String getNamespaceURI(OMNamespace ns) {
        return ns == null ? "" : ns.getNamespaceURI();
    }
    
    public static String getPrefix(OMNamespace ns) {
        return ns == null ? "" : ns.getPrefix();
    }
    
    public static void validateAttributeNamespace(String prefix, String namespaceURI) {
        if (namespaceURI.isEmpty()) {
            if (!prefix.isEmpty()) {
                throw new IllegalArgumentException("Cannot create a prefixed attribute with an empty namespace name");
            }
        } else {
            if (prefix.isEmpty()) {
                throw new IllegalArgumentException("Cannot create an unprefixed attribute with a namespace");
            }
        }
    }
}
