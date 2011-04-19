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
package com.googlecode.ddom.core;

import java.util.Iterator;

import com.googlecode.ddom.stream.XmlSource;

/**
 * Represents an element information item.
 * 
 * @author Andreas Veithen
 */
public interface CoreElement extends CoreChildNode, CoreParentNode {
    /**
     * Specifies the value that should be returned by
     * {@link CoreElement#coreSetAttribute(AttributeMatcher, String, String, CoreAttribute, NodeMigrationPolicy, ReturnValue)}.
     * .
     */
    public enum ReturnValue {
        /**
         * Nothing should be returned.
         */
        NONE,
        
        /**
         * The method will return the attribute that was effectively added to the element.
         */
        ADDED_ATTRIBUTE,
        
        /**
         * The method will return the attribute that was replaced by the new attribute, or
         * <code>null</code> if no matching attribute existed
         */
        REPLACED_ATTRIBUTE,
    }
    
    /**
     * Set the source of this element. The effect of this method is similar to
     * {@link CoreParentNode#coreSetContent(XmlSource)}, except that the provided {@link XmlSource}
     * object is expected to describe the element itself, not its content. When the source is
     * expanded, the name (tag name for a namespace unaware element; namespace URI, local name and
     * prefix for a namespace aware element) of the root element information item are checked for
     * consistency with the corresponding values set during the construction of this node.
     * <p>
     * If during construction of this element, the name (or one or more of its components) was left
     * unspecified, then it will be determined lazily by expanding the source of the element set
     * with this method.
     * 
     * @param source
     *            the source for the element
     * 
     * @see NodeFactory#createElement(CoreDocument, String)
     * @see NodeFactory#createElement(CoreDocument, String, String, String)
     * @see CoreNSUnawareNamedNode#coreGetName()
     * @see CoreNSAwareNamedNode#coreGetNamespaceURI()
     * @see CoreNSAwareNamedNode#coreGetLocalName()
     * @see CoreNSAwareNamedNode#coreGetPrefix()
     */
    void coreSetSource(XmlSource source);
    
    /**
     * Get the first attribute of this element.
     * 
     * @return the first attribute, or <code>null</code> if this element has no attributes
     * @throws DeferredParsingException 
     */
    CoreAttribute coreGetFirstAttribute() throws DeferredParsingException;
    
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
     * @throws DeferredParsingException
     *             If an error occurs during deferred parsing.
     */
    CoreAttribute coreGetAttribute(AttributeMatcher matcher, String namespaceURI, String name) throws DeferredParsingException;
    
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
     * @throws DeferredParsingException 
     *             If an error occurs during deferred parsing.
     */
    void coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, String prefix, String value) throws DeferredParsingException;
    
    /**
     * Add a new attribute or replace an existing attribute based on a given
     * {@link AttributeMatcher}. If a matching attribute on this element is found, it is replaced by
     * the specified attribute. If no matching attribute is found, then the specified attribute is
     * added to this element. If the attribute is already owned by this element, then calling this method
     * has no effect.
     * 
     * @param matcher
     *            the {@link AttributeMatcher} implementation to use
     * @param attr
     *            the new attribute to add
     * @param policy
     *            the policy to apply if the attribute already has an owner element or belongs to a
     *            different document
     * @param returnValue
     *            specifies the expected return value of the method
     * @return the attribute as specified by the <code>returnValue</code> parameter
     * @throws NodeMigrationException 
     * @throws DeferredParsingException 
     *             If an error occurs during deferred parsing.
     */
    CoreAttribute coreSetAttribute(AttributeMatcher matcher, CoreAttribute attr, NodeMigrationPolicy policy, ReturnValue returnValue) throws NodeMigrationException, DeferredParsingException;
    
    /**
     * Append an attribute to this element. The attribute is simply added at the end of the list of
     * attributes for this element. This method should be used with care because no provisions are
     * made to ensure uniqueness of attribute names.
     * 
     * @param attr
     *            the attribute to append
     * @param policy
     *            the policy to apply if the attribute already has an owner element or belongs to a
     *            different document
     * @throws NodeMigrationException
     *             if appending the attribute was rejected by the policy
     */
    void coreAppendAttribute(CoreAttribute attr, NodeMigrationPolicy policy) throws NodeMigrationException;
    
    /**
     * Remove an attribute based on a given {@link AttributeMatcher}.
     * 
     * @param matcher
     *            the {@link AttributeMatcher} implementation to use
     * @param namespaceURI
     *            the <code>namespaceURI</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)}
     * @param name
     *            the <code>name</code> parameter to pass to
     *            {@link AttributeMatcher#matches(CoreAttribute, String, String)}
     * @return <code>true</code> if a matching attribute was found (and has been removed),
     *         <code>false</code> if no matching attribute was found
     * @throws DeferredParsingException 
     *             If an error occurs during deferred parsing.
     */
    boolean coreRemoveAttribute(AttributeMatcher matcher, String namespaceURI, String name) throws DeferredParsingException;
    
    /**
     * Look up the namespace URI associated to the given prefix.
     * 
     * @param prefix
     *            The prefix to look for. If this parameter is the empty string, then the URI of the
     *            default namespace will be returned.
     * @param strict
     *            If this parameter is set to <code>true</code>, only namespace declarations will be
     *            taken into account. If set to <code>false</code> the prefixes of the element and
     *            its ancestors are also taken into account (limited to instanced of
     *            {@link CoreNSAwareElement}), even if no explicit namespace declarations exists for
     *            these prefixes.
     * @return the namespace URI or <code>null</code> if the prefix is not bound; if the prefix is
     *         the empty string and no default namespace declaration exists, then an empty string is
     *         returned
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
