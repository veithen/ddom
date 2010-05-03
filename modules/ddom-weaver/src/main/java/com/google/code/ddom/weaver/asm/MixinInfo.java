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

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class MixinInfo {
    private final String name;
    private final List<ClassInfo> targets;
    private final Set<String> contributedInterfaces;
    private final MethodNode init;
    private final List<FieldNode> fields;
    private final List<MethodNode> methods;
    private final SourceInfo sourceInfo;
    
    public MixinInfo(String name, List<ClassInfo> targets, Set<String> contributedInterfaces,
            MethodNode init, List<FieldNode> fields, List<MethodNode> methods, SourceInfo sourceInfo) {
        this.name = name;
        this.targets = targets;
        this.contributedInterfaces = contributedInterfaces;
        this.init = init;
        this.fields = fields;
        this.methods = methods;
        this.sourceInfo = sourceInfo;
    }

    public String getName() {
        return name;
    }

    public List<ClassInfo> getTargets() {
        return targets;
    }

    public List<FieldNode> getFields() {
        return fields;
    }

    public List<MethodNode> getMethods() {
        return methods;
    }

    public Set<String> getContributedInterfaces() {
        return contributedInterfaces;
    }

    public InsnList getInitInstructions() {
        return init.instructions;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    @Override
    public String toString() {
        return name;
    }
}
