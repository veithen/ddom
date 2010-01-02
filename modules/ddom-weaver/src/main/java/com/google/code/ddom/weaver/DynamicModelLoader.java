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

import java.util.Arrays;
import java.util.Map;

import org.aspectj.weaver.loadtime.Aj;
import org.aspectj.weaver.loadtime.ClassPreProcessor;

import com.google.code.ddom.DocumentFactory;
import com.google.code.ddom.commons.cl.ClassLoaderUtils;
import com.google.code.ddom.commons.cl.ClassUtils;
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
        String documentFactoryClassName = backend.getDocumentFactoryClassName();
        
        String[] frontendIds = definition.getFrontends();
        Frontend[] frontends = new Frontend[frontendIds.length];
        for (int i=0; i<frontendIds.length; i++) {
            Frontend frontend = frontendMap.get(frontendIds[i]);
            if (frontend == null) {
                return null;
            }
            frontends[i] = frontend;
        }
        DynamicClassLoader classLoader = new DynamicClassLoader(parentClassLoader);
        ClassPreProcessor preProcessor = new Aj(new LoadTimeWeaverContext(classLoader, frontends));
        try {
            // Aspects must be loaded into the child class loader. Otherwise the code in these aspects
            // will not see the woven backend classes. 
            for (Frontend frontend : frontends) {
                for (String className : frontend.getAspectClasses()) {
                    classLoader.processClassDefinition(className, ClassLoaderUtils.getClassDefinition(parentClassLoader, className));
                }
            }
            Class<?>[] classes = ClassLoaderUtils.getClassesInPackage(parentClassLoader, documentFactoryClassName);
            for (Class<?> cls : ClassUtils.sortHierarchically(Arrays.asList(classes))) {
                String className = cls.getName();
                // TODO: Aj#preProcess may have the side effect of calling ClassLoader#defineClass (through reflection); thus, this approach can't be generalized to static model loading (because we miss some generated classes, namely the XXX$AjcClosureT classes)
                classLoader.processClassDefinition(className, preProcessor.preProcess(className, ClassLoaderUtils.getClassDefinition(parentClassLoader, className), classLoader));
            }
            return (DocumentFactory)classLoader.loadClass(documentFactoryClassName).newInstance();
        } catch (Exception ex) {
            throw new ModelLoaderException(ex);
        }
    }
}
