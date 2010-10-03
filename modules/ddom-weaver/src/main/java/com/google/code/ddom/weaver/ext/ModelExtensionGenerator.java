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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.ddom.commons.cl.ClassRef;
import com.google.code.ddom.weaver.reactor.ReactorException;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.reactor.WeavableClassInjector;
import com.google.code.ddom.weaver.realm.ClassInfo;
import com.google.code.ddom.weaver.realm.ClassRealm;

public class ModelExtensionGenerator {
    private static final Logger log = Logger.getLogger(ModelExtensionGenerator.class.getName());
    
    private final ClassRealm realm;
    private final List<ClassInfo> requiredImplementations;
    private final Map<ClassInfo,WeavableClassInfo> implementationMap = new HashMap<ClassInfo,WeavableClassInfo>();
    private final List<ClassInfo> modelExtensionInterfaces = new ArrayList<ClassInfo>();

    ModelExtensionGenerator(ClassRealm realm, List<ClassInfo> requiredImplementations) {
        this.realm = realm;
        this.requiredImplementations = requiredImplementations;
    }
    
    public void loadModelExtensionInterface(ClassRef classRef) {
        ClassInfo modelExtension = realm.getClassInfo(classRef);
        if (modelExtension.getInterfaces().length != 1) {
            throw new ReactorException("A model extension interface must have exactly one superinterface");
        }
        modelExtensionInterfaces.add(modelExtension);
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
        for (ClassInfo iface : modelExtensionInterfaces) {
            ClassInfo rootInterface = iface;
            do {
                // The number of super interface has already been validated by loadModelExtension
                rootInterface = rootInterface.getInterfaces()[0];
            } while (modelExtensionInterfaces.contains(rootInterface));
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
                for (ClassInfo iface : modelExtensionInfo.getExtensionInterfaces()) {
                    ModelExtensionClassInfo modelExtensionClassInfo = new ModelExtensionClassInfo(implementation, modelExtensionInfo.getRootInterface(), iface);
                    injector.loadWeavableClass(new ModelExtensionClass(modelExtensionClassInfo));
                    injector.loadWeavableClass(new ModelExtensionFactoryDelegateImplementation(implementationInfo, modelExtensionClassInfo));
                }
            }
        }
    }
}
