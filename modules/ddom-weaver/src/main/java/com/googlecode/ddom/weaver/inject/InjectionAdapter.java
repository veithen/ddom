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
package com.googlecode.ddom.weaver.inject;

import java.util.Arrays;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.googlecode.ddom.weaver.mixin.ConstructorEnhancer;

class InjectionAdapter extends ClassAdapter {
    private final InjectionInfo injectionInfo;
    private String className;
    
    public InjectionAdapter(ClassVisitor cv, InjectionInfo injectionInfo) {
        super(cv);
        this.injectionInfo = injectionInfo;
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && name.equals("<init>")) {
            mv = new ConstructorEnhancer(mv, className, Arrays.asList("inject$$instance"));
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        for (InjectableFieldInfo fieldInfo : injectionInfo.getInjectableFields()) {
            MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, fieldInfo.getFactoryMethodName(), fieldInfo.getFactoryMethodDesc(), null, new String[0]);
            if (mv != null) {
                fieldInfo.getInjector().generateFactoryMethodCode(mv);
            }
        }
        MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PRIVATE, "inject$$instance", "()V", null, new String[0]);
        if (mv != null) {
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            for (InjectableFieldInfo fieldInfo : injectionInfo.getInjectableFields()) {
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, className, fieldInfo.getFactoryMethodName(), fieldInfo.getFactoryMethodDesc());
                mv.visitFieldInsn(Opcodes.PUTFIELD, className, fieldInfo.getFieldName(), fieldInfo.getFieldDesc());
            }
            mv.visitInsn(Opcodes.RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + className + ";", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        super.visitEnd();
    }
}
