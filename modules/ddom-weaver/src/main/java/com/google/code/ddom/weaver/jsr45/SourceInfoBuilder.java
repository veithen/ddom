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
package com.google.code.ddom.weaver.jsr45;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.google.code.ddom.weaver.asm.AbstractClassVisitor;
import com.google.code.ddom.weaver.asm.AbstractMethodVisitor;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.google.code.ddom.weaver.realm.ClassRealm;

/**
 * Class visitor that extracts information about source code references. In particular, it
 * calculates the maximum line number.
 */
public class SourceInfoBuilder extends AbstractClassVisitor implements WeavableClassInfoBuilderCollaborator {
    private String name;
    private String source;
    private String debug;
    int maxLine;
    private SourceInfo sourceInfo;
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = name;
    }

    @Override
    public void visitSource(String source, String debug) {
        this.source = source;
        this.debug = debug;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new AbstractMethodVisitor() {
            @Override
            public void visitLineNumber(int line, Label start) {
                if (line > maxLine) {
                    maxLine = line;
                }
            }
        };
    }

    @Override
    public void visitEnd() {
        sourceInfo = new SourceInfo(name.substring(0, name.lastIndexOf('/')+1) + source, maxLine);
    }

    // TODO: this should eventually disappear
    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public void process(ClassRealm realm, WeavableClassInfo classInfo) {
        classInfo.set(sourceInfo);
    }
}
