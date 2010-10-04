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
package com.google.code.ddom.weaver.realm;

import com.google.code.ddom.weaver.reactor.Extensible;
import com.google.code.ddom.weaver.reactor.Extensions;

public class ClassInfo implements Extensible {
    private final String name;
    private final boolean isInterface;
    private final ClassInfo superclass;
    private final ClassInfo[] interfaces;
    private final Extensions extensions;
    
    public ClassInfo(String name, boolean isInterface, ClassInfo superclass, ClassInfo[] interfaces, Extensions extensions) {
        this.name = name;
        this.isInterface = isInterface;
        this.superclass = superclass;
        this.interfaces = interfaces;
        this.extensions = extensions;
    }
    
    public final String getName() {
        return name;
    }

    public final boolean isInterface() {
        return isInterface;
    }

    public final ClassInfo getSuperclass() {
        return superclass;
    }

    public final ClassInfo[] getInterfaces() {
        return interfaces;
    }

    public final <T> T get(Class<T> key) {
        return extensions.get(key);
    }

    public final boolean isAssignableFrom(ClassInfo classInfo) {
        if (classInfo == this) {
            return true;
        }
        if (classInfo.superclass != null && (classInfo.superclass == this || isAssignableFrom(classInfo.superclass))) {
            return true;
        }
        for (ClassInfo iface : classInfo.interfaces) {
            if (iface == this || isAssignableFrom(iface)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final String toString() {
        return name;
    }
}
