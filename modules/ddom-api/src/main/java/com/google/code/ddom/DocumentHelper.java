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
package com.google.code.ddom;

import com.google.code.ddom.model.ModelBuilder;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.spi.model.ModelLoaderRegistry;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;

// TODO: need a solution to dispose the parser and to close the underlying stream
public class DocumentHelper {
    private final ModelLoaderRegistry modelLoaderRegistry;
    private final StreamFactory streamFactory;
    
    private DocumentHelper(ModelLoaderRegistry modelLoaderRegistry, StreamFactory streamFactory) {
        this.modelLoaderRegistry = modelLoaderRegistry;
        this.streamFactory = streamFactory;
    }
    
    /**
     * Get the default instance. In a non OSGi environment, this instance will have access to all
     * frontend, backend and stream providers visible in the class loader from which the
     * {@link DocumentHelper} class is loaded. In an OSGi environment, this instance will use all
     * providers in registered bundles. TODO: make this more precise
     * 
     * @return the default instance
     */
    public static DocumentHelper getDefaultInstance() {
        // TODO
        return null;
    }
    
    public static DocumentHelper newInstance(ClassLoader classLoader) {
        return new DocumentHelper(ModelLoaderRegistry.getInstance(classLoader), StreamFactory.getInstance(classLoader));
    }
    
    public static DocumentHelper newInstance() {
        return new DocumentHelper(ModelLoaderRegistry.getInstance(), StreamFactory.getInstance());
    }
    
    public DeferredDocument newDocument(ModelDefinition model) {
        return modelLoaderRegistry.getDocumentFactory(model).createDocument(null);
    }
    
    public DeferredDocument newDocument(String frontend) {
        return newDocument(ModelBuilder.buildModelDefinition(frontend));
    }
    
    // TODO: need to make sure that if an exception occurs, all resources (input streams!!) are released properly
    public DeferredDocument parse(ModelDefinition model, Object source, Options options, boolean preserve) throws DeferredParsingException {
        // TODO: check for null here!
        DocumentFactory nodeFactory = modelLoaderRegistry.getDocumentFactory(model);
        OptionsTracker tracker = options.createTracker();
        Producer producer;
        try {
            // TODO: this is bad because we need to reconfigure the underlying parser every time!
            producer = streamFactory.getProducer(source, tracker, preserve);
        } catch (StreamException ex) {
            throw new DeferredParsingException(ex.getMessage(), ex.getCause());
        }
        if (producer == null) {
            // TODO: maybe a distinct exception here?
            throw new DeferredParsingException("Don't know how to parse sources of type " + source.getClass().getName(), null);
        }
        tracker.finish();
        return nodeFactory.createDocument(producer);
    }

    public DeferredDocument parse(String frontend, Object source, Options options, boolean preserve) throws DeferredParsingException {
        return parse(ModelBuilder.buildModelDefinition(frontend), source, options, preserve);
    }
    
    public DeferredDocument parse(String frontend, Object source, boolean preserve) throws DeferredParsingException {
        return parse(frontend, source, new Options(), preserve);
    }
    
    public DeferredDocument parse(String frontend, Object source, Options options) throws DeferredParsingException {
        return parse(frontend, source, options, true);
    }
    
    public DeferredDocument parse(String frontend, Object source) throws DeferredParsingException {
        return parse(frontend, source, new Options(), true);
    }
    
    public void disposeDocument(Object document) {
        // TODO
    }
}
