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
import com.google.code.ddom.spi.model.ModelLoaderRegistry;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;

// TODO: need a solution to dispose the parser and to close the underlying stream
public class DeferredDocumentFactory {
    private final ModelLoaderRegistry modelLoaderRegistry;
    private final StreamFactory streamFactory;
    
    private DeferredDocumentFactory(ModelLoaderRegistry modelLoaderRegistry, StreamFactory streamFactory) {
        this.modelLoaderRegistry = modelLoaderRegistry;
        this.streamFactory = streamFactory;
    }
    
    public static DeferredDocumentFactory newInstance(ClassLoader classLoader) {
        return new DeferredDocumentFactory(ModelLoaderRegistry.getInstance(classLoader), StreamFactory.getInstance(classLoader));
    }
    
    public static DeferredDocumentFactory newInstance() {
        return new DeferredDocumentFactory(ModelLoaderRegistry.getInstance(), StreamFactory.getInstance());
    }
    
    public DeferredDocument newDocument(String frontend) {
        // TODO: check for null here!
        return modelLoaderRegistry.getDocumentFactory(ModelBuilder.buildModelDefinition(frontend)).createDocument(null);
    }
    
    // TODO: need to make sure that if an exception occurs, all resources (input streams!!) are released properly
    public DeferredDocument parse(String frontend, Object source, Options options, boolean preserve) throws DeferredParsingException {
        // TODO: check for null here!
        DocumentFactory nodeFactory = modelLoaderRegistry.getDocumentFactory(ModelBuilder.buildModelDefinition(frontend));
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

    public DeferredDocument parse(String frontend, Object source, boolean preserve) throws DeferredParsingException {
        return parse(frontend, source, new Options(), preserve);
    }
    
    public DeferredDocument parse(String frontend, Object source, Options options) throws DeferredParsingException {
        return parse(frontend, source, options, true);
    }
    
    public DeferredDocument parse(String frontend, Object source) throws DeferredParsingException {
        return parse(frontend, source, new Options(), true);
    }
}
