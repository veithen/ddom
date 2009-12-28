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

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.ProviderFinder;
import com.google.code.ddom.spi.model.Backend;
import com.google.code.ddom.spi.model.Frontend;
import com.google.code.ddom.spi.model.ModelLoader;
import com.google.code.ddom.spi.model.ModelLoaderFactory;

@Provider(name="dynamic")
public class DynamicModelLoaderFactory implements ModelLoaderFactory {
    public ModelLoader createModelLoader(ClassLoader classLoader) {
        return new DynamicModelLoader(classLoader, ProviderFinder.find(classLoader, Backend.class), ProviderFinder.find(classLoader, Frontend.class));
    }
}