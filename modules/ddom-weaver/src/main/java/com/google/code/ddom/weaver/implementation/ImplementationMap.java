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
package com.google.code.ddom.weaver.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.ddom.weaver.reactor.ReactorException;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.realm.ClassInfo;

public class ImplementationMap {
    private static final Logger log = Logger.getLogger(ImplementationMap.class.getName());
    
    private final List<ClassInfo> requiredImplementations;
    private final Map<ClassInfo,WeavableClassInfo> implementationMap = new HashMap<ClassInfo,WeavableClassInfo>();

    ImplementationMap(List<ClassInfo> requiredImplementations) {
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
}
