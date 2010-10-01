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
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.google.code.ddom.weaver.asm.Util;
import com.google.code.ddom.weaver.implementation.ConstructorInfo;
import com.google.code.ddom.weaver.implementation.ImplementationInfo;
import com.google.code.ddom.weaver.reactor.GeneratedClass;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.realm.ClassInfo;

class ModelExtensionClass extends GeneratedClass {
    private final WeavableClassInfo implementation;
    private final ClassInfo rootInterface;
    private final ClassInfo extensionInterface;

    ModelExtensionClass(WeavableClassInfo implementation, ClassInfo rootInterface, ClassInfo extensionInterface) {
        this.implementation = implementation;
        this.rootInterface = rootInterface;
        this.extensionInterface = extensionInterface;
    }
    
    private String getModelExtensionClassName(ClassInfo extensionInterface) {
        return implementation.getName() + "__" + extensionInterface.getName().replace('.', '_');
    }
    
    public void accept(ClassVisitor classVisitor) {
        ImplementationInfo implementationInfo = implementation.get(ImplementationInfo.class);
        ClassInfo superInterface = extensionInterface.getInterfaces()[0];
        String name = Util.classNameToInternalName(getModelExtensionClassName(extensionInterface));
        String superName = Util.classNameToInternalName(superInterface == rootInterface ? implementation.getName() : getModelExtensionClassName(superInterface));
        classVisitor.visit(
                Opcodes.V1_5,
                Opcodes.ACC_PUBLIC,
                name,
                null,
                superName,
                new String[] { Util.classNameToInternalName(extensionInterface.getName()) });
        for (ConstructorInfo constructor : implementationInfo.getConstructors()) {
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, "<init>", constructor.getDescriptor(), constructor.getSignature(), constructor.getExceptions());
            if (mv != null) {
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                Type[] argumentTypes = constructor.getArgumentTypes();
                for (int i=0; i<argumentTypes.length; i++) {
                    mv.visitVarInsn(argumentTypes[i].getOpcode(Opcodes.ILOAD), i+1);
                }
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, superName, "<init>", constructor.getDescriptor());
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", "L" + name + ";", null, l0, l1, 0);
                for (int i=0; i<argumentTypes.length; i++) {
                    mv.visitLocalVariable("arg" + i, argumentTypes[i].getDescriptor(), null, l0, l1, i+1);
                }
                mv.visitMaxs(argumentTypes.length + 1, argumentTypes.length + 1);
                mv.visitEnd();
            }
        }
        classVisitor.visitEnd();
    }
}
