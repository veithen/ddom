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

import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.realm.ClassInfo;

class ModelExtensionClassInfo {
    private final WeavableClassInfo implementation;
    private final ClassInfo rootInterface;
    private final ClassInfo extensionInterface;
    
    ModelExtensionClassInfo(WeavableClassInfo implementation, ClassInfo rootInterface, ClassInfo extensionInterface) {
        this.implementation = implementation;
        this.rootInterface = rootInterface;
        this.extensionInterface = extensionInterface;
    }

    public WeavableClassInfo getImplementation() {
        return implementation;
    }

    public ClassInfo getRootInterface() {
        return rootInterface;
    }

    public ClassInfo getExtensionInterface() {
        return extensionInterface;
    }

    private String getModelExtensionClassName(ClassInfo extensionInterface) {
        return implementation.getName() + "__" + extensionInterface.getName().replace('.', '_');
    }
    
    public String getClassName() {
        return extensionInterface == null ? implementation.getName() : getModelExtensionClassName(extensionInterface);
    }
    
    public String getFactoryDelegateImplementationClassName() {
        return getClassName() + "__FactoryDelegateImpl";
    }
    
    public String getSuperClassName() {
        ClassInfo superInterface = extensionInterface.getInterfaces()[0];
        return superInterface == rootInterface ? implementation.getName() : getModelExtensionClassName(superInterface);
    }
}
