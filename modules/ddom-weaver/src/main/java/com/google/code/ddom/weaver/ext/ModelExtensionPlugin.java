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
import com.google.code.ddom.weaver.reactor.Extensions;
import com.google.code.ddom.weaver.reactor.ReactorPlugin;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.google.code.ddom.weaver.reactor.WeavableClassInjector;
import com.google.code.ddom.weaver.realm.ClassInfo;
import com.google.code.ddom.weaver.realm.ClassRealm;

public class ModelExtensionPlugin extends ReactorPlugin {
    private final List<ClassRef> requiredImplementations = new ArrayList<ClassRef>();

    public void addRequiredImplementation(ClassRef iface) throws ClassNotFoundException {
        requiredImplementations.add(iface);
    }
    
    @Override
    public void init(ClassRealm realm, Extensions extensions) {
        List<ClassInfo> requiredImplementations = new ArrayList<ClassInfo>(this.requiredImplementations.size());
        for (ClassRef classRef : this.requiredImplementations) {
            requiredImplementations.add(realm.getClassInfo(classRef));
        }
        extensions.set(new ModelExtensionGenerator(realm, requiredImplementations));
    }

    @Override
    public WeavableClassInfoBuilderCollaborator newWeavableClassInfoBuilderCollaborator() {
        return new ImplementationBuilder();
    }

    @Override
    public void resolve(ClassRealm realm) {
        ModelExtensionGenerator generator = realm.get(ModelExtensionGenerator.class);
        generator.validate();
        generator.resolve();
    }

    @Override
    public void generateWeavableClasses(ClassRealm realm, WeavableClassInjector weavableClassInjector) {
        realm.get(ModelExtensionGenerator.class).generateExtensions(weavableClassInjector);
    }
}