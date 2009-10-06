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
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class ModelRegistry {
    private static final Map<ClassLoader,ModelRegistry> registries = Collections.synchronizedMap(new WeakHashMap<ClassLoader,ModelRegistry>());
    
    private final Map<String,NodeFactory> nodeFactories = new HashMap<String,NodeFactory>();
    
    private ModelRegistry() {}
    
    public static ModelRegistry getInstance(ClassLoader classLoader) {
        ModelRegistry registry = registries.get(classLoader);
        if (registry == null) {
            registry = new ModelRegistry();

            // TODO: replace this by a service discovery algorithm
            try {
                registry.nodeFactories.put("dom", (NodeFactory)classLoader.loadClass("com.google.code.ddom.dom.impl.DOMNodeFactory").newInstance());
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            registries.put(classLoader, registry);
        }
        return registry;
    }
    
    public static ModelRegistry getInstance() {
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
