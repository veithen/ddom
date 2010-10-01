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
package com.google.code.ddom.weaver.ext;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.google.code.ddom.weaver.asm.Util;
import com.google.code.ddom.weaver.reactor.GeneratedClass;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.realm.ClassInfo;

class ModelExtensionClass extends GeneratedClass {
    private final WeavableClassInfo implementation;
    private final ClassInfo rootInterface;
    private final ClassInfo extensionInterface;

    ModelExtensionClass(WeavableClassInfo implementation, ClassInfo rootInterface, ClassInfo extensionInterface) {
        this.implementation = implementation;
        this.rootInterface = rootInterface;
        this.extensionInterface = extensionInterface;
    }
    
    private String getModelExtensionClassName(ClassInfo extensionInterface) {
        return implementation.getName() + "__" + extensionInterface.getName().replace('.', '_');
    }
    
    public void accept(ClassVisitor classVisitor) {
        ClassInfo superInterface = extensionInterface.getInterfaces()[0];
        classVisitor.visit(
                Opcodes.V1_5,
                Opcodes.ACC_PUBLIC,
                Util.classNameToInternalName(getModelExtensionClassName(extensionInterface)),
                null,
                Util.classNameToInternalName(superInterface == rootInterface ? implementation.getName() : getModelExtensionClassName(superInterface)),
                new String[] { Util.classNameToInternalName(extensionInterface.getName()) });
        
        classVisitor.visitEnd();
    }
}
