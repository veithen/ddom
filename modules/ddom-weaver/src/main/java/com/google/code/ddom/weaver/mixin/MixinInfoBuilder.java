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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.code.ddom.weaver.asm.AbstractAnnotationVisitor;
import com.google.code.ddom.weaver.asm.AbstractClassVisitor;
import com.google.code.ddom.weaver.asm.ErrorHandler;
import com.google.code.ddom.weaver.jsr45.SourceInfoBuilder;
import com.google.code.ddom.weaver.realm.ClassInfo;
import com.google.code.ddom.weaver.realm.ClassRealm;

public class MixinInfoBuilder extends AbstractClassVisitor {
    private static final Logger log = Logger.getLogger(MixinInfo.class.getName());
    
    private final ClassRealm realm;
    private final SourceInfoBuilder sourceInfoBuilder;
    private final ErrorHandler errorHandler;
    private String name;
    private final List<Type> targetTypes = new ArrayList<Type>();
    private final Set<String> contributedInterfaces = new HashSet<String>();
    private final List<FieldNode> fields = new ArrayList<FieldNode>();
    private final List<MethodNode> methods = new ArrayList<MethodNode>();
    private MethodNode init;

    public MixinInfoBuilder(ClassRealm realm, SourceInfoBuilder sourceInfoBuilder, ErrorHandler errorHandler) {
        this.realm = realm;
        this.sourceInfoBuilder = sourceInfoBuilder;
        this.errorHandler = errorHandler;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name;
        // Add all interfaces; we will remove the target interface later
        contributedInterfaces.addAll(Arrays.asList(interfaces));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lcom/google/code/ddom/frontend/Mixin;")) {
            final List<Type> targetTypes = this.targetTypes;
            return new AbstractAnnotationVisitor() {
                @Override
                public AnnotationVisitor visitArray(String name) {
                    return name.equals("value") ? this : null;
                }

                @Override
                public void visit(String name, Object value) {
                    targetTypes.add((Type)value);
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
                return new ConstructorToMethodConverter(init);
            } else {
                log.warning("Encountered non default constructor");
                return null;
            }
        } else {
            if ((access & Opcodes.ACC_ABSTRACT) != 0) {
                errorHandler.handleError("Mixin " + this.name + " declares an abstract method " + name + desc + ". This is disallowed; use an interface instead.");
            }
            MethodNode method = new MethodNode(access, name, desc, signature, exceptions);
            methods.add(method);
            return method;
        }
    }

    @Override
    public void visitEnd() {
        if (targetTypes.isEmpty()) {
            errorHandler.handleError(name + " is missing a @Mixin annotation or the value of the @Mixin annotation is an empty array");
        }
    }

    public MixinInfo build() throws ClassNotFoundException {
        // TODO: do we still need this?
//        if (!contributedInterfaces.remove(target.getInternalName())) {
//            log.warning("Mixin class doesn't implement target interface");
//        }
        List<ClassInfo> targets = new ArrayList<ClassInfo>(targetTypes.size());
        for (Type type : targetTypes) {
            targets.add(realm.getClassInfo(type.getClassName()));
        }
        return new MixinInfo(name, targets, contributedInterfaces, init, fields, methods, sourceInfoBuilder.getSourceInfo());
    }
}