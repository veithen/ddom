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

import org.aspectj.weaver.loadtime.Aj;
import org.aspectj.weaver.loadtime.ClassPreProcessor;

import com.google.code.ddom.commons.cl.TransformingClassLoader;
import com.google.code.ddom.spi.model.Model;

public class ModelWeaver extends TransformingClassLoader {
    private final Model model;
    private final ClassPreProcessor preProcessor;
    
    public ModelWeaver(ClassLoader parent, Model model) {
        super(parent);
        this.model = model;
        preProcessor = new Aj(new ModelWeaverContext(this));
    }
    
    public Model getModel() {
        return model;
    }

    @Override
    protected boolean needsTransformation(String className) {
        return className.startsWith("com.google.code.ddom.core.model.") || className.startsWith("com.google.code.ddom.dom.impl."); // TODO: hardcoded aspects package!!!
    }

    @Override
    protected byte[] transformClass(String className, byte[] classDef) {
        return preProcessor.preProcess(className, classDef, this);
    }
}
