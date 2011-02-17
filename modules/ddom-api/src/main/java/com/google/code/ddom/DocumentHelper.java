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
package com.google.code.ddom;

import com.googlecode.ddom.model.ModelDefinition;
import com.googlecode.ddom.stream.Options;

public interface DocumentHelper {
    Object newDocument(ModelDefinition model);
    
    Object newDocument(String frontend);
    
    Object parse(ModelDefinition model, Object source, Options options, boolean preserve);

    Object parse(String frontend, Object source, Options options, boolean preserve);
    
    Object parse(String frontend, Object source, boolean preserve);
    
    Object parse(String frontend, Object source, Options options);
    
    Object parse(ModelDefinition modelDefinition, Object source, Options options);
    
    Object parse(String frontend, Object source);
    
    void buildDocument(Object document);
    
    void disposeDocument(Object document);
    
    <T> T getAPIObject(ModelDefinition modelDefinition, Class<T> clazz);
}
