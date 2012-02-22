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
package com.googlecode.ddom.jaxp;

import javax.xml.parsers.ParserConfigurationException;

import com.googlecode.ddom.frontend.dom.intf.DOMNodeFactory;
import com.googlecode.ddom.model.ModelDefinition;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;

/**
 * Standard JAXP document builder factory implementation that uses the DOM front-end.
 * 
 * @author Andreas Veithen
 */
public class DocumentBuilderFactoryImpl extends AbstractDocumentBuilderFactory {
    private static final ModelDefinition DOM = ModelDefinitionBuilder.buildModelDefinition("dom");
    private static final ModelRegistry modelRegistry = ModelRegistry.getInstance(DocumentBuilderFactoryImpl.class.getClassLoader());
    
    @Override
    protected DOMNodeFactory getNodeFactory() throws ParserConfigurationException {
        try {
            return (DOMNodeFactory)modelRegistry.getModel(DOM).getNodeFactory();
        } catch (ModelLoaderException ex) {
            ParserConfigurationException pce = new ParserConfigurationException("Unable to load model");
            pce.initCause(ex);
            throw pce;
        }
    }
}
