/*
 * Copyright 2009 Andreas Veithen
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

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * Class adapter that extracts information about source code references. In particular, it
 * calculates the maximum line number.
 */
public class SourceInfoBuilder extends ClassAdapter {
    private String name;
    private String source;
    private String debug;
    int maxLine;
    private SourceInfo sourceInfo;
    
    public SourceInfoBuilder(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.name = name;
    }

    @Override
    public void visitSource(String source, String debug) {
        super.visitSource(source, debug);
        this.source = source;
        this.debug = debug;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv == null) {
            mv = new EmptyVisitor();
        }
        return new MethodAdapter(mv) {

            @Override
            public void visitLineNumber(int line, Label start) {
                super.visitLineNumber(line, start);
                if (line > maxLine) {
                    maxLine = line;
                }
            }
        };
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        sourceInfo = new SourceInfo(name.substring(0, name.lastIndexOf('/')+1) + source, maxLine);
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
