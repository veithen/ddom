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

import java.util.Iterator;

public interface CoreElement extends CoreChildNode, CoreParentNode {
    /**
     * Get the first attribute of this element.
     * 
     * @return the first attribute, or <code>null</code> if this element has no attributes
     */
    CoreAttribute coreGetFirstAttribute();
    
    /**
     * Get the last attribute of this element.
     * 
     * @return the last attribute, or <code>null</code> if this element has no attributes
     */
    CoreAttribute coreGetLastAttribute();
    
    /**
     * Get the first attribute selected by a given {@link AttributeMatcher}.
     * 
     * @param matcher
     *            the {@link AttributeMatcher} implementation to use
     * @param namespaceURI
     *            the <code>namespaceURI</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)}
     * @param name
     *            the <code>name</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)}
     * @return the (first) matching attribute, or <code>null</code> if no matching attribute was
     *         found
     */
    CoreAttribute coreGetAttribute(AttributeMatcher matcher, String namespaceURI, String name);
    
    /**
     * Create or update an attribute based on a given {@link AttributeMatcher}.
     * 
     * @param matcher
     *            the {@link AttributeMatcher} implementation to use
     * @param namespaceURI
     *            the <code>namespaceURI</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)} and
     *            {@link AttributeMatcher#createAttribute(NodeFactory, CoreDocument, String, String, String, String)}
     * @param name
     *            the <code>name</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)} and
     *            {@link AttributeMatcher#createAttribute(NodeFactory, CoreDocument, String, String, String, String)}
     * @param prefix
     *            the <code>prefix</code> parameter to pass to
     *            {@link AttributeMatcher#createAttribute(NodeFactory, CoreDocument, String, String, String, String)}
     *            and {@link AttributeMatcher#update(CoreAttribute, String, String)}
     * @param value
     *            the <code>value</code> parameter to pass to
     *            {@link AttributeMatcher#createAttribute(NodeFactory, CoreDocument, String, String, String, String)}
     *            and {@link AttributeMatcher#update(CoreAttribute, String, String)}
     */
    void coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, String prefix, String value);
    
    /**
     * Add a new attribute or replace an existing attribute based on a given
     * {@link AttributeMatcher}. If a matching attribute on this element is found, it is replaced by
     * the specified attribute. If no matching attribute is found, then the specified attribute is
     * added to this element.
     * 
     * @param matcher
     *            the {@link AttributeMatcher} implementation to use
     * @param namespaceURI
     *            the <code>namespaceURI</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)}
     * @param name
     *            the <code>name</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)}
     * @param attr
     *            the new attribute to add
     * @param policy
     *            the policy to apply if the attribute already has an owner element or belongs to a
     *            different document
     * @return the attribute that was replaced by the new attribute, or <code>null</code> if no
     *         matching attribute existed
     * @throws NodeMigrationException 
     */
    CoreAttribute coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, CoreAttribute attr, NodeMigrationPolicy policy) throws NodeMigrationException;
    
    /**
     * Look up the namespace URI associated to the given prefix.
     * 
     * @param prefix
     *            The prefix to look for. If this parameter is <code>null</code> then the URI of the
     *            default namespace will be returned.
     * @param strict
     *            If this parameter is set to <code>true</code>, only namespace declarations will be
     *            taken into account. If set to <code>false</code> the prefixes of the element and
     *            its ancestors are also taken into account (limited to instanced of
     *            {@link CoreNSAwareElement}), even if no explicit namespace declarations exists for
     *            these prefixes.
     * @return the namespace URI or <code>null</code> if none is found
     * @throws DeferredParsingException 
     */
    String coreLookupNamespaceURI(String prefix, boolean strict) throws DeferredParsingException;
    
    /**
     * Find a prefix associated to the given namespace URI. Default namespaces are not taken into
     * account by this method.
     * 
     * @param namespaceURI
     *            The namespace URI to look for. This parameter must not be <code>null</code> (XML
     *            forbids to bind a prefix to the null namespace).
     * @param strict
     *            If this parameter is set to <code>true</code>, only namespace declarations will be
     *            taken into account. If set to <code>false</code> the prefixes of the element and
     *            its ancestors are also taken into account (limited to instanced of
     *            {@link CoreNSAwareElement}), even if no explicit namespace declarations exists for
     *            these prefixes.
     * @return a prefix bound to the given namespace URI or <code>null</code> if none is found
     * @throws DeferredParsingException 
     * @throws IllegalArgumentException
     *             if <code>namespaceURI</code> is <code>null</code>
     */
    String coreLookupPrefix(String namespaceURI, boolean strict) throws DeferredParsingException;
    
    /**
     * Coalesce child text nodes, and optionally CDATA sections. This method replaces groups of
     * adjacent text nodes by single text nodes. If <code>includeCDATASections</code> is
     * <code>true</code>, then CDATA sections are treated as text nodes.
     * 
     * @param includeCDATASections
     *            <code>true</code> if CDATA sections should also be coalesced
     * @throws DeferredParsingException 
     */
    void coreCoalesce(boolean includeCDATASections) throws DeferredParsingException;
    
    <T extends CoreAttribute,S> Iterator<S> coreGetAttributesByType(Class<T> type, Mapper<T,S> mapper);
}
