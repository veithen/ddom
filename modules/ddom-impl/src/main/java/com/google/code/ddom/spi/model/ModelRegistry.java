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
package com.google.code.ddom.spi.model;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.google.code.ddom.model.ModelDefinition;
import com.googlecode.ddom.spi.ProviderFinder;
import com.googlecode.ddom.spi.ProviderFinderException;

/**
 * Maps {@link ModelDefinition} instances to {@link Model} instances.
 * 
 * @author Andreas Veithen
 */
public class ModelRegistry {
    private static final ClassLoaderLocal<ModelRegistry> registries = new ClassLoaderLocal<ModelRegistry>();
    
    private final Map<String,ModelLoader> loaders;
    private final Map<ModelDefinition,Model> modelCache = new ConcurrentHashMap<ModelDefinition,Model>();

    private ModelRegistry(Map<String,ModelLoader> loaders) {
        this.loaders = loaders;
    }
    
    public static ModelRegistry getInstance(ClassLoader classLoader) {
        ModelRegistry registry = registries.get(classLoader);
        if (registry == null) {
            Map<String,ModelLoader> loaders = new HashMap<String,ModelLoader>();
            for (Map.Entry<String,ModelLoaderFactory> entry : ProviderFinder.find(classLoader, ModelLoaderFactory.class).entrySet()) {
                loaders.put(entry.getKey(), entry.getValue().createModelLoader(classLoader));
            }
            registry = new ModelRegistry(loaders);
            registries.put(classLoader, registry);
        }
        return registry;
    }

    public static ModelRegistry getInstance() throws ProviderFinderException {
        return getInstance(AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        }));
    }
    
    /**
     * Get a {@link Model} instance for a given definition. This method will either return an
     * previously instantiated model with the same definition or locate an appropriate
     * {@link ModelLoader} to create a new instance.
     * 
     * @param definition
     *            the model definition
     * @return the model
     * @throws ModelLoaderException
     */
    public Model getModel(ModelDefinition definition) throws ModelLoaderException {
        Model model = modelCache.get(definition);
        if (model == null) {
            if (loaders.isEmpty()) {
                throw new ModelLoaderException("Unable to create model; no model loaders have been registered");
            }
            for (ModelLoader loader : loaders.values()) {
                try {
                    model = loader.loadModel(definition);
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
            modelCache.put(definition, model);
        }
        return model;
    }
}
