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
package com.googlecode.ddom.weaver.reactor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.googlecode.ddom.weaver.asm.Util;
import com.googlecode.ddom.weaver.realm.ClassInfo;

public class ReactorAwareClassWriter extends ClassWriter {
    private final Reactor reactor;
    private String className;
    
    public ReactorAwareClassWriter(Reactor reactor, ClassReader classReader, int flags) {
        super(classReader, flags);
        this.reactor = reactor;
    }

    public ReactorAwareClassWriter(Reactor reactor, int flags) {
        super(flags);
        this.reactor = reactor;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        ClassInfo c = reactor.getClassInfo(Util.internalNameToClassName(type1));
        ClassInfo d = reactor.getClassInfo(Util.internalNameToClassName(type2));
        if (c.isAssignableFrom(d)) {
            return type1;
        }
        if (d.isAssignableFrom(c)) {
            return type2;
        }
        if (c.isInterface() || d.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                c = c.getSuperclass();
            } while (!c.isAssignableFrom(d));
            return Util.classNameToInternalName(c.getName());
        }
    }
}
