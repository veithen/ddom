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
        this.localName = localName;
        this.prefix = prefix;
        this.uri = uri;
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
        // TODO Auto-generated method stub
        return null;
    }
}
