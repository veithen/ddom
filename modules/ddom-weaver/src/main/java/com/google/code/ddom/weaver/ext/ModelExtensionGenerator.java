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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.ddom.commons.cl.ClassRef;
import com.google.code.ddom.weaver.implementation.ImplementationMap;
import com.google.code.ddom.weaver.reactor.Reactor;
import com.google.code.ddom.weaver.reactor.ReactorException;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.reactor.WeavableClassInjector;
import com.google.code.ddom.weaver.realm.ClassInfo;

public class ModelExtensionGenerator {
    private final Reactor reactor;
    private final List<ClassInfo> modelExtensionInterfaces = new ArrayList<ClassInfo>();
    private List<ModelExtension> modelExtensions;

    ModelExtensionGenerator(Reactor reactor) {
        this.reactor = reactor;
    }
    
    public void loadModelExtensionInterface(ClassRef classRef) {
        ClassInfo modelExtension = reactor.getClassInfo(classRef);
        if (modelExtension.getInterfaces().length != 1) {
            throw new ReactorException("A model extension interface must have exactly one superinterface");
        }
        modelExtensionInterfaces.add(modelExtension);
    }
    
    private boolean isModelExtension(ClassInfo classInfo) {
        // TODO: do we really need this, or is equals (identity) enough???
        String className = classInfo.getName();
        for (ClassInfo ci : modelExtensionInterfaces) {
            if (className.equals(ci.getName())) {
                return true;
            }
        }
        return false;
    }
    
    void resolve() {
        // We need to sort the model extensions so that defineClass doesn't complain (in case
        // a DynamicClassLoader is used).
//        for (ClassInfo modelExtension : TopologicalSort.sort(modelExtensionInterfaces, inheritanceRelation)) {
        Map<ClassInfo,ModelExtension> modelExtensionMap = new HashMap<ClassInfo,ModelExtension>();
        for (ClassInfo iface : modelExtensionInterfaces) {
            ClassInfo root = iface;
            do {
                // The number of super interface has already been validated by loadModelExtension
                root = root.getInterfaces()[0];
            } while (isModelExtension(root));
            ModelExtension modelExtension = modelExtensionMap.get(root);
            if (modelExtension == null) {
                modelExtension = new ModelExtension(root);
                modelExtensionMap.put(root, modelExtension);
            }
            modelExtension.addExtensionInterface(iface);
        }
        modelExtensions = new ArrayList<ModelExtension>(modelExtensionMap.values());
        for (ModelExtension modelExtension : modelExtensions) {
            modelExtension.resolve(reactor);
        }
    }
    
    void generateExtensions(WeavableClassInjector injector) {
        for (WeavableClassInfo implementation : reactor.get(ImplementationMap.class).getImplementations()) {
            injector.loadWeavableClass(new ModelExtensionFactoryDelegateInterface(implementation));
        }
        for (ModelExtension modelExtension : modelExtensions) {
            for (WeavableClassInfo implementation : modelExtension.getImplementations()) {
                injector.loadWeavableClass(new ModelExtensionFactoryDelegateImplementation(new ModelExtensionClassInfo(implementation, modelExtension.getRootInterface(), null)));
                for (ClassInfo iface : modelExtension.getExtensionInterfaces()) {
                    ModelExtensionClassInfo info = new ModelExtensionClassInfo(implementation, modelExtension.getRootInterface(), iface);
                    injector.loadWeavableClass(new ModelExtensionClass(info));
                    injector.loadWeavableClass(new ModelExtensionFactoryDelegateImplementation(info));
                }
            }
        }
    }
}
