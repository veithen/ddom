/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.core;

/**
 * Selects elements based on some match rule.
 * 
 * @author Andreas Veithen
 */
public interface ElementMatcher<T extends CoreElement> {
    /**
     * Matches {@link CoreNSAwareElement} nodes by qualified name, i.e. namespace URI and local name.
     */
    ElementMatcher<CoreNSAwareElement> BY_QNAME = new ElementMatcher<CoreNSAwareElement>() {
        public boolean matches(CoreNSAwareElement element, String namespaceURI, String name) throws DeferredBuildingException {
            return name.equals(element.coreGetLocalName())
                    && namespaceURI.equals(element.coreGetNamespaceURI());
        }
    };
    
    /**
     * Matches {@link CoreNSAwareElement} nodes by namespace URI.
     */
    ElementMatcher<CoreNSAwareElement> BY_NAMESPACE_URI = new ElementMatcher<CoreNSAwareElement>() {
        public boolean matches(CoreNSAwareElement element, String namespaceURI, String name) throws DeferredBuildingException {
            return namespaceURI.equals(element.coreGetNamespaceURI());
        }
    };
    
    /**
     * Matches {@link CoreNSAwareElement} nodes by local name.
     */
    ElementMatcher<CoreNSAwareElement> BY_LOCAL_NAME = new ElementMatcher<CoreNSAwareElement>() {
        public boolean matches(CoreNSAwareElement element, String namespaceURI, String name) throws DeferredBuildingException {
            return name.equals(element.coreGetLocalName());
        }
    };
    
    /**
     * Matches elements (of any kind) by tag name.
     */
    ElementMatcher<CoreElement> BY_NAME = new ElementMatcher<CoreElement>() {
        public boolean matches(CoreElement element, String namespaceURI, String name) throws DeferredBuildingException {
            if (element instanceof CoreNSUnawareElement) {
                return name.equals((CoreNSUnawareElement)element);
            } else {
                CoreNSAwareElement nsAwareElement = (CoreNSAwareElement)element;
                String prefix = nsAwareElement.coreGetPrefix();
                int prefixLength = prefix.length();
                String localName = nsAwareElement.coreGetLocalName();
                if (prefixLength == 0) {
                    return name.equals(localName);
                } else {
                    int localNameLength = localName.length();
                    if (prefixLength + localNameLength + 1 == name.length()) {
                        if (name.charAt(prefixLength) != ':') {
                            return false;
                        }
                        for (int i=0; i<localNameLength; i++) {
                            if (name.charAt(prefixLength+i+1) != localName.charAt(i)) {
                                return false;
                            }
                        }
                        for (int i=0; i<prefix.length(); i++) {
                            if (name.charAt(i) != prefix.charAt(i)) {
                                return false;
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    };
    
    /**
     * Check if the given element matches. The values of the <code>namespaceURI</code> and
     * <code>name</code> parameters are those passed to
     * {@link CoreParentNode#coreGetElements(Axis, Class, ElementMatcher, String, String)}.
     * 
     * @param element
     *            the element to check
     * @param namespaceURI
     *            see above
     * @param name
     *            see above
     * @return <code>true</code> if the element matches, <code>false</code> otherwise
     * @throws DeferredBuildingException
     *             If a parsing error occurs while accessing the element.
     */
    boolean matches(T element, String namespaceURI, String name) throws DeferredBuildingException;
}
