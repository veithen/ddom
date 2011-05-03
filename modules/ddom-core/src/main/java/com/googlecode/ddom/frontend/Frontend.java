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
package com.googlecode.ddom.frontend;

import java.util.Map;

import com.google.code.ddom.commons.cl.ClassCollection;
import com.googlecode.ddom.core.ext.ModelExtension;

// TODO: this API is a bit simplistic; we need at least to support the following features:
//        - a frontend configuration (so that aspects can be selected dynamically)
//        - we need to be able to mix frontends and to support dependencies (e.g. SAAJ -> DOM)
public interface Frontend {
    // TODO: we should hide the context/configuration data (current parameters) behind some class to allow for extensibility in the future
    ClassCollection getMixins(Map<String,Frontend> frontends);
    
    ModelExtension getModelExtension();
}
