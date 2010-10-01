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
package com.google.code.ddom.frontend.dom;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.Options;
import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.stream.options.NamespaceAwareness;

public class DDOMUtil extends DOMUtil {
    public static final DDOMUtil INSTANCE = new DDOMUtil();
    
    private final ModelDefinition modelDefinition;
    private final DocumentHelper documentHelper;
    
    private DDOMUtil() {
        ModelDefinitionBuilder modelDefinitionBuilder = new ModelDefinitionBuilder();
        modelDefinitionBuilder.addFrontend("dom");
        modelDefinition = modelDefinitionBuilder.buildModelDefinition();
        documentHelper = DocumentHelperFactory.INSTANCE.newInstance();
    }
    
    @Override
    public Document newDocument() {
        return (Document)documentHelper.newDocument(modelDefinition);
    }

    @Override
    public Document parse(boolean namespaceAware, InputSource source) {
        // TODO: need to cleanup somehow
        Options options = new Options();
        options.set(NamespaceAwareness.get(namespaceAware));
        return (Document)documentHelper.parse(modelDefinition, source, options);
    }

    @Override
    public DOMImplementation getDOMImplementation() {
        return documentHelper.getAPIObject(modelDefinition, DOMImplementation.class);
    }
}
