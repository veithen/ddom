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

import com.google.code.ddom.weaver.reactor.PropertySupport;

// TODO: not sure if ClassInfo or WeavableClassInfo should inherit from PropertySupport
public class ClassInfo extends PropertySupport {
    private final String name;
    private final boolean isInterface;
    private final ClassInfo superclass;
    private final ClassInfo[] interfaces;
    
    public ClassInfo(String name, boolean isInterface, ClassInfo superclass, ClassInfo[] interfaces) {
        this.name = name;
        this.isInterface = isInterface;
        this.superclass = superclass;
        this.interfaces = interfaces;
    }
    
    public String getName() {
        return name;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public ClassInfo getSuperclass() {
        return superclass;
    }

    public ClassInfo[] getInterfaces() {
        return interfaces;
    }

    public boolean isAssignableFrom(ClassInfo classInfo) {
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
    public String toString() {
        return name;
    }
}
