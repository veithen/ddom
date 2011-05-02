/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.weaver.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.googlecode.ddom.weaver.asm.AbstractAnnotationVisitor;
import com.googlecode.ddom.weaver.asm.AbstractClassVisitor;
import com.googlecode.ddom.weaver.asm.ErrorHandler;
import com.googlecode.ddom.weaver.reactor.Extensions;
import com.googlecode.ddom.weaver.realm.ClassInfo;
import com.googlecode.ddom.weaver.realm.ClassRealm;

public class MixinInfoBuilder extends AbstractClassVisitor {
    private static final Log log = LogFactory.getLog(MixinInfo.class);
    
    private final ErrorHandler errorHandler;
    private final List<MixinInfoBuilderCollaborator> collaborators;
    private String mixinName;
    private final List<Type> targetTypes = new ArrayList<Type>();
    private final Set<String> contributedInterfaces = new HashSet<String>();
    private final List<FieldNode> fields = new ArrayList<FieldNode>();
    private final List<MethodNode> methods = new ArrayList<MethodNode>();
    private MethodNode initMethod;

    public MixinInfoBuilder(ErrorHandler errorHandler, List<MixinInfoBuilderCollaborator> collaborators) {
        this.errorHandler = errorHandler;
        this.collaborators = collaborators;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.mixinName = name;
        // Add all interfaces; we will remove the target interface later
        contributedInterfaces.addAll(Arrays.asList(interfaces));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lcom/googlecode/ddom/frontend/Mixin;")) {
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
                initMethod = new MethodNode(Opcodes.ACC_PRIVATE, "init$$" + mixinName.replace('/', '$'), desc, signature, exceptions);
                return new ConstructorToMethodConverter(initMethod);
            } else {
                log.warn("Encountered non default constructor");
                return null;
            }
        } else {
            if ((access & Opcodes.ACC_ABSTRACT) != 0) {
                errorHandler.handleError("Mixin " + mixinName + " declares an abstract method " + name + desc + ". This is disallowed; use an interface instead.");
            }
            MethodNode method = new MethodNode(access, name, desc, signature, exceptions);
            methods.add(method);
            return method;
        }
    }

    @Override
    public void visitEnd() {
        if (targetTypes.isEmpty()) {
            errorHandler.handleError(mixinName + " is missing a @Mixin annotation or the value of the @Mixin annotation is an empty array");
        }
    }

    public MixinInfo build(ClassRealm realm) {
        // TODO: do we still need this?
//        if (!contributedInterfaces.remove(target.getInternalName())) {
//            log.warning("Mixin class doesn't implement target interface");
//        }
        List<ClassInfo> targets = new ArrayList<ClassInfo>(targetTypes.size());
        for (Type type : targetTypes) {
            targets.add(realm.getClassInfo(type.getClassName()));
        }
        Extensions extensions = new Extensions();
        boolean emptyInitMethod = true;
        for (Iterator<?> it = initMethod.instructions.iterator(); it.hasNext(); ) {
            int opcode = ((AbstractInsnNode)it.next()).getOpcode();
            if (opcode != -1 && opcode != Opcodes.RETURN) {
                emptyInitMethod = false;
                break;
            }
        }
        MixinInfo mixinInfo = new MixinInfo(mixinName, targets, contributedInterfaces, emptyInitMethod ? null : initMethod, fields, methods, extensions);
        for (MixinInfoBuilderCollaborator collaborator : collaborators) {
            collaborator.process(realm, mixinInfo, extensions);
        }
        return mixinInfo;
    }
}