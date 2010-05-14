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
package com.google.code.ddom.weaver.mixin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
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

import com.google.code.ddom.weaver.asm.ErrorHandler;
import com.google.code.ddom.weaver.jsr45.SourceMapper;

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
    private static final Logger log = Logger.getLogger(MergeAdapter.class.getName());
    
    final List<MixinInfo> mixins;
    private final SourceMapper sourceMapper;
    private final ErrorHandler errorHandler;
    private Remapper remapper;
    
    /**
     * The set of methods that have already been written to the output class. Used to detect method
     * collisions.
     */
    private final Set<String> seenMethods = new HashSet<String>();
    
    /**
     * The set of fields that have already been written to the output class. Used to detect field
     * collisions.
     */
    private final Set<String> seenFields = new HashSet<String>();
    
    public MergeAdapter(ClassVisitor cv, List<MixinInfo> mixins, SourceMapper sourceMapper, ErrorHandler errorHandler) {
        super(cv);
        this.mixins = mixins;
        this.sourceMapper = sourceMapper;
        this.errorHandler = errorHandler;
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
        if (log.isLoggable(Level.FINER)) {
            log.finer("Visiting method " + name + desc + " from base class");
        }
        seenMethods.add(name + desc);
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
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (log.isLoggable(Level.FINER)) {
            log.finer("Visiting field " + name + " from base class");
        }
        seenFields.add(name);
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        for (MixinInfo mixin : mixins) {
            for (FieldNode field : mixin.getFields()) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Merging field " + field.name + " from mixin " + mixin.getName());
                }
                if (!seenFields.add(field.name)) {
                    errorHandler.handleError("Duplicate field " + field.name);
                }
                field.accept(this);
            }
            for (MethodNode mn : mixin.getMethods()) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Merging method " + mn.name + mn.desc + " from mixin " + mixin.getName());
                }
                if (!seenMethods.add(mn.name + mn.desc)) {
                    errorHandler.handleError("Method " + mn.name + mn.desc + " of mixin " + mixin.getName() + " collides with a method declared in the base class or another mixin");
                }
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