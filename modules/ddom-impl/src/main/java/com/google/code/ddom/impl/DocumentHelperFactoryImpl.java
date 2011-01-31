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
package com.google.code.ddom.impl;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.stream.StreamFactory;

public class DocumentHelperFactoryImpl extends DocumentHelperFactory {
    @Override
    public DocumentHelper getDefaultInstance() {
        // TODO
        return null;
    }
    
    @Override
    public DocumentHelper newInstance(ClassLoader classLoader) {
        return new DocumentHelperImpl(ModelRegistry.getInstance(classLoader), StreamFactory.getInstance(classLoader));
    }
    
    @Override
    public DocumentHelper newInstance() {
        return new DocumentHelperImpl(ModelRegistry.getInstance(), StreamFactory.getInstance());
    }
}
