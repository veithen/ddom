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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;

public class FieldVisitorTee implements FieldVisitor {
    private final List<FieldVisitor> visitors = new ArrayList<FieldVisitor>();
    
    public FieldVisitorTee(FieldVisitor... visitors) {
        Collections.addAll(this.visitors, visitors);
    }
    
    public void addVisitor(FieldVisitor visitor) {
        visitors.add(visitor);
    }
    
    public static FieldVisitor mergeVisitors(FieldVisitor current, FieldVisitor visitor) {
        if (visitor != null) {
            if (current == null) {
                return visitor;
            } else if (current instanceof FieldVisitorTee) {
                ((FieldVisitorTee)current).addVisitor(visitor);
                return current;
            } else {
                FieldVisitorTee tee = new FieldVisitorTee(current);
                tee.addVisitor(visitor);
                return tee;
            }
        } else {
            return current;
        }
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        AnnotationVisitor result = null;
        for (FieldVisitor visitor : visitors) {
            result = AnnotationVisitorTee.mergeVisitors(result, visitor.visitAnnotation(desc, visible));
        }
        return result;
    }

    public void visitAttribute(Attribute attr) {
        for (FieldVisitor visitor : visitors) {
            visitor.visitAttribute(attr);
        }
    }

    public void visitEnd() {
        for (FieldVisitor visitor : visitors) {
            visitor.visitEnd();
        }
    }
}
