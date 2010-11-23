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
