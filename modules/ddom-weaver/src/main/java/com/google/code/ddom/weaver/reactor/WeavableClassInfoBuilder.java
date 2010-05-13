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
package com.google.code.ddom.weaver.reactor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

import com.google.code.ddom.weaver.asm.AbstractClassVisitor;
import com.google.code.ddom.weaver.asm.Util;
import com.google.code.ddom.weaver.jsr45.SourceInfoBuilder;
import com.google.code.ddom.weaver.realm.ClassInfo;

public class WeavableClassInfoBuilder extends AbstractClassVisitor {
    private final Reactor reactor;
    private final ClassDefinitionSource classDefinitionSource;
    private final SourceInfoBuilder sourceInfoBuilder;
    private String name;
    private boolean isInterface;
    private String superName;
    private String[] interfaceNames;
    private boolean isImplementation;
    
    public WeavableClassInfoBuilder(Reactor reactor, ClassDefinitionSource classDefinitionSource, SourceInfoBuilder sourceInfoBuilder) {
        this.reactor = reactor;
        this.classDefinitionSource = classDefinitionSource;
        this.sourceInfoBuilder = sourceInfoBuilder;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = Util.internalNameToClassName(name);
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
        this.superName = superName;
        interfaceNames = interfaces;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lcom/google/code/ddom/backend/Implementation;")) {
            isImplementation = true;
        }
        // No need to actually get the details of the annotation => always return null
        return null;
    }

    public String getName() {
        return name;
    }

    public WeavableClassInfo build() throws ClassNotFoundException {
        ClassInfo[] interfaces = new ClassInfo[interfaceNames.length];
        for (int i=0; i<interfaces.length; i++) {
            interfaces[i] = reactor.getClassInfo(Util.internalNameToClassName(interfaceNames[i]));
        }
        return new WeavableClassInfo(name, isInterface, reactor.getClassInfo(Util.internalNameToClassName(superName)),
                interfaces, classDefinitionSource, sourceInfoBuilder.getSourceInfo(), isImplementation);
    }
}
