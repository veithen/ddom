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
package com.google.code.ddom.weaver.ext;

import java.util.ArrayList;
import java.util.List;

import com.google.code.ddom.commons.cl.ClassRef;
import com.google.code.ddom.weaver.reactor.Reactor;
import com.google.code.ddom.weaver.reactor.ReactorPlugin;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.google.code.ddom.weaver.reactor.WeavableClassInjector;
import com.google.code.ddom.weaver.realm.ClassInfo;

public class ModelExtensionPlugin extends ReactorPlugin {
    private final List<ClassRef> requiredImplementations = new ArrayList<ClassRef>();

    public void addRequiredImplementation(ClassRef iface) throws ClassNotFoundException {
        requiredImplementations.add(iface);
    }
    
    @Override
    public void init(Reactor reactor) {
        List<ClassInfo> requiredImplementations = new ArrayList<ClassInfo>(this.requiredImplementations.size());
        for (ClassRef classRef : this.requiredImplementations) {
            requiredImplementations.add(reactor.getClassInfo(classRef));
        }
        reactor.set(new ModelExtensionGenerator(reactor, requiredImplementations));
    }

    @Override
    public WeavableClassInfoBuilderCollaborator newWeavableClassInfoBuilderCollaborator(Reactor reactor) {
        return new ImplementationAnnotationExtractor(reactor, reactor.get(ModelExtensionGenerator.class));
    }

    @Override
    public void resolve(Reactor reactor) {
        ModelExtensionGenerator generator = reactor.get(ModelExtensionGenerator.class);
        generator.validate();
        generator.resolve();
    }

    @Override
    public void generateWeavableClasses(Reactor reactor, WeavableClassInjector weavableClassInjector) {
        reactor.get(ModelExtensionGenerator.class).generateExtensions(weavableClassInjector);
    }
}
