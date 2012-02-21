/*
 * Copyright 2009-2012 Andreas Veithen
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
package com.googlecode.ddom.stream.filter;

import java.util.Map;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.filter.util.XmlHandlerWrapper;

final class NamespaceContextFilterHandler extends XmlHandlerWrapper {
    private final Map<String,String> namespaces;

    NamespaceContextFilterHandler(XmlHandler parent, Map<String,String> namespaces) {
        super(parent);
        this.namespaces = namespaces;
    }

    @Override
    public void startNamespaceDeclaration(String prefix) throws StreamException {
        namespaces.remove(prefix);
        super.startNamespaceDeclaration(prefix);
    }

    @Override
    public void attributesCompleted() throws StreamException {
        for (Map.Entry<String,String> namespace : namespaces.entrySet()) {
            super.startNamespaceDeclaration(namespace.getKey());
            super.processCharacterData(namespace.getValue(), false);
            super.endAttribute();
        }
        super.attributesCompleted();
    }
}
