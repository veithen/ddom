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
package com.google.code.ddom.spi.parser;

public interface AttributeData extends Data {
    public enum Type {
        DOM1,
        
        DOM2,
        
        NS_DECL
    }
    
    int getLength();

    Type getType(int index);
    
    /**
     * Get the name of the attribute.
     * 
     * @param index
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#DOM1}</td><td>the name of the attribute</td></tr>
     * <tr><td>{@link Type#DOM2}</td><td>the local part of the attribute name</td></tr>
     * <tr><td>{@link Type#NS_DECL}</td><td>undefined</td>
     * </table>
     */
    String getName(int index);

    /**
     * Get the namespace URI of the attribute.
     * 
     * @param index
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#DOM1}</td><td>undefined</td></tr>
     * <tr><td>{@link Type#DOM2}</td><td>the namespace URI of the attribute, or <code>null</code>
     * if the attribute has no namespace</td></tr>
     * <tr><td>{@link Type#NS_DECL}</td><td>the URI of the namespace that the declaration refers to</td>
     * </table>
     */
    String getNamespaceURI(int index);

    /**
     * Get the prefix of the attribute.
     * 
     * @param index
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#DOM1}</td><td>undefined</td></tr>
     * <tr><td>{@link Type#DOM2}</td><td>the prefix of the attribute, or <code>null</code>
     * if the attribute has no prefix</td></tr>
     * <tr><td>{@link Type#NS_DECL}</td><td>the prefix that the declaration refers to, or <code>null</code>
     * if the declaration declares the default namespace</td>
     * </table>
     */
    String getPrefix(int index);

    /**
     * Get the value of the attribute.
     * 
     * @param index
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#DOM1}</td><td rowspan="2">the value of the attribute</td></tr>
     * <tr><td>{@link Type#DOM2}</td></tr>
     * <tr><td>{@link Type#NS_DECL}</td><td>undefined</td>
     * </table>
     */
    String getValue(int index);
    
    /**
     * Get the data type of the attribute as defined in the DTD.
     * 
     * @param index
     * @return
     * <table border="2" rules="all" cellpadding="4" cellspacing="0">
     * <tr><td>{@link Type#DOM1}</td><td rowspan="2">the data type of the attribute</td></tr>
     * <tr><td>{@link Type#DOM2}</td></tr>
     * <tr><td>{@link Type#NS_DECL}</td><td>undefined</td>
     * </table>
     */
    String getDataType(int index);
}
