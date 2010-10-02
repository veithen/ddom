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

class ImplementationInfo {
    private final WeavableClassInfo implementation;
    private final ClassInfo factoryInterface;
    private final List<ConstructorInfo> constructors;
    private final List<ModelExtension> modelExtensions = new ArrayList<ModelExtension>();

    ImplementationInfo(WeavableClassInfo implementation, ClassInfo factoryInterface, List<ConstructorInfo> constructors) {
        this.implementation = implementation;
        this.factoryInterface = factoryInterface;
        this.constructors = constructors;
    }

    WeavableClassInfo getImplementation() {
        return implementation;
    }

    ClassInfo getFactoryInterface() {
        return factoryInterface;
    }

    List<ConstructorInfo> getConstructors() {
        return constructors;
    }

    String getFactoryDelegateInterfaceName() {
        return implementation.getName() + "$$FactoryDelegate";
    }

    List<ModelExtension> getModelExtensions() {
        return modelExtensions;
    }
    
    void addModelExtension(ModelExtension modelExtension) {
        modelExtensions.add(modelExtension);
    }
}
