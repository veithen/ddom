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

/**
 * Method adapter that inserts additional method calls at the beginning of a method. All methods
 * must be private, have no arguments and return void.
 * 
 * @author Andreas Veithen
 */
public class MethodEnhancer extends MethodAdapter {
    private final String className;
    private final List<String> methodNames;
    private final boolean isStatic;
    private boolean inlined;
    
    public MethodEnhancer(MethodVisitor mv, String className, List<String> methodNames, boolean isStatic) {
        super(mv);
        this.className = className;
        this.methodNames = methodNames;
        this.isStatic = isStatic;
    }

    private void injectIfNotInlined() {
        if (!inlined) {
            // TODO
            inlined = true;
        }
    }

    // TODO: we need to intercept other methods
    @Override
    public void visitInsn(int opcode) {
        injectIfNotInlined();
        super.visitInsn(opcode);
    }
}
