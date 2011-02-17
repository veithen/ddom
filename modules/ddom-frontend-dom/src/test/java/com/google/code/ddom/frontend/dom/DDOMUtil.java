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
package com.google.code.ddom.frontend.dom;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.code.ddom.Options;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.StreamFactory;
import com.googlecode.ddom.stream.options.NamespaceAwareness;

public class DDOMUtil extends DOMUtil {
    public static final DDOMUtil INSTANCE = new DDOMUtil();
    
    private final Model model;
    private final StreamFactory streamFactory;
    
    private DDOMUtil() {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(DDOMUtil.class.getClassLoader());
        try {
            model = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("dom"));
        } catch (ModelLoaderException ex) {
            throw new Error(ex);
        }
        streamFactory = StreamFactory.getInstance(DDOMUtil.class.getClassLoader());
    }
    
    @Override
    public Document newDocument() {
        return (Document)model.getNodeFactory().createDocument();
    }

    @Override
    public Document parse(boolean namespaceAware, InputSource source) {
        DOMDocument document = (DOMDocument)model.getNodeFactory().createDocument();
        // TODO: need to cleanup somehow
        Options options = new Options();
        options.set(NamespaceAwareness.get(namespaceAware));
        try {
            document.coreSetContent(streamFactory.getSource(source, options, false));
        } catch (StreamException ex) {
            throw new Error(ex);
        }
        return document;
    }

    @Override
    public DOMImplementation getDOMImplementation() {
        return (DOMImplementation)model.getAPIObjectFactory().getAPIObject(DOMImplementation.class);
    }
}
