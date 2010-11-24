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
package com.googlecode.ddom.weaver.asm;

import java.util.List;

import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Method adapter that inserts additional method calls into a constructor. All methods must be
 * private, have no arguments and return void. The method calls are inserted right after the call to
 * the constructor of the superclass.
 * 
 * @author Andreas Veithen
 */
public class ConstructorEnhancer extends MethodAdapter {
    private final String className;
    private final List<String> methodNames;
    private boolean inlined;
    
    public ConstructorEnhancer(MethodVisitor mv, String className, List<String> methodNames) {
        super(mv);
        this.className = className;
        this.methodNames = methodNames;
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        super.visitMethodInsn(opcode, owner, name, desc);
        if (!inlined && opcode == Opcodes.INVOKESPECIAL) {
            for (String methodName : methodNames) {
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitMethodInsn(Opcodes.INVOKESPECIAL, className, methodName, "()V");
            }
            inlined = true;
        }
    }
}
