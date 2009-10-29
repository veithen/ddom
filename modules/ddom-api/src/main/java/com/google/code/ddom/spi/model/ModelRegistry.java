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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.ProviderFinderException;
import com.google.code.ddom.weaver.ModelWeaver;

public final class ModelRegistry {
    // TODO: WeakHashMap will probably not work as expected in this case, because the value may hold a strong reference to the key (see "Implementation note" of WeakHashMap)
    private static final Map<ClassLoader,ModelRegistry> registries = Collections.synchronizedMap(new WeakHashMap<ClassLoader,ModelRegistry>());
    
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
                    // TODO: clarify this
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
