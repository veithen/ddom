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

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.googlecode.ddom.weaver.asm.Util;

/**
 * Injector that injects a field value by creating a new instance of a given class.
 * 
 * @author Andreas Veithen
 */
public class PrototypeInjector implements Injector {
    private final String className;
    
    public PrototypeInjector(String className) {
        this.className = Util.classNameToInternalName(className);
    }

    public void generateInjectorMethod(String owner, String fieldName, String fieldDesc, MethodVisitor mv) {
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.NEW, className);
        mv.visitInsn(Opcodes.DUP);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, className, "<init>", "()V");
        mv.visitFieldInsn(Opcodes.PUTFIELD, owner, fieldName, fieldDesc);
        mv.visitInsn(Opcodes.RETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "L" + owner + ";", null, l0, l1, 0);
        mv.visitMaxs(3, 1);
        mv.visitEnd();
    }
}
