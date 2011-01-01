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
package com.google.code.ddom.stream.spi.buffer;

public class Event {
    public enum Type {
        START_NS_UNAWARE_ELEMENT,
        
        START_NS_AWARE_ELEMENT,
        
        END_ELEMENT,
        
        NS_UNAWARE_ATTRIBUTE,
        
        NS_AWARE_ATTRIBUTE,
        
        NAMESPACE_DECLARATION,
        
        ATTRIBUTES_COMPLETE,
        
        COMPLETED,
        
        PROCESSING_INSTRUCTION,
        
        CHARACTERS,
        
        SPACE,
        
        START_CDATA_SECTION,
        
        END_CDATA_SECTION,
        
        ENTITY_REFERENCE,
        
        COMMENT
    }
    
    private Type eventType;
    private String name;
    private String namespaceURI;
    private String prefix;
    private String data;
    private String dataType;
    
    void init(Type eventType, String name, String namespaceURI, String prefix, String data, String dataType) {
        this.eventType = eventType;
        this.name = name;
        this.namespaceURI = namespaceURI;
        this.prefix = prefix;
        this.data = data;
        this.dataType = dataType;
    }

    void reset() {
        eventType = null;
        name = null;
        namespaceURI = null;
        data = null;
        dataType = null;
    }
    
    public Type getEventType() {
        return eventType;
    }
    
    /**
     * Get the name of the information item represented by this event.
     * 
     * @return The return value depends on the event type:
     * <p>
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#START_NS_UNAWARE_ELEMENT}</td><td>the name of the element</td></tr>
     * <tr><td>{@link Type#START_NS_AWARE_ELEMENT}</td><td>the local part of the element name</td></tr>
     * <tr><td>{@link Type#NS_UNAWARE_ATTRIBUTE}</td><td>the name of the attribute</td></tr>
     * <tr><td>{@link Type#NS_AWARE_ATTRIBUTE}</td><td>the local part of the attribute name</td></tr>
     * <tr><td>{@link Type#PROCESSING_INSTRUCTION}</td><td>the target of the processing instruction</td></tr>
     * <tr><td>{@link Type#ENTITY_REFERENCE}</td><td>the name of the entity reference</td></tr>
     * </table>
     * <p>
     * For all other event types, the behavior of this method is undefined.
     * <p>
     * The return value is canonicalized using the symbol table returned by {@link Input#getSymbols()}.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the namespace URI of the information item represented by this event.
     * 
     * @return The return value depends on the event type:
     * <p>
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#START_NS_AWARE_ELEMENT}</td><td>the namespace URI of the element, or <code>null</code>
     * if the element has no namespace</td></tr>
     * <tr><td>{@link Type#NS_AWARE_ATTRIBUTE}</td><td>the namespace URI of the attribute, or <code>null</code>
     * if the attribute has no namespace</td></tr>
     * <tr><td>{@link Type#NAMESPACE_DECLARATION}</td><td>the URI of the namespace that the declaration refers to</td>
     * </table>
     * <p>
     * For all other event types, the behavior of this method is undefined.
     * <p>
     * The return value is canonicalized using the symbol table returned by {@link Input#getSymbols()}.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }
    
    /**
     * Get the namespace prefix of the information item represented by this event.
     * 
     * @return The return value depends on the event type:
     * <p>
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#START_NS_AWARE_ELEMENT}</td><td>the prefix of the element, or <code>null</code>
     * if the element has no prefix</td></tr>
     * <tr><td>{@link Type#NS_AWARE_ATTRIBUTE}</td><td>the prefix of the attribute, or <code>null</code>
     * if the attribute has no prefix</td></tr>
     * <tr><td>{@link Type#NAMESPACE_DECLARATION}</td><td>the prefix that the declaration refers to, or <code>null</code>
     * if the declaration declares the default namespace</td>
     * </table>
     * <p>
     * For all other event types, the behavior of this method is undefined.
     * <p>
     * The return value is canonicalized using the symbol table returned by {@link Input#getSymbols()}.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 
     * Valid for {@link Type#CHARACTERS}, {@link Type#SPACE}, {@link Type#START_CDATA_SECTION}, {@link Type#COMMENT},
     * {@link Type#PROCESSING_INSTRUCTION}, {@link Type#NS_UNAWARE_ATTRIBUTE} and {@link Type#NS_AWARE_ATTRIBUTE}.
     * 
     * @return
     */
    public String getData() {
        return data;
    }

    /**
     * 
     * Valid for {@link Type#NS_UNAWARE_ATTRIBUTE} and {@link Type#NS_AWARE_ATTRIBUTE}.
     * 
     * @return
     */
    public String getDataType() {
        return dataType;
    }
}
