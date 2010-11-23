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
package com.googlecode.ddom.weaver.compound;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import com.google.code.ddom.commons.cl.ClassRef;
import com.googlecode.ddom.weaver.reactor.GeneratedClass;

/**
 * Generates a class whose methods delegate to instances of a set of classes implementing the same
 * interface as the compound class. This is typically used to invoke a set of event listeners
 * without resorting to a collection or array. The following restrictions apply:
 * <ul>
 * <li>All methods defined by the interface must have <tt>void</tt> as return type.
 * <li>All component classes must have public default constructors.
 * </ul>
 * 
 * @author Andreas Veithen
 */
public class CompoundClass extends GeneratedClass {
    private final String className;
    private final String ifaceName;
    private final byte[] ifaceDefinition;
    private final String[] componentClasses;

    public CompoundClass(String className, ClassRef iface, String[] componentClasses) throws ClassNotFoundException {
        this.className = className;
        ifaceName = iface.getClassName();
        ifaceDefinition = iface.getClassDefinition();
        this.componentClasses = componentClasses;
    }

    public void accept(ClassVisitor classVisitor) {
        new ClassReader(ifaceDefinition).accept(new CompoundClassGenerator(className, ifaceName, componentClasses, classVisitor), 0);
    }
}
