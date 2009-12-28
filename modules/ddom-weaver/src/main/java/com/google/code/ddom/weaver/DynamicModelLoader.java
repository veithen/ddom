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
    private final ClassLoader classLoader;
    private final Map<String,Backend> backendMap;
    private final Map<String,Frontend> frontendMap;
    
    DynamicModelLoader(ClassLoader classLoader, Map<String,Backend> backends, Map<String,Frontend> frontends) {
        this.classLoader = classLoader;
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
        
        FrontendWeaver weaver = new FrontendWeaver(classLoader, frontends[0]); // TODO: support multiple frontends
        
        try {
            // The following code serves two purposes:
            //  * All classes are woven at this point, so that any errors will be reported here. This
            //    avoids obscure class loader error messages if there is any problem with the aspects.
            //  * The classes are loaded in superclass-first order to work around a bug in AspectJ
            //    (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=286473).
            // TODO: follow-up the bug with the AspectJ project
            Class<?>[] classes = ClassLoaderUtils.getClassesInPackage(classLoader, documentFactoryClassName);
            for (Class<?> cls : ClassUtils.sortHierarchically(Arrays.asList(classes))) {
                weaver.loadClass(cls.getName());
            }
            
            return (DocumentFactory)weaver.loadClass(documentFactoryClassName).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new ModelLoaderException(ex);
        } catch (IllegalAccessException ex) {
            throw new ModelLoaderException(ex);
        } catch (InstantiationException ex) {
            throw new ModelLoaderException(ex);
        }
    }
}
