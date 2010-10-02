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
import com.google.code.ddom.weaver.reactor.GeneratedClass;

class ModelExtensionFactoryDelegateImplementation extends GeneratedClass {
    private final ImplementationInfo implementationInfo;
    private final ModelExtensionClassInfo modelExtensionClassInfo;
    
    ModelExtensionFactoryDelegateImplementation(ImplementationInfo implementationInfo, ModelExtensionClassInfo modelExtensionClassInfo) {
        this.implementationInfo = implementationInfo;
        this.modelExtensionClassInfo = modelExtensionClassInfo;
    }

    public void accept(ClassVisitor classVisitor) {
        String factoryName = Util.classNameToInternalName(modelExtensionClassInfo.getFactoryDelegateImplementationClassName());
        classVisitor.visit(
                Opcodes.V1_5,
                Opcodes.ACC_PUBLIC,
                factoryName,
                null,
                "java/lang/Object",
                new String[] { Util.classNameToInternalName(implementationInfo.getFactoryDelegateInterfaceName()) });
        String className = Util.classNameToInternalName(modelExtensionClassInfo.getClassName());
        {
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            if (mv != null) {
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", "L" + className + ";", null, l0, l1, 0);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
        }
        for (ConstructorInfo constructor : implementationInfo.getConstructors()) {
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, "create", constructor.getDescriptor(), constructor.getSignature(), constructor.getExceptions());
            if (mv != null) {
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitTypeInsn(Opcodes.NEW, className);
                mv.visitInsn(Opcodes.DUP);
                Type[] argumentTypes = constructor.getArgumentTypes();
                for (int i=0; i<argumentTypes.length; i++) {
                    mv.visitVarInsn(argumentTypes[i].getOpcode(Opcodes.ILOAD), i+1);
                }
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, className, "<init>", constructor.getDescriptor());
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", "L" + factoryName + ";", null, l0, l1, 0);
                for (int i=0; i<argumentTypes.length; i++) {
                    mv.visitLocalVariable("arg" + i, argumentTypes[i].getDescriptor(), null, l0, l1, i+1);
                }
                mv.visitMaxs(argumentTypes.length + 2, argumentTypes.length + 1);
                mv.visitEnd();
            }
        }
        classVisitor.visitEnd();
    }
}
