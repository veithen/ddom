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

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.code.ddom.weaver.asm.Util;
import com.google.code.ddom.weaver.implementation.ConstructorInfo;
import com.google.code.ddom.weaver.implementation.ImplementationInfo;
import com.google.code.ddom.weaver.reactor.GeneratedClass;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;

class ModelExtensionFactoryDelegateInterface extends GeneratedClass {
    private final WeavableClassInfo implementation;
    
    ModelExtensionFactoryDelegateInterface(WeavableClassInfo implementation) {
        this.implementation = implementation;
    }

    public void accept(ClassVisitor classVisitor) {
        ImplementationInfo implementationInfo = implementation.get(ImplementationInfo.class);
        String factoryName = Util.classNameToInternalName(implementation.getName() + "__FactoryDelegate");
        // TODO: the reactor currently has an issue with interfaces
        classVisitor.visit(
                Opcodes.V1_5,
                Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT /*| Opcodes.ACC_INTERFACE*/,
                factoryName,
                null,
                "java/lang/Object",
                new String[0]);
        for (ConstructorInfo constructor : implementationInfo.getConstructors()) {
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, "create", constructor.getDescriptor(), constructor.getSignature(), constructor.getExceptions());
            if (mv != null) {
                mv.visitEnd();
            }
        }
        classVisitor.visitEnd();
    }
}
