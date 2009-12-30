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
package com.google.code.ddom.frontend.dom;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.code.ddom.DeferredDocumentFactory;

public class DDOMUtilImpl extends DOMUtilImpl {
    public static final DDOMUtilImpl INSTANCE = new DDOMUtilImpl();
    
    @Override
    public Document newDocument() {
        return (Document)DeferredDocumentFactory.newInstance().newDocument("dom");
    }

    @Override
    public Document parse(boolean namespaceAware, InputSource source) {
        // TODO: need to cleanup somehow
        Map<String,Object> properties = new HashMap<String,Object>();
        properties.put(XMLInputFactory.IS_NAMESPACE_AWARE, namespaceAware);
        return (Document)DeferredDocumentFactory.newInstance().parse("dom", source, properties);
    }
}
