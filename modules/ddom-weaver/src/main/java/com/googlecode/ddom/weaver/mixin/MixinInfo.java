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
package com.googlecode.ddom.weaver.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.googlecode.ddom.weaver.reactor.Extensible;
import com.googlecode.ddom.weaver.reactor.Extensions;
import com.googlecode.ddom.weaver.realm.ClassInfo;

public class MixinInfo implements Extensible {
    private final String name;
    private final List<ClassInfo> targets;
    private final Set<String> contributedInterfaces;
    private final MethodNode initMethod;
    private final List<FieldNode> fields;
    private final List<MethodNode> methods;
    private final Extensions extensions;
    
    public MixinInfo(String name, List<ClassInfo> targets, Set<String> contributedInterfaces,
            MethodNode initMethod, List<FieldNode> fields, List<MethodNode> methods, Extensions extensions) {
        this.name = name;
        this.targets = targets;
        this.contributedInterfaces = contributedInterfaces;
        this.initMethod = initMethod;
        this.fields = fields;
        this.methods = methods;
        this.extensions = extensions;
    }

    public <T> T get(Class<T> key) {
        return extensions.get(key);
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
    
    public MethodNode getInitMethod() {
        return initMethod;
    }

    @Override
    public String toString() {
        return name;
    }
}
