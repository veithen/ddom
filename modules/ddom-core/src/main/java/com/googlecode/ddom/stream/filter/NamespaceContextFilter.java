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

import com.googlecode.ddom.stream.XmlFilter;
import com.googlecode.ddom.stream.XmlHandler;

/**
 * Filter that adds a given set of namespace declarations to the document element. This filter can
 * be use to correct the namespace context if the document is actually a fragment extracted from a
 * larger document.
 * 
 * @author Andreas Veithen
 */
public class NamespaceContextFilter extends XmlFilter {
    private final Map<String,String> namespaces;
    
    public NamespaceContextFilter(Map<String,String> namespaces) {
        this.namespaces = namespaces;
    }

    @Override
    protected XmlHandler createXmlHandler(XmlHandler target) {
        return new NamespaceContextFilterHandler(target, namespaces);
    }
}
