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

import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.realm.ClassInfo;

class ModelExtensionInfo {
    private final ClassInfo rootInterface;
    private final List<ClassInfo> extensionInterfaces = new ArrayList<ClassInfo>();
    // TODO: we should actually store a list of ImplementationInfos here
    private final List<WeavableClassInfo> implementations;
    
    ModelExtensionInfo(ClassInfo rootInterface, List<WeavableClassInfo> implementations) {
        this.rootInterface = rootInterface;
        this.implementations = implementations;
    }
    
    void addExtensionInterface(ClassInfo classInfo) {
        extensionInterfaces.add(classInfo);
    }
    
    ClassInfo getRootInterface() {
        return rootInterface;
    }

    List<WeavableClassInfo> getImplementations() {
        return implementations;
    }

    List<ClassInfo> getExtensionInterfaces() {
        return extensionInterfaces;
    }
}