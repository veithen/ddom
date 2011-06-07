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
package com.googlecode.ddom.weaver.mixin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Converts a constructor into a normal method. This will remove the call to the constructor of the
 * superclass. Note that this is only meaningful if the constructor is a default constructor (i.e.
 * has no parameters) and calls the default constructor of the superclass. By definition, this is
 * the case for mixins.
 * <p>
 * The class actually looks for the following sequence of instructions and removes them:
 * 
 * <pre>
 *    ALOAD 0
 *    INVOKESPECIAL java/lang/Object.&lt;init> ()V
 * </pre>
 * 
 * Any instructions before and after will be preserved. This also includes the code inserted by
 * Cobertura before the call to the constructor of the superclass. This ensures that coverage
 * reports are accurate.
 */
public class ConstructorToMethodConverter implements MethodVisitor {
    private final MethodVisitor mv;
    
    /**
     * Flag indicating that the last processed instruction was ALOAD 0. This will only be set if
     * {@link #callRemoved} is <code>false</code>.
     */
    private boolean lastWasALoad0;
    
    /**
     * Flag indicating that the call to the constructor of the superclass has been removed.
     */
    private boolean callRemoved;
    
    public ConstructorToMethodConverter(MethodVisitor mv) {
        this.mv = mv;
    }
    
    private void reset() {
        if (lastWasALoad0) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            lastWasALoad0 = false;
        }
    }
    
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return mv.visitAnnotation(desc, visible);
    }

    public AnnotationVisitor visitAnnotationDefault() {
        return mv.visitAnnotationDefault();
    }

    public void visitAttribute(Attribute attr) {
        mv.visitAttribute(attr);
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        return mv.visitParameterAnnotation(parameter, desc, visible);
    }

    public void visitCode() {
        mv.visitCode();
    }

    public void visitEnd() {
        mv.visitEnd();
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        reset();
        mv.visitFieldInsn(opcode, owner, name, desc);
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        reset();
        mv.visitFrame(type, nLocal, local, nStack, stack);
    }

    public void visitIincInsn(int var, int increment) {
        reset();
        mv.visitIincInsn(var, increment);
    }

    public void visitInsn(int opcode) {
        reset();
        mv.visitInsn(opcode);
    }

    public void visitIntInsn(int opcode, int operand) {
        reset();
        mv.visitIntInsn(opcode, operand);
    }

    public void visitJumpInsn(int opcode, Label label) {
        reset();
        mv.visitJumpInsn(opcode, label);
    }

    public void visitLabel(Label label) {
        reset();
        mv.visitLabel(label);
    }

    public void visitLdcInsn(Object cst) {
        reset();
        mv.visitLdcInsn(cst);
    }

    public void visitLineNumber(int line, Label start) {
        reset();
        mv.visitLineNumber(line, start);
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        reset();
        mv.visitLocalVariable(name, desc, signature, start, end, index);
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        reset();
        mv.visitLookupSwitchInsn(dflt, keys, labels);
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        reset();
        mv.visitMaxs(maxStack, maxLocals);
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (lastWasALoad0 && opcode == Opcodes.INVOKESPECIAL) {
            lastWasALoad0 = false;
            callRemoved = true;
        } else {
            reset();
            mv.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        reset();
        mv.visitMultiANewArrayInsn(desc, dims);
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        reset();
        mv.visitTableSwitchInsn(min, max, dflt, labels);
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        reset();
        mv.visitTryCatchBlock(start, end, handler, type);
    }

    public void visitTypeInsn(int opcode, String type) {
        reset();
        mv.visitTypeInsn(opcode, type);
    }

    public void visitVarInsn(int opcode, int var) {
        reset();
        if (!callRemoved && opcode == Opcodes.ALOAD && var == 0) {
            lastWasALoad0 = true;
        } else {
            mv.visitVarInsn(opcode, var);
        }
    }
}
