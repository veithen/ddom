package com.google.code.ddom.weaver.asm;

import java.util.Iterator;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class MergeAdapter extends ClassAdapter {
    private ClassNode cn;
    private String cname;
    
    public MergeAdapter(ClassVisitor cv, ClassNode cn) {
        super(cv);
        this.cn = cn;
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.cname = name;
    }
    
    @Override
    public void visitEnd() {
        for (Iterator it = cn.fields.iterator(); it.hasNext(); ) {
            FieldNode field = (FieldNode)it.next();
            System.out.println(field.name);
            field.accept(this);
        }
        for (Iterator it = cn.methods.iterator(); it.hasNext();) {
            MethodNode mn = (MethodNode)it.next();
            // TODO: need to process constructors somehow (or throw an exception when non empty constructors are encountered)
            if (!mn.name.equals("<init>")) {
                String[] exceptions = new String[mn.exceptions.size()];
                mn.exceptions.toArray(exceptions);
                MethodVisitor mv = cv.visitMethod(mn.access, mn.name, mn.desc, mn.signature, exceptions);
                mn.instructions.resetLabels();
                mn.accept(new RemappingMethodAdapter(mn.access, mn.desc, mv, new SimpleRemapper(cname, cn.name)));
            }
        }
        super.visitEnd();
    }
}
