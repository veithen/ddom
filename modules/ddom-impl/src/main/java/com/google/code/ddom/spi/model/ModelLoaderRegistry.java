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
package com.google.code.ddom.spi.model;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.ProviderFinderException;

public class ModelLoaderRegistry {
    private static final ClassLoaderLocal<ModelLoaderRegistry> registries = new ClassLoaderLocal<ModelLoaderRegistry>();
    
    private final Map<String,ModelLoader> loaders;
    private final Map<ModelDefinition,Model> modelCache = new ConcurrentHashMap<ModelDefinition,Model>();

    private ModelLoaderRegistry(Map<String,ModelLoader> loaders) {
        this.loaders = loaders;
    }
    
    public static ModelLoaderRegistry getInstance(ClassLoader classLoader) {
        ModelLoaderRegistry registry = registries.get(classLoader);
        if (registry == null) {
            Map<String,ModelLoader> loaders = new HashMap<String,ModelLoader>();
            for (Map.Entry<String,ModelLoaderFactory> entry : ProviderFinder.find(classLoader, ModelLoaderFactory.class).entrySet()) {
                loaders.put(entry.getKey(), entry.getValue().createModelLoader(classLoader));
            }
            registry = new ModelLoaderRegistry(loaders);
            registries.put(classLoader, registry);
        }
        return registry;
    }

    public static ModelLoaderRegistry getInstance() throws ProviderFinderException {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }
    
    public Model getModel(ModelDefinition modelDefinition) throws ModelLoaderException {
        Model model = modelCache.get(modelDefinition);
        if (model == null) {
            if (loaders.isEmpty()) {
                throw new ModelLoaderException("Unable to create model; no model loaders have been registered");
            }
            for (ModelLoader loader : loaders.values()) {
                try {
                    model = loader.loadModel(modelDefinition);
                    if (model != null) {
                        break;
                    }
                } catch (ModelLoaderException ex) {
                    throw ex;
                } catch (Throwable ex) {
                    throw new ModelLoaderException("Model loader threw unexpected exception", ex);
                }
            }
            if (model == null) {
                throw new ModelLoaderException("Unable to create model; none of the registered model loaders (" + loaders.keySet() + ") was able to load the model");
            }
            modelCache.put(modelDefinition, model);
        }
        return model;
    }
}
