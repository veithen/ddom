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
package com.google.code.ddom.weaver.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Converts a constructor into a normal method. This will remove the call to the constructor of the
 * superclass. Note that this is only meaningful for default constructors.
 */
public class ConstructorToMethodConverter implements MethodVisitor {
    private final MethodVisitor mv;
    private boolean keep;
    
    public ConstructorToMethodConverter(MethodVisitor mv) {
        this.mv = mv;
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
        if (keep) {
            mv.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        if (keep) {
            mv.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

    public void visitIincInsn(int var, int increment) {
        if (keep) {
            mv.visitIincInsn(var, increment);
        }
    }

    public void visitInsn(int opcode) {
        if (keep) {
            mv.visitInsn(opcode);
        }
    }

    public void visitIntInsn(int opcode, int operand) {
        if (keep) {
            mv.visitIntInsn(opcode, operand);
        }
    }

    public void visitJumpInsn(int opcode, Label label) {
        if (keep) {
            mv.visitJumpInsn(opcode, label);
        }
    }

    public void visitLabel(Label label) {
        if (keep) {
            mv.visitLabel(label);
        }
    }

    public void visitLdcInsn(Object cst) {
        if (keep) {
            mv.visitLdcInsn(cst);
        }
    }

    public void visitLineNumber(int line, Label start) {
        if (keep) {
            mv.visitLineNumber(line, start);
        }
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        if (keep) {
            mv.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        if (keep) {
            mv.visitLookupSwitchInsn(dflt, keys, labels);
        }
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        if (keep) {
            mv.visitMaxs(maxStack, maxLocals);
        }
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (keep) {
            mv.visitMethodInsn(opcode, owner, name, desc);
        } else if (opcode == Opcodes.INVOKESPECIAL) {
            keep = true;
        }
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        if (keep) {
            mv.visitMultiANewArrayInsn(desc, dims);
        }
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        if (keep) {
            mv.visitTableSwitchInsn(min, max, dflt, labels);
        }
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        if (keep) {
            mv.visitTryCatchBlock(start, end, handler, type);
        }
    }

    public void visitTypeInsn(int opcode, String type) {
        if (keep) {
            mv.visitTypeInsn(opcode, type);
        }
    }

    public void visitVarInsn(int opcode, int var) {
        if (keep) {
            mv.visitVarInsn(opcode, var);
        }
    }
}
