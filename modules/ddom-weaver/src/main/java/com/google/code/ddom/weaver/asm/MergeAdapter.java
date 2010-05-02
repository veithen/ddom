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
package com.google.code.ddom.weaver.asm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Class adapter that merges a set of mixins into a given target class. This consists in the
 * following actions:
 * <ul>
 * <li>Add methods defined by the mixins into the target class.
 * <li>Merge the code of the mixin constructor into the constructor(s) of the target class.
 * <li>Replace all references to the mixin class by references to the target class.
 * <li>Support JSR-45, i.e. add an SMAP and transform line number information.
 * </ul>
 */
public class MergeAdapter extends ClassAdapter {
    final List<MixinInfo> mixins;
    private final SourceMapper sourceMapper;
    private Remapper remapper;
    
    public MergeAdapter(ClassVisitor cv, List<MixinInfo> mixins, SourceMapper sourceMapper) {
        super(cv);
        this.mixins = mixins;
        this.sourceMapper = sourceMapper;
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Set<String> mergedInterfaces = new HashSet<String>();
        mergedInterfaces.addAll(Arrays.asList(interfaces));
        for (MixinInfo mixin : mixins) {
            mergedInterfaces.addAll(mixin.getContributedInterfaces());
        }
        super.visit(version, access, name, signature, superName, mergedInterfaces.toArray(new String[mergedInterfaces.size()]));
        Map<String,String> mapping = new HashMap<String,String>();
        for (MixinInfo mixin : mixins) {
            mapping.put(mixin.getName(), name);
        }
        remapper = new SimpleRemapper(mapping);
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (visitor != null) {
            visitor = new RemappingMethodAdapter(access, desc, visitor, remapper);
            visitor = new MethodAdapter(visitor) {
                private boolean inlined;
                
                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String desc) {
                    super.visitMethodInsn(opcode, owner, name, desc);
                    if (!inlined && opcode == Opcodes.INVOKESPECIAL) {
                        Label end = new Label();
                        for (MixinInfo mixin : mixins) {
                            for (Iterator<?> it = mixin.getInitInstructions().iterator(); it.hasNext(); ) {
                                AbstractInsnNode insn = (AbstractInsnNode)it.next();
                                if (insn instanceof InsnNode && insn.getOpcode() == Opcodes.RETURN) {
                                    mv.visitJumpInsn(Opcodes.GOTO, end);
                                } else {
                                    insn.accept(mv);
                                }
                            }
                        }
                        mv.visitLabel(end);
                        inlined = true;
                    }
                }
            };
        }
        return visitor;
    }

    @Override
    public void visitEnd() {
        for (MixinInfo mixin : mixins) {
            for (FieldNode field : mixin.getFields()) {
                System.out.println(field.name);
                field.accept(this);
            }
            for (MethodNode mn : mixin.getMethods()) {
                String[] exceptions = new String[mn.exceptions.size()];
                mn.exceptions.toArray(exceptions);
                MethodVisitor mv = cv.visitMethod(mn.access, mn.name, mn.desc, mn.signature, exceptions);
                if (mv != null) {
                    mn.instructions.resetLabels();
                    mv = sourceMapper.getMethodAdapter(mixin.getSourceInfo(), mv);
                    mv = new RemappingMethodAdapter(mn.access, mn.desc, mv, remapper);
                    mn.accept(mv);
                }
            }
        }
        super.visitEnd();
    }
}
