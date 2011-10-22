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

/**
 * Selects nodes based on their type.
 */
public interface Selector {
    /**
     * Selects nodes with any type.
     */
    Selector ANY = new Selector() {
        public boolean select(int nodeType) {
            return true;
        }
    };
    
    /**
     * Selects element nodes.
     */
    Selector ELEMENT = new Selector() {
        public boolean select(int nodeType) {
            return nodeType == CoreNode.NS_AWARE_ELEMENT_NODE || nodeType == CoreNode.NS_UNAWARE_ELEMENT_NODE;
        }
    };
    
    /**
     * Selects nodes with type {@link CoreNode#DOCUMENT_NODE}.
     */
    Selector DOCUMENT = new SimpleSelector(CoreNode.DOCUMENT_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#DOCUMENT_TYPE_DECLARATION_NODE}.
     */
    Selector DOCUMENT_TYPE_DECLARATION = new SimpleSelector(CoreNode.DOCUMENT_TYPE_DECLARATION_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#NS_UNAWARE_ELEMENT_NODE}.
     */
    Selector NS_UNAWARE_ELEMENT = new SimpleSelector(CoreNode.NS_UNAWARE_ELEMENT_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#NS_AWARE_ELEMENT_NODE}.
     */
    Selector NS_AWARE_ELEMENT = new SimpleSelector(CoreNode.NS_AWARE_ELEMENT_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#NS_UNAWARE_ATTRIBUTE_NODE}.
     */
    Selector NS_UNAWARE_ATTRIBUTE = new SimpleSelector(CoreNode.NS_UNAWARE_ATTRIBUTE_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#NS_AWARE_ATTRIBUTE_NODE}.
     */
    Selector NS_AWARE_ATTRIBUTE = new SimpleSelector(CoreNode.NS_AWARE_ATTRIBUTE_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#NAMESPACE_DECLARATION_NODE}.
     */
    Selector NAMESPACE_DECLARATION = new SimpleSelector(CoreNode.NAMESPACE_DECLARATION_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#PROCESSING_INSTRUCTION_NODE}.
     */
    Selector PROCESSING_INSTRUCTION = new SimpleSelector(CoreNode.PROCESSING_INSTRUCTION_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#DOCUMENT_FRAGMENT_NODE}.
     */
    Selector DOCUMENT_FRAGMENT = new SimpleSelector(CoreNode.DOCUMENT_FRAGMENT_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#CHARACTER_DATA_NODE}.
     */
    Selector CHARACTER_DATA = new SimpleSelector(CoreNode.CHARACTER_DATA_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#COMMENT_NODE}.
     */
    Selector COMMENT = new SimpleSelector(CoreNode.COMMENT_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#CDATA_SECTION_NODE}.
     */
    Selector CDATA_SECTION = new SimpleSelector(CoreNode.CDATA_SECTION_NODE);
    
    /**
     * Selects nodes with type {@link CoreNode#ENTITY_REFERENCE_NODE}.
     */
    Selector ENTITY_REFERENCE = new SimpleSelector(CoreNode.ENTITY_REFERENCE_NODE);
    
    /**
     * Check if the given node type is selected by this selector.
     * 
     * @param nodeType
     *            the node type
     * @return <code>true</code> if the node type is selected, <code>false</code> otherwise
     */
    boolean select(int nodeType);
}
