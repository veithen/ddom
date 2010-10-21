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
package com.google.code.ddom.weaver.inject;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;

import com.google.code.ddom.weaver.reactor.ClassTransformation;
import com.google.code.ddom.weaver.reactor.ReactorPlugin;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;

public class InjectionPlugin extends ReactorPlugin {
    private final Map<String,Injector> bindings = new HashMap<String,Injector>();
    
    public void addBinding(String fieldType, Injector injector) {
        bindings.put(fieldType, injector);
    }

    @Override
    public WeavableClassInfoBuilderCollaborator newWeavableClassInfoBuilderCollaborator() {
        return new InjectionInfoBuilder(bindings);
    }

    @Override
    public ClassTransformation getPreTransformation(WeavableClassInfo classInfo) {
        final InjectionInfo injectionInfo = classInfo.get(InjectionInfo.class);
        if (injectionInfo == null) {
            return null;
        } else {
            return new ClassTransformation() {
                public ClassVisitor adapt(ClassVisitor cv) {
                    return new InjectionAdapter(cv, injectionInfo);
                }
            };
        }
    }
}
