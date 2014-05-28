/*
 * Copyright 2009-2011,2013-2014 Andreas Veithen
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
 * {@link AttributeMatcher} implementation that matches attributes based on their namespace URI and
 * local name. Parameters are defined as follows:
 * <dl>
 * <dt><code>namespaceURI</code>
 * <dd>The namespace URI of the attribute.
 * <dt><code>name</code>
 * <dd>The local name of the attribute.
 * <dt><code>value</code>
 * <dd>The attribute value.
 * <dt><code>prefix</code>
 * <dd>The prefix to be used when creating a new attribute or updating an existing one.
 * </dl>
 * If the namespace URI is the emtpy string, then this class will also match namespace unaware
 * attributes. Note that the class doesn't match namespace declarations (for which
 * {@link AttributeMatcher#NAMESPACE_DECLARATION} can be used).
 * 
 * @author Andreas Veithen
 */
public final class NSAwareAttributeMatcher implements AttributeMatcher {
    private final boolean matchNSUnawareAttributes;
    private final boolean updatePrefix;
    
    /**
     * Constructor.
     * 
     * @param matchNSUnawareAttributes
     *            Specifies if {@link CoreNSUnawareAttribute} instances can also be matched. Only
     *            applies to the case where <code>namespaceURI</code> is the empty string.
     * @param updatePrefix
     *            Specifies if the prefix of an existing attribute should be updated (based on the
     *            value of the <code>prefix</code> parameter. If this is <code>false</code>, then
     *            <code>prefix</prefix> is only used when creating new attributes and prefixes of
     * existing attributes are preserved (i.e. only their value is updated).
     */
    public NSAwareAttributeMatcher(boolean matchNSUnawareAttributes, boolean updatePrefix) {
        this.matchNSUnawareAttributes = matchNSUnawareAttributes;
        this.updatePrefix = updatePrefix;
    }
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String name) throws DeferredBuildingException {
        if (attr instanceof CoreNSAwareAttribute) {
            CoreNSAwareAttribute nsAwareAttr = (CoreNSAwareAttribute)attr;
            // Optimization: first compare the local names because they are in general
            // shorter and have higher "uniqueness"
            return name.equals(nsAwareAttr.coreGetLocalName())
                    && namespaceURI.equals(nsAwareAttr.coreGetNamespaceURI());
        } else if (matchNSUnawareAttributes && namespaceURI.length() == 0 && attr instanceof CoreNSUnawareAttribute) {
            return name.equals(((CoreNSUnawareAttribute)attr).coreGetName());
        } else {
            return false;
        }
    }

    public String getNamespaceURI(CoreAttribute attr) throws DeferredBuildingException {
        return ((CoreNSAwareAttribute)attr).coreGetNamespaceURI();
    }

    public String getName(CoreAttribute attr) throws DeferredBuildingException {
        return ((CoreNSAwareAttribute)attr).coreGetLocalName();
    }

    public CoreAttribute createAttribute(NodeFactory nodeFactory, CoreDocument document, String namespaceURI, String name, String prefix, String value) {
        return nodeFactory.createAttribute(document, namespaceURI, name, prefix, value, null);
    }

    public void update(CoreAttribute attr, String prefix, String value) throws DeferredParsingException {
        attr.coreSetValue(value);
        if (updatePrefix && attr instanceof CoreNSAwareAttribute) {
            ((CoreNSAwareAttribute)attr).coreSetPrefix(prefix);
        }
    }
}
