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
package com.google.code.ddom.spi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.code.ddom.DocumentFactory;
import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.spi.ProviderFinder;

public class ModelLoaderRegistry {
    private static final ClassLoaderLocal<ModelLoaderRegistry> registries = new ClassLoaderLocal<ModelLoaderRegistry>();
    
    private final List<ModelLoader> loaders;

    private ModelLoaderRegistry(List<ModelLoader> loaders) {
        this.loaders = loaders;
    }
    
    public static ModelLoaderRegistry getInstance(ClassLoader classLoader) {
        ModelLoaderRegistry registry = registries.get(classLoader);
        if (registry == null) {
            List<ModelLoader> loaders = new ArrayList<ModelLoader>();
            for (Map.Entry<String,ModelLoaderFactory> entry : ProviderFinder.find(classLoader, ModelLoaderFactory.class).entrySet()) {
                loaders.add(entry.getValue().createModelLoader(classLoader));
            }
            registry = new ModelLoaderRegistry(loaders);
            registries.put(classLoader, registry);
        }
        return registry;
    }

    public DocumentFactory getDocumentFactory(ModelDefinition model) {
        // TODO: cache by model definition!
        for (ModelLoader loader : loaders) {
            try {
                DocumentFactory documentFactory = loader.loadModel(model);
                if (documentFactory != null) {
                    return documentFactory;
                }
            } catch (Exception ex) {
                ex.printStackTrace(); // TODO
            }
        }
        throw new RuntimeException(); // TODO
    }
}
