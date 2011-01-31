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
package com.googlecode.ddom.jaxp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.google.code.ddom.Options;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.google.code.ddom.spi.model.Model;
import com.google.code.ddom.spi.model.ModelLoaderException;
import com.google.code.ddom.spi.model.ModelRegistry;
import com.google.code.ddom.stream.options.NamespaceAwareness;

public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    private static final ModelDefinition DOM = ModelDefinitionBuilder.buildModelDefinition("dom");
    private static final ModelRegistry modelRegistry = ModelRegistry.getInstance(DocumentBuilderFactoryImpl.class.getClassLoader());
    
    @Override
    public Object getAttribute(String name) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        Model model;
        try {
            model = modelRegistry.getModel(DOM);
        } catch (ModelLoaderException ex) {
            ParserConfigurationException pce = new ParserConfigurationException("Unable to load model");
            pce.initCause(ex);
            throw pce;
        }
        Options options = new Options();
        options.set(NamespaceAwareness.get(isNamespaceAware()));
// TODO       props.put(XMLInputFactory.IS_VALIDATING, isValidating());
// TODO       private boolean whitespace = false;
// TODO        props.put(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, isExpandEntityReferences());
// TODO        props.put(XMLInputFactory.IS_COALESCING, isCoalescing());
        return new DocumentBuilderImpl(model, options, isIgnoringComments());
    }

    @Override
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
        // TODO Auto-generated method stub
        
    }
}
