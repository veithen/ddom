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
package com.google.code.ddom.impl;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperException;
import com.google.code.ddom.Options;
import com.google.code.ddom.OptionsTracker;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.frontend.APIObjectFactory;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.google.code.ddom.spi.model.Model;
import com.google.code.ddom.spi.model.ModelLoaderException;
import com.google.code.ddom.spi.model.ModelLoaderRegistry;
import com.google.code.ddom.stream.spi.SimpleFragmentSource;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;
import com.google.code.ddom.stream.spi.XmlInput;

// TODO: need a solution to dispose the parser and to close the underlying stream
public class DocumentHelperImpl implements DocumentHelper {
    private final ModelLoaderRegistry modelLoaderRegistry;
    private final StreamFactory streamFactory;
    
    DocumentHelperImpl(ModelLoaderRegistry modelLoaderRegistry, StreamFactory streamFactory) {
        this.modelLoaderRegistry = modelLoaderRegistry;
        this.streamFactory = streamFactory;
    }
    
    public Object newDocument(ModelDefinition modelDefinition) {
        try {
            Model model = modelLoaderRegistry.getModel(modelDefinition);
            return model.getNodeFactory().createDocument();
        } catch (ModelLoaderException ex) {
            throw new DocumentHelperException(ex);
        }
    }
    
    public Object newDocument(String frontend) {
        return newDocument(ModelDefinitionBuilder.buildModelDefinition(frontend));
    }
    
    // TODO: need to make sure that if an exception occurs, all resources (input streams!!) are released properly
    public Object parse(ModelDefinition modelDefinition, Object source, Options options, boolean preserve) {
        // TODO: check for null here!
        Model model;
        try {
            model = modelLoaderRegistry.getModel(modelDefinition);
        } catch (ModelLoaderException ex) {
            throw new DocumentHelperException(ex);
        }
        OptionsTracker tracker = options.createTracker();
        XmlInput input;
        try {
            // TODO: this is bad because we need to reconfigure the underlying parser every time!
            input = streamFactory.getInput(source, tracker, preserve);
        } catch (StreamException ex) {
            // TODO
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        }
        if (input == null) {
            // TODO
            throw new RuntimeException("Don't know how to parse sources of type " + source.getClass().getName(), null);
        }
        tracker.finish();
        CoreDocument document = model.getNodeFactory().createDocument();
        document.coreSetContent(new SimpleFragmentSource(input));
        return document;
    }

    public Object parse(String frontend, Object source, Options options, boolean preserve) {
        return parse(ModelDefinitionBuilder.buildModelDefinition(frontend), source, options, preserve);
    }
    
    public Object parse(String frontend, Object source, boolean preserve) {
        return parse(frontend, source, new Options(), preserve);
    }
    
    public Object parse(String frontend, Object source, Options options) {
        return parse(frontend, source, options, true);
    }
    
    public Object parse(ModelDefinition modelDefinition, Object source, Options options) {
        return parse(modelDefinition, source, options, true);
    }
    
    public Object parse(String frontend, Object source) {
        return parse(frontend, source, new Options(), true);
    }
    
    public void buildDocument(Object document) {
        try {
            ((CoreDocument)document).coreBuild();
        } catch (DeferredParsingException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
    }
    
    public void disposeDocument(Object document) {
        // TODO
    }
    
    public <T> T getAPIObject(ModelDefinition modelDefinition, Class<T> clazz) {
        try {
            APIObjectFactory apiObjectFactory = modelLoaderRegistry.getModel(modelDefinition).getAPIObjectFactory();
            return apiObjectFactory == null ? null : clazz.cast(apiObjectFactory.getAPIObject(clazz));
        } catch (ModelLoaderException ex) {
            throw new DocumentHelperException(ex);
        }
    }
}
