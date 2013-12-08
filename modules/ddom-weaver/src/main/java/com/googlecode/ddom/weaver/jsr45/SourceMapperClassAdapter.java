/*
 * Copyright 2009-2010,2013 Andreas Veithen
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
package com.googlecode.ddom.weaver.jsr45;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

final class SourceMapperClassAdapter extends ClassAdapter {
    private static final Log log = LogFactory.getLog(SourceMapperClassAdapter.class);
    
    private final Mapping[] mappings;
    private boolean sourceWritten;

    SourceMapperClassAdapter(ClassVisitor cv, Mapping[] mappings) {
        super(cv);
        this.mappings = mappings;
    }

    private void writeSourceIfNecessary() {
        if (!sourceWritten) {
            visitSource("generated", null);
        }
    }
    
    @Override
    public void visitSource(String source, String debug) {
        if (log.isDebugEnabled()) {
            log.debug("Writing source map for the following mappings: " + Arrays.asList(mappings));
        }
        StringBuilder smap = new StringBuilder();
        smap.append("SMAP\n");
        smap.append(source);
        smap.append("\nWeaver\n*S Weaver\n*F\n");
        for (int i = 0; i<mappings.length; i++) {
            Mapping mapping = mappings[i];
            // TODO: we should only use absolute file names if necessary
            smap.append("+ ");
            smap.append(i+1);
            smap.append(' ');
            smap.append(mapping.sourceInfo.getSourceFile());
            smap.append('\n');
            smap.append(mapping.sourceInfo.getAbsoluteSourceFile());
            smap.append('\n');
        }
        smap.append("*L\n");
        for (int i = 0; i<mappings.length; i++) {
            Mapping mapping = mappings[i];
            smap.append("1#");
            smap.append(i+1);
            smap.append(',');
            smap.append(mapping.sourceInfo.getMaxLine());
            smap.append(':');
            smap.append(mapping.lineOffset+1);
            smap.append('\n');
        }
        smap.append("*E\n");
        super.visitSource(source, smap.toString());
        sourceWritten = true;
    }

    public void visitOuterClass(String owner, String name, String desc) {
        writeSourceIfNecessary();
        super.visitOuterClass(owner, name, desc);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        writeSourceIfNecessary();
        return super.visitAnnotation(desc, visible);
    }

    public void visitAttribute(Attribute attr) {
        writeSourceIfNecessary();
        super.visitAttribute(attr);
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        writeSourceIfNecessary();
        super.visitInnerClass(name, outerName, innerName, access);
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        writeSourceIfNecessary();
        return super.visitField(access, name, desc, signature, value);
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        writeSourceIfNecessary();
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}
