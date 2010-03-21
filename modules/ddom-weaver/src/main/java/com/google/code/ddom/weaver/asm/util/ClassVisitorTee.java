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
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassVisitorTee implements ClassVisitor {
    private final List<ClassVisitor> visitors = new ArrayList<ClassVisitor>();
    
    public ClassVisitorTee(ClassVisitor... visitors) {
        Collections.addAll(this.visitors, visitors);
    }

    public void addVisitor(ClassVisitor visitor) {
        visitors.add(visitor);
    }
    
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        for (ClassVisitor visitor : visitors) {
            visitor.visit(version, access, name, signature, superName, interfaces);
        }
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor result = null;
        for (ClassVisitor visitor : visitors) {
            result = AnnotationVisitorTee.mergeVisitors(result, visitor.visitAnnotation(desc, visible));
        }
        return result;
    }

    public void visitSource(String source, String debug) {
        for (ClassVisitor visitor : visitors) {
            visitor.visitSource(source, debug);
        }
    }

    public void visitAttribute(Attribute attr) {
        for (ClassVisitor visitor : visitors) {
            visitor.visitAttribute(attr);
        }
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        FieldVisitor result = null;
        for (ClassVisitor visitor : visitors) {
            result = FieldVisitorTee.mergeVisitors(result, visitor.visitField(access, name, desc, signature, value));
        }
        return result;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        for (ClassVisitor visitor : visitors) {
            visitor.visitInnerClass(name, outerName, innerName, access);
        }
    }

    public void visitOuterClass(String owner, String name, String desc) {
        for (ClassVisitor visitor : visitors) {
            visitor.visitOuterClass(owner, name, desc);
        }
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor result = null;
        for (ClassVisitor visitor : visitors) {
            result = MethodVisitorTee.mergeVisitors(result, visitor.visitMethod(access, name, desc, signature, exceptions));
        }
        return result;
    }

    public void visitEnd() {
        for (ClassVisitor visitor : visitors) {
            visitor.visitEnd();
        }
    }
}
