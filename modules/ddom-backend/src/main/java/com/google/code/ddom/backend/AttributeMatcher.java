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
package com.google.code.ddom.backend;

/**
 * Selects, creates or updates an attribute based on some match rule.
 * 
 * @author Andreas Veithen
 */
public interface AttributeMatcher {
    /**
     * {@link AttributeMatcher} that matches {@link CoreNamespaceDeclaration} attributes based on
     * the declared prefix. Parameters are defined as follows:
     * <dl>
     * <dt><code>namespaceURI</code>
     * <dd>Not used.
     * <dt><code>name</code>
     * <dd>The prefix declared by the namespace declaration, or <code>null</code> for the default
     * namespace declaration.
     * <dt><code>value</code>
     * <dd>The namespace URI of the namespace declaration.
     * <dt><code>prefix</code>
     * <dd>Not used.
     * </dl>
     * 
     * @author Andreas Veithen
     */
    AttributeMatcher NAMESPACE_DECLARATION = new AttributeMatcher() {
        public boolean matches(CoreAttribute attr, String namespaceURI, String name) {
            if (attr instanceof CoreNamespaceDeclaration) {
                String prefix = ((CoreNamespaceDeclaration)attr).coreGetDeclaredPrefix();
                return name == null && prefix == null || name != null && name.equals(prefix);
            } else {
                return false;
            }
        }

        public CoreAttribute createAttribute(CoreDocument document, String namespaceURI, String name, String prefix, String value) {
            return document.coreCreateNamespaceDeclaration(name, value);
        }

        public void update(CoreAttribute attr, String prefix, String value) {
            try {
                attr.coreSetValue(value);
            } catch (DeferredParsingException ex) {
                // TODO
                throw new RuntimeException(ex);
            }
        }
    };
    
    /**
     * Check if the given attribute matches. The values of the <code>namespaceURI</code> and
     * <code>name</code> parameters are those passed to
     * {@link CoreElement#coreGetAttribute(AttributeMatcher, String, String)},
     * {@link CoreElement#coreSetAttribute(AttributeMatcher, String, String, String, String)} or
     * {@link CoreElement#coreSetAttribute(AttributeMatcher, String, String, CoreAttribute)}. It is
     * not required that these parameters strictly represent the namespace URI and local name of the
     * attribute Their exact meaning is defined by the particular {@link AttributeMatcher}
     * implementation.
     * 
     * @param attr
     *            the attribute to check
     * @param namespaceURI
     *            see above
     * @param name
     *            see above
     * @return <code>true</code> if the attribute matches, <code>false</code> otherwise
     */
    boolean matches(CoreAttribute attr, String namespaceURI, String name);

    /**
     * Create a new attribute node. The values of the <code>namespaceURI</code>, <code>name</code>,
     * <code>prefix</code> and <code>value</code> parameters are those passed to
     * {@link CoreElement#coreSetAttribute(AttributeMatcher, String, String, String, String)}.
     * 
     * @param document
     *            the document in which the attribute is created
     * @param namespaceURI
     *            see above
     * @param name
     *            see above
     * @param prefix
     *            see above
     * @param value
     *            see above
     * @return
     */
    CoreAttribute createAttribute(CoreDocument document, String namespaceURI, String name, String prefix, String value);
    
    /**
     * Update an existing attribute. The values of the <code>prefix</code> and <code>value</code>
     * parameters are those passed to
     * {@link CoreElement#coreSetAttribute(AttributeMatcher, String, String, String, String)}.
     * 
     * @param attr
     *            the attribute to update
     * @param prefix
     *            see above
     * @param value
     *            see above
     */
    void update(CoreAttribute attr, String prefix, String value);
}
