package com.google.code.ddom.weaver.asm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class MergeAdapter extends ClassAdapter {
    private final MixinInfo mixinInfo;
    private String cname;
    
    public MergeAdapter(ClassVisitor cv, MixinInfo mixinInfo) {
        super(cv);
        this.mixinInfo = mixinInfo;
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        Set<String> mergedInterfaces = new HashSet<String>();
        mergedInterfaces.addAll(Arrays.asList(interfaces));
        mergedInterfaces.addAll(mixinInfo.getContributedInterfaces());
        super.visit(version, access, name, signature, superName, mergedInterfaces.toArray(new String[mergedInterfaces.size()]));
        this.cname = name;
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
            mn.instructions.resetLabels();
            mn.accept(new RemappingMethodAdapter(mn.access, mn.desc, mv, new SimpleRemapper(mixinInfo.getName(), cname)));
        }
        super.visitEnd();
    }
}
