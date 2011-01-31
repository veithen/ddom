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

import com.google.code.ddom.model.ModelDefinition;
import com.googlecode.ddom.model.Model;

/**
 * Produces a {@link Model} from a given {@link ModelDefinition}.
 * 
 * @author Andreas Veithen
 */
public interface ModelLoader {
    /**
     * Load the model corresponding to a given definition.
     * 
     * @param definition
     * @return the model, or <code>null</code> if the model could not be loaded
     */
    // TODO: document that the loader is not responsible for caching the returned instance
    Model loadModel(ModelDefinition definition) throws ModelLoaderException;
}