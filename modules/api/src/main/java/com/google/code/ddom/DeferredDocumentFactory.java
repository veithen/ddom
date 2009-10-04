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

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;

import com.google.code.ddom.spi.model.ModelRegistry;
import com.google.code.ddom.spi.model.NodeFactory;
import com.google.code.ddom.spi.parser.StreamException;
import com.google.code.ddom.spi.parser.Producer;
import com.google.code.ddom.spi.parser.StreamFactory;

// TODO: need a solution to dispose the parser and to close the underlying stream
public class DeferredDocumentFactory {
    private final ModelRegistry modelRegistry;
    private final StreamFactory streamFactory;
    private final Map<String,Object> properties = new HashMap<String,Object>();
    
    private DeferredDocumentFactory(ModelRegistry modelRegistry, StreamFactory streamFactory) {
        this.modelRegistry = modelRegistry;
        this.streamFactory = streamFactory;
    }
    
    public static DeferredDocumentFactory newInstance(ClassLoader classLoader) {
        return new DeferredDocumentFactory(ModelRegistry.getInstance(classLoader), StreamFactory.getInstance(classLoader));
    }
    
    public static DeferredDocumentFactory newInstance() {
        return new DeferredDocumentFactory(ModelRegistry.getInstance(), StreamFactory.getInstance());
    }
    
    public Document newDocument(String model) {
        // TODO: check for null here!
        return modelRegistry.getNodeFactory(model).createDocument(null);
    }
    
    // TODO: need to make sure that if an exception occurs, all resources (input streams!!) are released properly
    public Document parse(String model, Object source) throws DeferredParsingException {
        // TODO: check for null here!
        NodeFactory nodeFactory = modelRegistry.getNodeFactory(model);
        Producer producer;
        try {
            // TODO: this is bad because we need to reconfigure the underlying parser every time!
            producer = streamFactory.getProducer(source, properties);
        } catch (StreamException ex) {
            throw new DeferredParsingException(ex.getMessage(), ex.getCause());
        }
        if (producer == null) {
            // TODO: maybe a distinct exception here?
            throw new DeferredParsingException("Don't know how to parse sources of type " + source.getClass().getName(), null);
        }
        return nodeFactory.createDocument(producer);
    }
}
