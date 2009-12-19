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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.code.ddom.commons.cl.ClassLoaderLocal;
import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.ProviderFinderException;
import com.google.code.ddom.weaver.ModelWeaver;

public final class ModelRegistry {
    private static final ClassLoaderLocal<ModelRegistry> registries = new ClassLoaderLocal<ModelRegistry>();
    
    private final Map<String,NodeFactory> nodeFactories;
    
    private ModelRegistry(Map<String,NodeFactory> nodeFactories) {
        this.nodeFactories = nodeFactories;
    }
    
    public static ModelRegistry getInstance(ClassLoader classLoader) throws ProviderFinderException {
        ModelRegistry registry = registries.get(classLoader);
        if (registry == null) {
            Map<String,NodeFactory> factories = new LinkedHashMap<String,NodeFactory>();
            // TODO: this should be done lazily
            for (Map.Entry<String,Model> entry : ProviderFinder.find(classLoader, Model.class).entrySet()) {
                try {
                    ModelWeaver weaver = new ModelWeaver(classLoader, entry.getValue());
                    // TODO: this is necessary to work around some bug in AspectJ, probably https://bugs.eclipse.org/bugs/show_bug.cgi?id=286473
                    weaver.loadClass("com.google.code.ddom.core.model.NodeImpl");
                    weaver.loadClass("com.google.code.ddom.core.model.ParentNodeImpl");
                    factories.put(entry.getKey(), (NodeFactory)weaver.loadClass("com.google.code.ddom.core.model.NodeFactoryImpl").newInstance());
                } catch (Exception ex) { // TODO: do this properly
                    throw new ProviderFinderException(ex);
                }
            }
            registry = new ModelRegistry(factories);
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
    
    public NodeFactory getNodeFactory(String model) {
        return nodeFactories.get(model);
    }
}
