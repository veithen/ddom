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
package com.googlecode.ddom.model.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinition;

public class StaticModelLoader implements ModelLoader {
    private final Map<ModelDefinition,StaticModel> modelMap = new HashMap<ModelDefinition,StaticModel>();
    private final ClassLoader classLoader;

    StaticModelLoader(Collection<StaticModel> models, ClassLoader classLoader) {
        for (StaticModel model : models) {
            modelMap.put(model.getModelDefinition(), model);
        }
        this.classLoader = classLoader;
    }

    public Model loadModel(ModelDefinition definition) throws ModelLoaderException {
        StaticModel staticModel = modelMap.get(definition);
        if (staticModel == null) {
            return null;
        } else {
            Class<?> nodeFactoryClass;
            try {
                nodeFactoryClass = classLoader.loadClass(staticModel.getNodeFactoryClassName());
            } catch (ClassNotFoundException ex) {
                throw new ModelLoaderException("Node factory class " + staticModel.getNodeFactoryClassName() + " not found");
            }
            try {
                return new Model((NodeFactory)nodeFactoryClass.getField("INSTANCE").get(null));
            } catch (NoSuchFieldException ex) {
                throw new ModelLoaderException(ex);
            } catch (IllegalAccessException ex) {
                throw new ModelLoaderException(ex);
            }
        }
    }
}
