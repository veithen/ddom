package com.google.code.ddom.weaver.asm;

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
