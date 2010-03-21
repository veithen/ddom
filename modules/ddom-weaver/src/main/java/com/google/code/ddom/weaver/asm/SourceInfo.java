package com.google.code.ddom.weaver.asm;

public class SourceInfo {
    private final String absoluteSourceFile;
    private final int maxLine;
    
    public SourceInfo(String absoluteSourceFile, int maxLine) {
        this.absoluteSourceFile = absoluteSourceFile;
        this.maxLine = maxLine;
    }
    
    public String getSourceFile() {
        return absoluteSourceFile.substring(absoluteSourceFile.lastIndexOf('/') + 1);
    }
    
    public String getAbsoluteSourceFile() {
        return absoluteSourceFile;
    }
    
    public int getMaxLine() {
        return maxLine;
    }
}
