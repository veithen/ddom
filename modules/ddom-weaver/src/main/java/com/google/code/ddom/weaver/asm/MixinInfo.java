package com.google.code.ddom.weaver.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.code.ddom.weaver.asm.util.AbstractAnnotationVisitor;
import com.google.code.ddom.weaver.asm.util.AbstractClassVisitor;
import com.google.code.ddom.weaver.asm.util.ClassVisitorTee;

public class MixinInfo extends ClassVisitorTee {
    static final Logger log = Logger.getLogger(MixinInfo.class.getName());
    
    class VisitorImpl extends AbstractClassVisitor {
        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            MixinInfo.this.name = name;
            // Add all interfaces; we will remove the target interface later
            contributedInterfaces.addAll(Arrays.asList(interfaces));
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (desc.equals("Lcom/google/code/ddom/weaver/asm/Mixin;")) {
                return new AbstractAnnotationVisitor() {
                    @Override
                    public void visit(String name, Object value) {
                        if (name.equals("value")) {
                            target = (Type)value;
                        }
                    }
                };
            } else {
                return null;
            }
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            FieldNode field = new FieldNode(access, name, desc, signature, value);
            fields.add(field);
            return field;
        }

        @Override
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

        @Override
        public void visitEnd() {
            if (!contributedInterfaces.remove(target.getInternalName())) {
                log.warning("Mixin class doesn't implement target interface");
            }
            System.out.println("name = " + name);
            System.out.println("target = " + target.getInternalName());
            System.out.println("contributedInterfaces = " + contributedInterfaces);
        }
    }
    
    private final SourceInfoBuilder sourceInfoBuilder = new SourceInfoBuilder();
    private final VisitorImpl visitorImpl = new VisitorImpl();
    Type target;
    final Set<String> contributedInterfaces = new HashSet<String>();
    String name;
    MethodNode init;
    final List<FieldNode> fields = new ArrayList<FieldNode>();
    final List<MethodNode> methods = new ArrayList<MethodNode>();
    
    public MixinInfo() {
        addVisitor(sourceInfoBuilder);
        addVisitor(visitorImpl);
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

    public SourceInfo getSourceInfo() {
        return sourceInfoBuilder.getSourceInfo();
    }
}
