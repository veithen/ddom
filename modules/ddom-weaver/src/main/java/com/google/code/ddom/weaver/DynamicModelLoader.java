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
package com.google.code.ddom.weaver;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.code.ddom.backend.DocumentFactory;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.spi.model.Backend;
import com.google.code.ddom.spi.model.Frontend;
import com.google.code.ddom.spi.model.ModelLoader;
import com.google.code.ddom.spi.model.ModelLoaderException;

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
    
    public DocumentFactory loadModel(ModelDefinition definition) throws ModelLoaderException {
        Backend backend = backendMap.get(definition.getBackend());
        if (backend == null) {
            return null;
        }
        
        String[] frontendIds = definition.getFrontends();
        Map<String,Frontend> frontends = new LinkedHashMap<String,Frontend>();
        for (String frontendId : frontendIds) {
            Frontend frontend = frontendMap.get(frontendId);
            if (frontend == null) {
                return null;
            }
            frontends.put(frontendId, frontend);
        }
        DynamicClassLoader classLoader = new DynamicClassLoader(parentClassLoader);
        try {
            com.google.code.ddom.weaver.asm.ModelWeaver weaver = new com.google.code.ddom.weaver.asm.ModelWeaver(parentClassLoader, classLoader, backend);
            // Aspects must be loaded into the child class loader. Otherwise the code in these aspects
            // will not see the woven backend classes. 
//            for (Frontend frontend : frontends) {
//                for (String className : frontend.getAspectClasses()) {
//                    classLoader.processClassDefinition(className, ClassLoaderUtils.getClassDefinition(parentClassLoader, className));
//                }
//            }
            weaver.weave(frontends);
            return (DocumentFactory)classLoader.loadClass(backend.getDocumentFactoryClassName()).newInstance();
        } catch (Exception ex) {
            throw new ModelLoaderException(ex);
        }
    }
}
