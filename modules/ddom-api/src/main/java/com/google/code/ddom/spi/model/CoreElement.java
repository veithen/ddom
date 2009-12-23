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
package com.google.code.ddom.spi.model;

public interface CoreElement extends CoreChildNode, BuilderTarget, CoreOptimizedParentNode {
    /**
     * Get the first attribute of this element.
     * 
     * @return the first attribute, or <code>null</code> if this element has no attributes
     */
    CoreAttribute coreGetFirstAttribute();
    
    void internalSetFirstAttribute(CoreAttribute attr);
    
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
     * Create or replace an attribute based on a given {@link AttributeMatcher}.
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
     * @return the attribute that was replaced by the new attribute, or <code>null</code> if no
     *         matching attribute existed
     */
    CoreAttribute coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, CoreAttribute attr);
    
    /**
     * Add an attribute to this element. The attribute will be added after the last attribute
     * currently existing on the element. Note that this method will NOT check if an attribute with
     * the same name already exists, and always add the attribute.
     * 
     * @param attr
     *            the attribute to add
     */
    void coreAppendAttribute(CoreAttribute attr);
}
