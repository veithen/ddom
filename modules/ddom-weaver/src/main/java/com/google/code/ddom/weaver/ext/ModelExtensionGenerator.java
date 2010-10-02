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
    private List<ModelExtension> modelExtensions;

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
    public List<WeavableClassInfo> getImplementations(ClassInfo iface) {
        List<WeavableClassInfo> implementations = new ArrayList<WeavableClassInfo>();
        for (WeavableClassInfo candidate : implementationMap.values()) {
            if (iface.isAssignableFrom(candidate)) {
                implementations.add(candidate);
            }
        }
        return implementations;
    }

    public List<WeavableClassInfo> getImplementations() {
        return new ArrayList<WeavableClassInfo>(implementationMap.values());
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
            modelExtension.resolve(realm);
        }
    }
    
    void generateExtensions(WeavableClassInjector injector) {
        for (WeavableClassInfo implementation : getImplementations()) {
            ModelExtensionFactoryInfo info = new ModelExtensionFactoryInfo(implementation);
            injector.loadWeavableClass(new ModelExtensionFactoryDelegateInterface(info));
            injector.loadWeavableClass(new ModelExtensionFactoryImplementation(info));
        }
        for (ModelExtension modelExtension : modelExtensions) {
            for (WeavableClassInfo implementation : modelExtension.getImplementations()) {
                ModelExtensionFactoryInfo modelExtensionFactoryInfo = new ModelExtensionFactoryInfo(implementation);
                injector.loadWeavableClass(new ModelExtensionFactoryDelegateImplementation(modelExtensionFactoryInfo, new ModelExtensionClassInfo(implementation, modelExtension.getRootInterface(), null)));
                for (ClassInfo iface : modelExtension.getExtensionInterfaces()) {
                    ModelExtensionClassInfo modelExtensionClassInfo = new ModelExtensionClassInfo(implementation, modelExtension.getRootInterface(), iface);
                    injector.loadWeavableClass(new ModelExtensionClass(modelExtensionClassInfo));
                    injector.loadWeavableClass(new ModelExtensionFactoryDelegateImplementation(modelExtensionFactoryInfo, modelExtensionClassInfo));
                }
            }
        }
    }
}
