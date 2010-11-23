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
package com.googlecode.ddom.weaver.compound;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.googlecode.ddom.weaver.asm.AbstractClassVisitor;
import com.googlecode.ddom.weaver.asm.Util;

class CompoundClassGenerator extends AbstractClassVisitor {
    private final String className;
    private final String ifaceName;
    private final String[] componentClasses;
    private final ClassVisitor cv;

    public CompoundClassGenerator(String className, String ifaceName, String[] componentClasses, ClassVisitor cv) {
        this.className = Util.classNameToInternalName(className);
        this.ifaceName = Util.classNameToInternalName(ifaceName);
        this.componentClasses = componentClasses;
        this.cv = cv;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, className, null, "java/lang/Object", new String[] { ifaceName });
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, name, desc, signature, exceptions);
        if (mv != null) {
            Type[] argumentTypes = Type.getArgumentTypes(desc);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            for (int i = 0; i<componentClasses.length; i++) {
                String componentClass = Util.classNameToInternalName(componentClasses[i]);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, className, "c" + i, "L" + componentClass + ";");
                for (int j = 0; j<argumentTypes.length; j++) {
                    mv.visitVarInsn(argumentTypes[j].getOpcode(Opcodes.ILOAD), j+1);
                }
                mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, ifaceName, name, desc);
            }
            mv.visitInsn(Opcodes.RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + className + ";", null, l0, l1, 0);
            mv.visitMaxs(argumentTypes.length + 1, argumentTypes.length + 1);
            mv.visitEnd();
        }
        return null;
    }

    @Override
    public void visitEnd() {
        for (int i = 0; i<componentClasses.length; i++) {
            String componentClass = Util.classNameToInternalName(componentClasses[i]);
            FieldVisitor fv = cv.visitField(Opcodes.ACC_PRIVATE, "c" + i, "L" + componentClass + ";", null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        if (mv != null) {
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            for (int i = 0; i<componentClasses.length; i++) {
                String componentClass = Util.classNameToInternalName(componentClasses[i]);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitTypeInsn(Opcodes.NEW, componentClass);
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, componentClass, "<init>", "()V");
                mv.visitFieldInsn(Opcodes.PUTFIELD, className, "c" + i, "L" + componentClass + ";");
            }
            mv.visitInsn(Opcodes.RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + className + ";", null, l0, l1, 0);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        cv.visitEnd();
    }
}
