package com.google.code.ddom.weaver.asm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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
    final MixinInfo mixinInfo;
    private final SourceMapper sourceMapper;
    private Remapper remapper;
    
    public MergeAdapter(ClassVisitor cv, MixinInfo mixinInfo, SourceMapper sourceMapper) {
        super(cv);
        this.mixinInfo = mixinInfo;
        this.sourceMapper = sourceMapper;
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Set<String> mergedInterfaces = new HashSet<String>();
        mergedInterfaces.addAll(Arrays.asList(interfaces));
        mergedInterfaces.addAll(mixinInfo.getContributedInterfaces());
        super.visit(version, access, name, signature, superName, mergedInterfaces.toArray(new String[mergedInterfaces.size()]));
        remapper = new SimpleRemapper(mixinInfo.getName(), name);
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
                        for (Iterator<?> it = mixinInfo.getInitInstructions().iterator(); it.hasNext(); ) {
                            AbstractInsnNode insn = (AbstractInsnNode)it.next();
                            if (insn instanceof InsnNode && insn.getOpcode() == Opcodes.RETURN) {
                                mv.visitJumpInsn(Opcodes.GOTO, end);
                            } else {
                                insn.accept(mv);
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
        for (FieldNode field : mixinInfo.getFields()) {
            System.out.println(field.name);
            field.accept(this);
        }
        for (MethodNode mn : mixinInfo.getMethods()) {
            String[] exceptions = new String[mn.exceptions.size()];
            mn.exceptions.toArray(exceptions);
            MethodVisitor mv = cv.visitMethod(mn.access, mn.name, mn.desc, mn.signature, exceptions);
            if (mv != null) {
                mn.instructions.resetLabels();
                mv = sourceMapper.getMethodAdapter(mixinInfo.getSourceInfo(), mv);
                mv = new RemappingMethodAdapter(mn.access, mn.desc, mv, remapper);
                mn.accept(mv);
            }
        }
        super.visitEnd();
    }
}
