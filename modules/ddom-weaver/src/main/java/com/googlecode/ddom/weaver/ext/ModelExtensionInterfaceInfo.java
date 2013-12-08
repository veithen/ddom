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

import com.googlecode.ddom.weaver.reactor.NonWeavableClassInfo;
import com.googlecode.ddom.weaver.realm.ClassInfo;

class ModelExtensionInterfaceInfo {
    private final NonWeavableClassInfo classInfo;
    private final boolean isAbstract;
    private final ClassInfo parent;
    
    ModelExtensionInterfaceInfo(NonWeavableClassInfo classInfo, boolean isAbstract, ClassInfo parent) {
        this.classInfo = classInfo;
        this.isAbstract = isAbstract;
        this.parent = parent;
    }

    NonWeavableClassInfo getClassInfo() {
        return classInfo;
    }

    boolean isAbstract() {
        return isAbstract;
    }

    ClassInfo getParent() {
        return parent;
    }
    
    ClassInfo getRoot() {
        ModelExtensionInterfaceInfo parentInfo = parent.get(ModelExtensionInterfaceInfo.class);
        return parentInfo == null ? parent : parentInfo.getRoot();
    }

    @Override
    public String toString() {
        return classInfo.toString();
    }
}
