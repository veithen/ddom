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
package com.google.code.ddom.stream.spi;

public interface Event extends Data {
    public enum Type {
        NS_UNAWARE_ELEMENT,
        
        NS_AWARE_ELEMENT,
        
        NS_UNAWARE_ATTRIBUTE,
        
        NS_AWARE_ATTRIBUTE,
        
        NAMESPACE_DECLARATION,
        
        ATTRIBUTES_COMPLETE,
        
        NODE_COMPLETE,
        
        PROCESSING_INSTRUCTION,
        
        CHARACTERS,
        
        SPACE,
        
        CDATA,
        
        ENTITY_REFERENCE,
        
        COMMENT
    }
    
    Type getEventType();
    
    /**
     * 
     * 
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#NS_UNAWARE_ELEMENT}</td><td>the name of the element</td></tr>
     * <tr><td>{@link Type#NS_AWARE_ELEMENT}</td><td>the local part of the element name</td></tr>
     * <tr><td>{@link Type#NS_UNAWARE_ATTRIBUTE}</td><td>the name of the attribute</td></tr>
     * <tr><td>{@link Type#NS_AWARE_ATTRIBUTE}</td><td>the local part of the attribute name</td></tr>
     * <tr><td>{@link Type#PROCESSING_INSTRUCTION}</td><td>the target of the processing instruction</td></tr>
     * <tr><td>{@link Type#ENTITY_REFERENCE}</td><td>the name of the entity reference</td></tr>
     * </table>
     */
    String getName();

    /**
     * 
     * 
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#NS_AWARE_ELEMENT}</td><td>the namespace URI of the element, or <code>null</code>
     * if the element has no namespace</td></tr>
     * <tr><td>{@link Type#NS_AWARE_ATTRIBUTE}</td><td>the namespace URI of the attribute, or <code>null</code>
     * if the attribute has no namespace</td></tr>
     * <tr><td>{@link Type#NAMESPACE_DECLARATION}</td><td>the URI of the namespace that the declaration refers to</td>
     * </table>
     */
    String getNamespaceURI();
    
    /**
     * 
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#NS_AWARE_ELEMENT}</td><td>the prefix of the element, or <code>null</code>
     * if the element has no prefix</td></tr>
     * <tr><td>{@link Type#NS_AWARE_ATTRIBUTE}</td><td>the prefix of the attribute, or <code>null</code>
     * if the attribute has no prefix</td></tr>
     * <tr><td>{@link Type#NAMESPACE_DECLARATION}</td><td>the prefix that the declaration refers to, or <code>null</code>
     * if the declaration declares the default namespace</td>
     * </table>
     */
    String getPrefix();

    /**
     * 
     * Valid for {@link Type#CHARACTERS}, {@link Type#SPACE}, {@link Type#CDATA}, {@link Type#COMMENT}
     * and {@link Type#PROCESSING_INSTRUCTION}.
     * 
     * @return
     */
    CharacterData getData();

    /**
     * 
     * Valid for {@link Type#NS_UNAWARE_ATTRIBUTE} and {@link Type#NS_AWARE_ATTRIBUTE}.
     * 
     * @return
     */
    String getValue();

    /**
     * 
     * Valid for {@link Type#NS_UNAWARE_ATTRIBUTE} and {@link Type#NS_AWARE_ATTRIBUTE}.
     * 
     * @return
     */
    String getDataType();
}
