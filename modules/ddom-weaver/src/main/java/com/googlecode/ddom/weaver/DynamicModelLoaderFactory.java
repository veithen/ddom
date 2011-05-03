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
package com.googlecode.ddom.weaver;

import com.googlecode.ddom.backend.Backend;
import com.googlecode.ddom.frontend.Frontend;
import com.googlecode.ddom.model.spi.ModelLoader;
import com.googlecode.ddom.model.spi.ModelLoaderFactory;
import com.googlecode.ddom.spi.Provider;
import com.googlecode.ddom.spi.Finder;

@Provider(name="dynamic")
public class DynamicModelLoaderFactory implements ModelLoaderFactory {
    public ModelLoader createModelLoader(ClassLoader classLoader) {
        return new DynamicModelLoader(classLoader, Finder.findNamedProviders(classLoader, Backend.class), Finder.findNamedProviders(classLoader, Frontend.class));
    }
}
