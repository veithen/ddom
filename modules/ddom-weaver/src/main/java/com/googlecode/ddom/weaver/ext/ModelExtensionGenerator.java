/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.weaver.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.googlecode.ddom.weaver.reactor.ReactorException;
import com.googlecode.ddom.weaver.reactor.WeavableClassInfo;
import com.googlecode.ddom.weaver.reactor.WeavableClassInjector;
import com.googlecode.ddom.weaver.realm.ClassInfo;

class ModelExtensionGenerator {
    private static final Logger log = Logger.getLogger(ModelExtensionGenerator.class.getName());
    
    private final List<ClassInfo> requiredImplementations;
    private final List<ModelExtensionInterfaceInfo> modelExtensionInterfaces = new ArrayList<ModelExtensionInterfaceInfo>();
    private final Map<ClassInfo,WeavableClassInfo> implementationMap = new HashMap<ClassInfo,WeavableClassInfo>();

    ModelExtensionGenerator(List<ClassInfo> requiredImplementations) {
        this.requiredImplementations = requiredImplementations;
    }
    
    void addImplementation(WeavableClassInfo weavableClass) {
        boolean found = false;
        for (ClassInfo iface : requiredImplementations) {
            if (iface.isAssignableFrom(weavableClass)) {
                ClassInfo impl = implementationMap.get(iface);
                if (impl != null) {
                    throw new ReactorException("Duplicate implementation: an implementation of " + iface + " has already been found, namely " + impl);
                } else {
                    implementationMap.put(iface, weavableClass);
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            throw new ReactorException("The class " + weavableClass + " was annotated with @Implementation, but this is not expected");
        }
    }

    void addModelExtensionInterface(ModelExtensionInterfaceInfo modelExtensionInterface) {
        modelExtensionInterfaces.add(modelExtensionInterface);
    }

    void validate() {
        if (implementationMap.size() != requiredImplementations.size()) {
            Set<ClassInfo> missingImplementations = new HashSet<ClassInfo>(requiredImplementations);
            missingImplementations.removeAll(implementationMap.keySet());
            throw new ReactorException("The implementations for the following interfaces have not been found: " + missingImplementations);
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("Implementation map: " + implementationMap);
        }
    }

    /**
     * Get all weavable classes that are annotated with
     * {@link com.google.code.ddom.backend.Implementation} and implement (are assignable to) a given
     * interface.
     * 
     * @param iface
     * @return
     */
    List<WeavableClassInfo> getImplementations(ClassInfo iface) {
        List<WeavableClassInfo> implementations = new ArrayList<WeavableClassInfo>();
        for (WeavableClassInfo candidate : implementationMap.values()) {
            if (iface.isAssignableFrom(candidate)) {
                implementations.add(candidate);
            }
        }
        return implementations;
    }

    void resolve() {
        // We need to sort the model extensions so that defineClass doesn't complain (in case
        // a DynamicClassLoader is used).
//        for (ClassInfo modelExtension : TopologicalSort.sort(modelExtensionInterfaces, inheritanceRelation)) {
        Map<ClassInfo,ModelExtensionInfo> modelExtensionMap = new HashMap<ClassInfo,ModelExtensionInfo>();
        for (ModelExtensionInterfaceInfo iface : modelExtensionInterfaces) {
            ClassInfo rootInterface = iface.getRoot();
            ModelExtensionInfo modelExtensionInfo = modelExtensionMap.get(rootInterface);
            if (modelExtensionInfo == null) {
                List<WeavableClassInfo> implementations = getImplementations(rootInterface);
                if (implementations.isEmpty()) {
                    throw new ReactorException("No implementations found for root interface " + rootInterface);
                }
                modelExtensionInfo = new ModelExtensionInfo(rootInterface, implementations);
                modelExtensionMap.put(rootInterface, modelExtensionInfo);
                for (WeavableClassInfo implementation : implementations) {
                    implementation.get(ImplementationInfo.class).addModelExtension(modelExtensionInfo);
                }
            }
            modelExtensionInfo.addExtensionInterface(iface);
        }
        if (log.isLoggable(Level.FINE)) {
            for (ModelExtensionInfo modelExtensionInfo : modelExtensionMap.values()) {
                log.fine("Resolved model extension:\n  Root interface: " + modelExtensionInfo.getRootInterface()
                        + "\n  Extension interfaces: " + modelExtensionInfo.getExtensionInterfaces()
                        + "\n  Implementations: " + modelExtensionInfo.getImplementations());
            }
        }
    }
    
    void generateExtensions(WeavableClassInjector injector) {
        for (WeavableClassInfo implementation : implementationMap.values()) {
            ImplementationInfo implementationInfo = implementation.get(ImplementationInfo.class);
            injector.loadWeavableClass(new ModelExtensionFactoryDelegateInterface(implementationInfo));
            injector.loadWeavableClass(new ModelExtensionFactoryImplementation(implementationInfo));
            for (ModelExtensionInfo modelExtensionInfo : implementationInfo.getModelExtensions()) {
                for (ModelExtensionInterfaceInfo iface : modelExtensionInfo.getExtensionInterfaces()) {
                    ModelExtensionClassInfo modelExtensionClassInfo = new ModelExtensionClassInfo(implementation, modelExtensionInfo.getRootInterface(), iface);
                    injector.loadWeavableClass(new ModelExtensionClass(modelExtensionClassInfo));
                    if (!modelExtensionClassInfo.isAbstract()) {
                        injector.loadWeavableClass(new ModelExtensionFactoryDelegateImplementation(implementationInfo, modelExtensionClassInfo));
                    }
                }
            }
        }
    }
}
