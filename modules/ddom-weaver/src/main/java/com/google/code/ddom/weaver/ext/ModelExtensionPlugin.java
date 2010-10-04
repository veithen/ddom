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
import com.google.code.ddom.core.ext.Abstract;
import com.google.code.ddom.weaver.reactor.Extensions;
import com.google.code.ddom.weaver.reactor.NonWeavableClassInfo;
import com.google.code.ddom.weaver.reactor.ReactorPlugin;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.google.code.ddom.weaver.reactor.WeavableClassInjector;
import com.google.code.ddom.weaver.realm.ClassInfo;
import com.google.code.ddom.weaver.realm.ClassRealm;

public class ModelExtensionPlugin extends ReactorPlugin {
    private final List<ClassRef> requiredImplementations = new ArrayList<ClassRef>();
    private final List<ClassRef> modelExtensionInterfaces = new ArrayList<ClassRef>();

    public void addRequiredImplementation(ClassRef iface) {
        requiredImplementations.add(iface);
    }
    
    public void addModelExtensionInterface(ClassRef iface) {
        modelExtensionInterfaces.add(iface);
    }
    
    @Override
    public void init(ClassRealm realm, Extensions extensions) {
        List<ClassInfo> requiredImplementations = new ArrayList<ClassInfo>(this.requiredImplementations.size());
        for (ClassRef classRef : this.requiredImplementations) {
            requiredImplementations.add(realm.getClassInfo(classRef));
        }
        List<ModelExtensionInterfaceInfo> modelExtensionInterfaces = new ArrayList<ModelExtensionInterfaceInfo>(this.modelExtensionInterfaces.size());
        for (ClassRef classRef : this.modelExtensionInterfaces) {
            modelExtensionInterfaces.add(realm.getClassInfo(classRef).get(ModelExtensionInterfaceInfo.class));
        }
        extensions.set(new ModelExtensionGenerator(requiredImplementations, modelExtensionInterfaces));
    }

    @Override
    public WeavableClassInfoBuilderCollaborator newWeavableClassInfoBuilderCollaborator() {
        return new ImplementationBuilder();
    }

    @Override
    public void processNonWeavableClassInfo(NonWeavableClassInfo classInfo, Class<?> clazz, Extensions extensions) {
        boolean isExtensionInterface = false;
        for (ClassRef classRef : modelExtensionInterfaces) {
            if (classRef.getClassName().equals(classInfo.getName())) {
                isExtensionInterface = true;
                break;
            }
        }
        if (isExtensionInterface) {
            extensions.set(new ModelExtensionInterfaceInfo(classInfo, clazz.getAnnotation(Abstract.class) != null));
        } else {
            extensions.set(ModelExtensionInterfaceInfo.class, null);
        }
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
