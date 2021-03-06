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
package com.googlecode.ddom.weaver;

import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.ddom.backend.Backend;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.Frontend;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinition;
import com.googlecode.ddom.model.spi.ModelLoader;
import com.googlecode.ddom.model.spi.ModelLoaderException;
import com.googlecode.ddom.weaver.output.DynamicClassLoader;

/**
 * {@link ModelLoader} implementation that creates models using load time weaving.
 * 
 * @author Andreas Veithen
 */
public class DynamicModelLoader implements ModelLoader {
    private final ClassLoader parentClassLoader;
    private final Map<String,Backend> backendMap;
    private final Map<String,Frontend> frontendMap;
    
    DynamicModelLoader(ClassLoader classLoader, Map<String,Backend> backends, Map<String,Frontend> frontends) {
        this.parentClassLoader = classLoader;
        this.backendMap = backends;
        this.frontendMap = frontends;
    }
    
    public Model loadModel(ModelDefinition definition) throws ModelLoaderException {
        Backend backend = backendMap.get(definition.getBackend());
        if (backend == null) {
            return null;
        }
        
        String[] frontendIds = definition.getFrontends();
        Map<String,Frontend> frontends = new LinkedHashMap<String,Frontend>();
//        ModelExtension modelExtension = null;
        for (String frontendId : frontendIds) {
            Frontend frontend = frontendMap.get(frontendId);
            if (frontend == null) {
                return null;
            }
            frontends.put(frontendId, frontend);
//            if (modelExtension == null) {
//                // TODO: we need to have a way to combine model extensions!
//                modelExtension = frontend.getModelExtension();
//            }
        }
//        if (modelExtension == null) {
//            modelExtension = ModelExtension.NULL;
//        }
        NodeFactory nodeFactory;
        DynamicClassLoader classLoader = new DynamicClassLoader(parentClassLoader);
        try {
            ModelWeaver weaver = new ModelWeaver();
            weaver.setClassLoader(parentClassLoader);
            weaver.setProcessor(classLoader);
            weaver.setBackend(backend);
            // Aspects must be loaded into the child class loader. Otherwise the code in these aspects
            // will not see the woven backend classes. 
//            for (Frontend frontend : frontends) {
//                for (String className : frontend.getAspectClasses()) {
//                    classLoader.processClassDefinition(className, ClassLoaderUtils.getClassDefinition(parentClassLoader, className));
//                }
//            }
            weaver.setFrontends(frontends);
            weaver.weave();
            Class<? extends NodeFactory> nodeFactoryClass = classLoader.loadClass(backend.getNodeFactoryClassName()).asSubclass(NodeFactory.class);
            nodeFactory = (NodeFactory)nodeFactoryClass.getField("INSTANCE").get(null);
        } catch (Exception ex) {
            throw new ModelLoaderException("Failed to weave model", ex);
        }
        return new Model(nodeFactory);
    }
}
