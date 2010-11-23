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
package com.googlecode.ddom.weaver.jsr45;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

/**
 * Implements JSR-45 source mapping.
 */
// TODO: In addition to JSR-45 we should support some convention like AspectJ [1] that enables the developer to easily interpret stack traces on environments that don't support JSR-45
//       [1] http://www.eclipse.org/aspectj/doc/released/devguide/ajc-ref.html
public class SourceMapper {
    static class Mapping {
        SourceInfo sourceInfo;
        int lineOffset;
    }
    
    private final List<Mapping> mappings = new ArrayList<Mapping>();
    private int nextLineOffset;
    
    public void addSourceInfo(SourceInfo sourceInfo) {
        Mapping mapping = new Mapping();
        mapping.sourceInfo = sourceInfo;
        mapping.lineOffset = nextLineOffset;
        mappings.add(mapping);
        nextLineOffset += sourceInfo.getMaxLine();
    }

    /**
     * Create a class adapter that adds the SMAP.
     * 
     * @param cv
     * @return
     */
    public ClassAdapter getClassAdapter(ClassVisitor cv) {
        final Mapping[] mappings = this.mappings.toArray(new Mapping[this.mappings.size()]);
        return new ClassAdapter(cv) {
            @Override
            public void visitSource(String source, String debug) {
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
            }
        };
    }
    
    public MethodAdapter getMethodAdapter(SourceInfo sourceInfo, MethodVisitor mv) {
        Mapping mapping = null;
        for (Mapping candidate : mappings) {
            if (candidate.sourceInfo == sourceInfo) {
                mapping = candidate;
                break;
            }
        }
        if (mapping == null) {
            throw new IllegalArgumentException();
        }
        final int lineOffset = mapping.lineOffset;
        return new MethodAdapter(mv) {
            @Override
            public void visitLineNumber(int line, Label start) {
                super.visitLineNumber(line + lineOffset, start);
            }
        };
    }
}
