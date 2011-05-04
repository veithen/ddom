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

import com.googlecode.ddom.weaver.asm.AbstractClassVisitor;
import com.googlecode.ddom.weaver.reactor.Extensions;
import com.googlecode.ddom.weaver.reactor.WeavableClassInfo;
import com.googlecode.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.googlecode.ddom.weaver.realm.ClassRealm;

class MappingBuilder extends AbstractClassVisitor implements WeavableClassInfoBuilderCollaborator {
    private final String fromPrefix;
    private final String toPrefix;
    private String fromName;
    private String toName;

    MappingBuilder(String fromPrefix, String toPrefix) {
        this.fromPrefix = fromPrefix;
        this.toPrefix = toPrefix;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (name.startsWith(fromPrefix)) {
            fromName = name;
            toName = toPrefix + name.substring(fromPrefix.length());
        }
    }

    public void process(ClassRealm realm, WeavableClassInfo classInfo, Extensions classInfoExtensions) {
        if (toName != null) {
            realm.get(Mappings.class).addMapping(fromName, toName);
        }
    }
}
