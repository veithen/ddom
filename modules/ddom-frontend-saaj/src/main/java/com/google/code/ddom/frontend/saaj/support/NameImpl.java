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
package com.google.code.ddom.frontend.saaj.support;

import javax.xml.soap.Name;

public class NameImpl implements Name {
    private final String localName;
    private final String prefix;
    private final String uri;
    
    public NameImpl(String localName, String prefix, String uri) {
        if (localName == null || localName.length() == 0) {
            throw new IllegalArgumentException("localName can't be null or empty");
        }
        this.localName = localName;
        this.prefix = prefix == null ? "" : prefix;
        this.uri = uri == null ? "" : uri;
    }

    public String getLocalName() {
        return localName;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getURI() {
        return uri;
    }
    
    public String getQualifiedName() {
        return prefix.length() == 0 ? localName : prefix + ":" + localName;
    }
}
