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

import com.google.code.ddom.weaver.asm.Util;
import com.google.code.ddom.weaver.reactor.GeneratedClass;

public class ModelExtensionFactoryImplementation extends GeneratedClass {
    private final ImplementationInfo info;

    ModelExtensionFactoryImplementation(ImplementationInfo info) {
        this.info = info;
    }

    public void accept(ClassVisitor classVisitor) {
        String name = Util.classNameToInternalName(info.getFactoryInterface().getName() + "__Impl");
        classVisitor.visit(
                Opcodes.V1_5,
                Opcodes.ACC_PUBLIC,
                name,
                null,
                "java/lang/Object",
                new String[0]);
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
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
                
                mv.visitInsn(Opcodes.RETURN);
                Label l1 = new Label();
                mv.visitLabel(l1);
                mv.visitLocalVariable("this", "L" + name + ";", null, l0, l1, 0);
                mv.visitMaxs(1, 1);
                mv.visitEnd();
            }
        }
        classVisitor.visitEnd();
    }
}
