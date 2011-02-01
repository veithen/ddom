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
package com.googlecode.ddom.stream.filter.util;

// TODO: this class should eventually be able to handle entity references in attributes as well
public class AttributeBuffer {
    private Attribute[] attributes = new Attribute[16];
    private int count;
    private Attribute currentAttribute;
    
    private void allocateAttribute() {
        if (currentAttribute != null) {
            throw new IllegalStateException();
        }
        int currentCapacity = attributes.length;
        if (currentCapacity == count) {
            Attribute[] newAttributes = new Attribute[currentCapacity*2];
            System.arraycopy(attributes, 0, newAttributes, 0, currentCapacity);
            attributes = newAttributes;
        }
        Attribute attr = attributes[count];
        if (attr == null) {
            attr = new Attribute();
            attributes[count] = attr;
        }
        currentAttribute = attr;
        count++;
    }
    
    public Attribute startAttribute(String name, String type) {
        allocateAttribute();
        currentAttribute.init(Attribute.NS_UNAWARE_ATTRIBUTE, null, name, null, type);
        return currentAttribute;
    }
    
    public Attribute startAttribute(String namespaceURI, String localName, String prefix, String type) {
        allocateAttribute();
        currentAttribute.init(Attribute.NS_AWARE_ATTRIBUTE, namespaceURI, localName, prefix, type);
        return currentAttribute;
    }
    
    public Attribute startNamespaceDeclaration(String prefix) {
        allocateAttribute();
        currentAttribute.init(Attribute.NAMESPACE_DECLARATION, null, null, prefix, null);
        return currentAttribute;
    }
    
    public void processCharacterData(String data) {
        if (currentAttribute == null) {
            throw new IllegalStateException();
        }
        currentAttribute.processCharacterData(data);
    }
    
    public Attribute endAttribute() {
        if (currentAttribute == null) {
            throw new IllegalStateException();
        }
        Attribute attr = currentAttribute;
        currentAttribute = null;
        return attr;
    }
    
    public void clear() {
        if (currentAttribute != null) {
            throw new IllegalStateException();
        }
        count = 0;
    }
    
    public boolean isInAttribute() {
        return currentAttribute != null;
    }
}
