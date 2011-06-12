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
package com.googlecode.ddom.frontend.saaj.support;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;

public interface NameFactory<T> {
    NameFactory<QName> QNAME = new NameFactory<QName>() {
        public QName createName(String localName) {
            return new QName(localName);
        }

        public QName createName(String namespaceURI, String localName) {
            return new QName(namespaceURI, localName);
        }

        public QName createName(String namespaceURI, String localName, String prefix) {
            return new QName(namespaceURI, localName, prefix);
        }
    };
    
    NameFactory<Name> NAME = new NameFactory<Name>() {
        public Name createName(String localName) {
            return new NameImpl(localName, "", "");
        }

        public Name createName(String namespaceURI, String localName) {
            return new NameImpl(localName, "", namespaceURI);
        }

        public Name createName(String namespaceURI, String localName, String prefix) {
            return new NameImpl(localName, prefix, namespaceURI);
        }
    };

    T createName(String localName);
    T createName(String namespaceURI, String localName);
    T createName(String namespaceURI, String localName, String prefix);
}
