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
package com.google.code.ddom.weaver.reactor;

import com.google.code.ddom.weaver.jsr45.SourceInfo;

public class WeavableClassInfo extends ClassInfo {
    private final ClassDefinitionSource classDefinitionSource;
    private final SourceInfo sourceInfo;
    private final boolean isImplementation;
    
    public WeavableClassInfo(String name, boolean isInterface, ClassInfo superclass, ClassInfo[] interfaces, ClassDefinitionSource classDefinitionSource, SourceInfo sourceInfo, boolean isImplementation) {
        super(name, isInterface, superclass, interfaces);
        this.classDefinitionSource = classDefinitionSource;
        this.sourceInfo = sourceInfo;
        this.isImplementation = isImplementation;
    }

    public ClassDefinitionSource getClassDefinitionSource() {
        return classDefinitionSource;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
    
    public boolean isImplementation() {
        return isImplementation;
    }
}
