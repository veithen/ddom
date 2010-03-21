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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class MethodVisitorTee implements MethodVisitor {
    private final List<MethodVisitor> visitors = new ArrayList<MethodVisitor>();
    
    public MethodVisitorTee(MethodVisitor... visitors) {
        Collections.addAll(this.visitors, visitors);
    }

    public void addVisitor(MethodVisitor visitor) {
        visitors.add(visitor);
    }
    
    public static MethodVisitor mergeVisitors(MethodVisitor current, MethodVisitor visitor) {
        if (visitor != null) {
            if (current == null) {
                return visitor;
            } else if (current instanceof MethodVisitorTee) {
                ((MethodVisitorTee)current).addVisitor(visitor);
                return current;
            } else {
                MethodVisitorTee tee = new MethodVisitorTee(current);
                tee.addVisitor(visitor);
                return tee;
            }
        } else {
            return current;
        }
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor result = null;
        for (MethodVisitor visitor : visitors) {
            result = AnnotationVisitorTee.mergeVisitors(result, visitor.visitAnnotation(desc, visible));
        }
        return result;
    }

    public AnnotationVisitor visitAnnotationDefault() {
        AnnotationVisitor result = null;
        for (MethodVisitor visitor : visitors) {
            result = AnnotationVisitorTee.mergeVisitors(result, visitor.visitAnnotationDefault());
        }
        return result;
    }

    public void visitAttribute(Attribute attr) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitAttribute(attr);
        }
    }

    public void visitCode() {
        for (MethodVisitor visitor : visitors) {
            visitor.visitCode();
        }
    }

    public void visitEnd() {
        for (MethodVisitor visitor : visitors) {
            visitor.visitEnd();
        }
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitFieldInsn(opcode, owner, name, desc);
        }
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitFrame(type, nLocal, local, nStack, stack);
        }
    }

    public void visitIincInsn(int var, int increment) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitIincInsn(var, increment);
        }
    }

    public void visitInsn(int opcode) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitInsn(opcode);
        }
    }

    public void visitIntInsn(int opcode, int operand) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitIntInsn(opcode, operand);
        }
    }

    public void visitJumpInsn(int opcode, Label label) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitJumpInsn(opcode, label);
        }
    }

    public void visitLabel(Label label) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitLabel(label);
        }
    }

    public void visitLdcInsn(Object cst) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitLdcInsn(cst);
        }
    }

    public void visitLineNumber(int line, Label start) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitLineNumber(line, start);
        }
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitLocalVariable(name, desc, signature, start, end, index);
        }
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitLookupSwitchInsn(dflt, keys, labels);
        }
    }

    public void visitMaxs(int maxStack, int maxLocals) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitMaxs(maxStack, maxLocals);
        }
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    public void visitMultiANewArrayInsn(String desc, int dims) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitMultiANewArrayInsn(desc, dims);
        }
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        AnnotationVisitor result = null;
        for (MethodVisitor visitor : visitors) {
            result = AnnotationVisitorTee.mergeVisitors(result, visitor.visitParameterAnnotation(parameter, desc, visible));
        }
        return result;
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitTableSwitchInsn(min, max, dflt, labels);
        }
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitTryCatchBlock(start, end, handler, type);
        }
    }

    public void visitTypeInsn(int opcode, String type) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitTypeInsn(opcode, type);
        }
    }

    public void visitVarInsn(int opcode, int var) {
        for (MethodVisitor visitor : visitors) {
            visitor.visitVarInsn(opcode, var);
        }
    }
}
