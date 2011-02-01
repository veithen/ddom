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

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

public class Attribute {
    public static final int NS_UNAWARE_ATTRIBUTE = 1;
    public static final int NS_AWARE_ATTRIBUTE = 2;
    public static final int NAMESPACE_DECLARATION = 3;
    
    private int attType;
    private String namespaceURI;
    private String localName;
    private String prefix;
    private String type;
    private final StringAccumulator value = new StringAccumulator();
    
    void init(int attType, String namespaceURI, String localName, String prefix, String type) {
        this.attType = attType;
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
        this.type = type;
        value.clear();
    }
    
    void processCharacterData(String data) {
        value.append(data);
    }
    
    public void serialize(XmlHandler handler) throws StreamException {
        switch (attType) {
            case NS_UNAWARE_ATTRIBUTE:
                handler.startAttribute(localName, type);
                break;
            case NS_AWARE_ATTRIBUTE:
                handler.startAttribute(namespaceURI, localName, prefix, type);
                break;
            case NAMESPACE_DECLARATION:
                handler.startNamespaceDeclaration(prefix);
                break;
        }
        if (!value.isEmpty()) {
            handler.processCharacterData(value.toString(), false);
        }
        handler.endAttribute();
    }
}
