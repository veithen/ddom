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
package com.google.code.ddom.stream.sax;

import java.util.Arrays;

import javax.xml.XMLConstants;

import org.xml.sax.Attributes;

import com.google.code.ddom.stream.spi.AttributeData;

public class SAXAttributeData implements AttributeData {
    private Type[] attributeTypeCache;
    private Attributes atts;

    public void setData(Attributes atts) {
        this.atts = atts;
    }
    
    public void clear() {
        if (attributeTypeCache != null) {
            Arrays.fill(attributeTypeCache, 0, atts.getLength(), null);
        }
    }
    
    public Scope getScope() {
        return Scope.CONSUMER_INVOCATION;
    }

    public String getDataType(int index) {
        return atts.getType(index);
    }

    public int getLength() {
        return atts.getLength();
    }

    public String getName(int index) {
        switch (getType(index)) {
            case DOM1: return atts.getQName(index);
            case DOM2: return atts.getLocalName(index);
            default: return null;
        }
    }

    public String getNamespaceURI(int index) {
        switch (getType(index)) {
            case DOM1: return null;
            case DOM2: return SAXStreamUtils.normalizeNamespaceURI(atts.getURI(index));
            case NS_DECL: return atts.getValue(index);
            default: return null;
        }
    }

    public String getPrefix(int index) {
        switch (getType(index)) {
            case DOM1: return null;
            case DOM2: return SAXStreamUtils.getPrefixFromQName(atts.getQName(index));
            case NS_DECL: return SAXStreamUtils.getDeclaredPrefixFromQName(atts.getQName(index));
            default: return null;
        }
    }

    private int calculateNewCapacity(int currentCapacity) {
        int len = atts.getLength();
        while (currentCapacity < len) {
            currentCapacity <<= 1;
        }
        return currentCapacity;
    }
    
    public Type getType(int index) {
        if (attributeTypeCache == null) {
            attributeTypeCache = new Type[calculateNewCapacity(16)];
        } else if (index >= attributeTypeCache.length) {
            int currentCapacity = attributeTypeCache.length;
            Type[] newAttributeTypes = new Type[calculateNewCapacity(currentCapacity)];
            System.arraycopy(attributeTypeCache, 0, newAttributeTypes, 0, currentCapacity);
            attributeTypeCache = newAttributeTypes;
        }
        Type type = attributeTypeCache[index];
        if (type == null) {
            if (atts.getLocalName(index).length() == 0) {
                type = Type.DOM1;
            } else if (atts.getURI(index).equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
                type = Type.NS_DECL;
            } else {
                type = Type.DOM2;
            }
            attributeTypeCache[index] = type;
        }
        return type;
    }

    public String getValue(int index) {
        return atts.getValue(index);
    }
}
