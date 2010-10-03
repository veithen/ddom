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
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.google.code.ddom.weaver.asm.Util;
import com.google.code.ddom.weaver.reactor.GeneratedClass;
import com.google.code.ddom.weaver.realm.ClassInfo;

public class ModelExtensionFactoryImplementation extends GeneratedClass {
    private final ImplementationInfo implementationInfo;

    ModelExtensionFactoryImplementation(ImplementationInfo implementationInfo) {
        this.implementationInfo = implementationInfo;
    }

    public void accept(ClassVisitor classVisitor) {
        // Note: the name chosen here must match what is expected in ExtensionFactoryLocator
        String name = Util.classNameToInternalName(implementationInfo.getFactoryInterface().getName() + "$$Impl");
        String factoryInterfaceName = Util.classNameToInternalName(implementationInfo.getFactoryInterface().getName());
        classVisitor.visit(
                Opcodes.V1_5,
                Opcodes.ACC_PUBLIC,
                name,
                null,
                "java/lang/Object",
                new String[] { factoryInterfaceName });
        {
            FieldVisitor fw = classVisitor.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_STATIC, "INSTANCE", "L" + factoryInterfaceName + ";", null, null);
            if (fw != null) {
                fw.visitEnd();
            }
        }
        {
            FieldVisitor fw = classVisitor.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_FINAL, "delegates", "Ljava/util/Map;", null, null);
            if (fw != null) {
                fw.visitEnd();
            }
        }
        {
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_PRIVATE, "<init>", "()V", null, null);
            if (mv != null) {
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                // Call constructor from superclass
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
                // Create delegates map
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitTypeInsn(Opcodes.NEW, "java/util/HashMap");
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/HashMap", "<init>", "()V");
                mv.visitFieldInsn(Opcodes.PUTFIELD, name, "delegates", "Ljava/util/Map;");
                // Populate delegates map
                for (ModelExtensionInfo modelExtensionInfo : implementationInfo.getModelExtensions()) {
                    for (ClassInfo extensionInterface : modelExtensionInfo.getExtensionInterfaces()) {
                        // TODO: this is stupid; we should not recreate the info object here
                        ModelExtensionClassInfo modelExtensionClassInfo = new ModelExtensionClassInfo(implementationInfo.getImplementation(), modelExtensionInfo.getRootInterface(), extensionInterface);
                        String factoryDelegateImplName = Util.classNameToInternalName(modelExtensionClassInfo.getFactoryDelegateImplementationClassName());
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        mv.visitFieldInsn(Opcodes.GETFIELD, name, "delegates", "Ljava/util/Map;");
                        mv.visitLdcInsn(Type.getObjectType(Util.classNameToInternalName(modelExtensionClassInfo.getExtensionInterface().getName())));
                        mv.visitTypeInsn(Opcodes.NEW, factoryDelegateImplName);
                        mv.visitInsn(Opcodes.DUP);
                        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, factoryDelegateImplName, "<init>", "()V");
                        mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
                        mv.visitInsn(Opcodes.POP);
                    }
                }
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", "L" + name + ";", null, l0, l1, 0);
                mv.visitMaxs(4, 1);
                mv.visitEnd();
            }
        }
        String factoryDelegateInterfaceName = Util.classNameToInternalName(implementationInfo.getFactoryDelegateInterfaceName());
        String getDelegateDesc = "(Ljava/lang/Class;)L" + factoryDelegateInterfaceName + ";";
        {
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_PRIVATE, "getDelegate", getDelegateDesc, null, null);
            if (mv != null) {
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, name, "delegates", "Ljava/util/Map;");
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
                mv.visitTypeInsn(Opcodes.CHECKCAST, factoryDelegateInterfaceName);
                mv.visitInsn(Opcodes.ARETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", "L" + name + ";", null, l0, l1, 0);
                mv.visitLocalVariable("extensionInterface", "Ljava/lang/Class;", null, l0, l1, 1);
                mv.visitMaxs(2, 2);
                mv.visitEnd();
            }
        }
        String implementationName = Util.classNameToInternalName(implementationInfo.getImplementation().getName());
        for (ConstructorInfo constructor : implementationInfo.getConstructors()) {
            Type[] constructorArgumentTypes = constructor.getArgumentTypes();
            Type[] argumentTypes = new Type[constructorArgumentTypes.length+1];
            argumentTypes[0] = Type.getObjectType("java/lang/Class");
            System.arraycopy(constructorArgumentTypes, 0, argumentTypes, 1, constructorArgumentTypes.length);
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_PUBLIC, "create", Type.getMethodDescriptor(Type.getObjectType(implementationName), argumentTypes), null, null);
            if (mv != null) {
                mv.visitCode();
                Label l0 = new Label();
                mv.visitLabel(l0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                Label l1 = new Label();
                mv.visitJumpInsn(Opcodes.IFNONNULL, l1);
                mv.visitTypeInsn(Opcodes.NEW, implementationName);
                mv.visitInsn(Opcodes.DUP);
                for (int i=0; i<constructorArgumentTypes.length; i++) {
                    mv.visitVarInsn(constructorArgumentTypes[i].getOpcode(Opcodes.ILOAD), i+2);
                }
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, implementationName, "<init>", constructor.getDescriptor());
                mv.visitInsn(Opcodes.ARETURN);
                mv.visitLabel(l1);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitVarInsn(Opcodes.ALOAD, 1);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, name, "getDelegate", getDelegateDesc);
                for (int i=0; i<constructorArgumentTypes.length; i++) {
                    mv.visitVarInsn(constructorArgumentTypes[i].getOpcode(Opcodes.ILOAD), i+2);
                }
                mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, factoryDelegateInterfaceName, "create", constructor.getFactoryDelegateMethodDescriptor());
                mv.visitInsn(Opcodes.ARETURN);
                Label l3 = new Label();
                mv.visitLabel(l3);
                mv.visitLocalVariable("this", "L" + name + ";", null, l0, l3, 0);
                mv.visitLocalVariable("extensionInterface", "Ljava/lang/Class;", null, l0, l3, 1);
                for (int i=0; i<constructorArgumentTypes.length; i++) {
                    mv.visitLocalVariable("arg" + i, constructorArgumentTypes[i].getDescriptor(), null, l0, l3, i+2);
                }
                mv.visitMaxs(argumentTypes.length+1, argumentTypes.length+1);
                mv.visitEnd();
            }
        }
        {
            MethodVisitor mv = classVisitor.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
            if (mv != null) {
                mv.visitCode();
                mv.visitTypeInsn(Opcodes.NEW, name);
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, name, "<init>", "()V");
                mv.visitFieldInsn(Opcodes.PUTSTATIC, name, "INSTANCE", "L" + factoryInterfaceName + ";");
                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(2, 0);
                mv.visitEnd();
            }
        }
        classVisitor.visitEnd();
    }
}
