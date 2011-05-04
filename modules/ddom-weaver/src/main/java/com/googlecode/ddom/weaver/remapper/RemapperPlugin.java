/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.weaver.remapper;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.RemappingClassAdapter;

import com.googlecode.ddom.weaver.reactor.Extensions;
import com.googlecode.ddom.weaver.reactor.ReactorPlugin;
import com.googlecode.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.googlecode.ddom.weaver.realm.ClassRealm;

/**
 * Reactor plugin that allows to remap woven classes to an alternate Java package.
 * 
 * @author Andreas Veithen
 */
public class RemapperPlugin extends ReactorPlugin {
    private final String fromPrefix;
    private final String toPrefix;
    
    public RemapperPlugin(String fromPackage, String toPackage) {
        fromPrefix = fromPackage.replace('.', '/') + '/';
        toPrefix = toPackage.replace('.', '/') + '/';
    }

    @Override
    public void init(ClassRealm realm, Extensions extensions) {
        extensions.set(new Mappings());
    }

    @Override
    public WeavableClassInfoBuilderCollaborator newWeavableClassInfoBuilderCollaborator() {
        return new MappingBuilder(fromPrefix, toPrefix);
    }

    @Override
    public ClassVisitor prepareForOutput(ClassRealm realm, ClassVisitor outputClassVisitor, boolean generated, boolean enhanced) {
        return new RemappingClassAdapter(outputClassVisitor, realm.get(Mappings.class));
    }
}
