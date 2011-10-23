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
package com.googlecode.ddom.axiom;

import java.util.Map;

import org.apache.axiom.locator.loader.OMMetaFactoryLoader;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMMetaFactory;

import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;

public class OMMetaFactoryLoaderImpl implements OMMetaFactoryLoader {
    public OMMetaFactory load(Map properties) {
        try {
            ModelDefinitionBuilder mdb = new ModelDefinitionBuilder();
            mdb.addFrontend("axiom-soap");
            mdb.addFrontend("dom");
            ModelRegistry modelRegistry = ModelRegistry.getInstance(OMMetaFactoryLoaderImpl.class.getClassLoader());
            Model model = modelRegistry.getModel(mdb.buildModelDefinition());
            return (OMMetaFactory)model.getNodeFactory();
        } catch (ModelLoaderException ex) {
            throw new OMException(ex);
        }
    }
}
