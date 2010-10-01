/*
 * Copyright 2009-2010 Andreas Veithen
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

import com.google.code.ddom.core.DocumentFactory;
import com.google.code.ddom.frontend.APIObjectFactory;

/**
 * Represents a (woven) model corresponding to a given
 * {@link com.google.code.ddom.model.ModelDefinition}.
 * 
 * @author Andreas Veithen
 */
public class Model {
    private final DocumentFactory documentFactory;
    private final APIObjectFactory apiObjectFactory;
    
    public Model(DocumentFactory documentFactory, APIObjectFactory apiObjectFactory) {
        this.documentFactory = documentFactory;
        this.apiObjectFactory = apiObjectFactory;
    }

    public DocumentFactory getDocumentFactory() {
        return documentFactory;
    }

    public APIObjectFactory getAPIObjectFactory() {
        return apiObjectFactory;
    }
}
