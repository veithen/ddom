package com.google.code.ddom.weaver.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class MixinInfo implements ClassVisitor {
    private static final Logger log = Logger.getLogger(MixinInfo.class.getName());
    
    Type target;
    private final Set<String> contributedInterfaces = new HashSet<String>();
    private String name;
    private MethodNode init;
    private final List<FieldNode> fields = new ArrayList<FieldNode>();
    private final List<MethodNode> methods = new ArrayList<MethodNode>();
    
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name;
        // Add all interfaces; we will remove the target interface later
        contributedInterfaces.addAll(Arrays.asList(interfaces));
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lcom/google/code/ddom/weaver/asm/Mixin;")) {
            return new AnnotationVisitor() {
                public void visit(String name, Object value) {
                    if (name.equals("value")) {
                        target = (Type)value;
                    }
                }

                public AnnotationVisitor visitAnnotation(String name, String desc) {
                    return null;
                }

                public AnnotationVisitor visitArray(String name) {
                    return null;
                }
                
                public void visitEnum(String name, String desc, String value) {
                }

                public void visitEnd() {
                }
            };
        } else {
            return null;
        }
    }

    public void visitAttribute(Attribute attr) {
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        FieldNode field = new FieldNode(access, name, desc, signature, value);
        fields.add(field);
        return field;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (name.equals("<init>")) {
            if (desc.equals("()V")) {
                init = new MethodNode(access, name, desc, signature, exceptions);
                return init;
            } else {
                log.warning("Encountered non default constructor");
                return null;
            }
        } else {
            MethodNode method = new MethodNode(access, name, desc, signature, exceptions);
            methods.add(method);
            return method;
        }
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public void visitSource(String source, String debug) {
    }

    public void visitEnd() {
        if (!contributedInterfaces.remove(target.getInternalName())) {
            log.warning("Mixin class doesn't implement target interface");
        }
        System.out.println("name = " + name);
        System.out.println("target = " + target.getInternalName());
        System.out.println("contributedInterfaces = " + contributedInterfaces);
    }

    public String getName() {
        return name;
    }

    public List<FieldNode> getFields() {
        return fields;
    }

    public List<MethodNode> getMethods() {
        return methods;
    }

    public Set<String> getContributedInterfaces() {
        return contributedInterfaces;
    }
}
