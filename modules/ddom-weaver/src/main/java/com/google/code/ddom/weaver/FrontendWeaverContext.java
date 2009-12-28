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

import java.util.Collections;
import java.util.List;

import org.aspectj.weaver.loadtime.DefaultWeavingContext;
import org.aspectj.weaver.loadtime.definition.Definition;
import org.aspectj.weaver.tools.WeavingAdaptor;

import com.google.code.ddom.spi.model.Frontend;

public class FrontendWeaverContext extends DefaultWeavingContext {
    public FrontendWeaverContext(ClassLoader loader) {
        super(loader);
    }

    @Override
    public List<Definition> getDefinitions(ClassLoader loader, WeavingAdaptor adaptor) {
        Frontend frontend = ((FrontendWeaver)loader).getFrontend();
        Definition definition = new Definition();
        definition.getAspectClassNames().addAll(frontend.getAspectClasses());
        return Collections.singletonList(definition);
    }
}
