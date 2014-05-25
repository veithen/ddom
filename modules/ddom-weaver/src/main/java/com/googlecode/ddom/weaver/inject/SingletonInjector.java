/*
 * Copyright 2014 Andreas Veithen
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
package com.googlecode.ddom.weaver.inject;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.googlecode.ddom.weaver.asm.Util;

/**
 * Injector that injects a singleton. The singleton class must have a static final field
 * <code>INSTANCE</code> of the appropriate type.
 * 
 * @author Andreas Veithen
 */
public class SingletonInjector implements Injector {
    private final String singletonClassName;
    private final String fieldDesc;
    
    /**
     * Constructor.
     * 
     * @param singletonClassName
     *            the singleton class to inject (i.e. the class having the static final
     *            <code>INSTANCE</code> field)
     * @param type
     *            the singleton type (i.e. the type of the <code>INSTANCE</code> field
     */
    public SingletonInjector(String singletonClassName, String type) {
        this.singletonClassName = Util.classNameToInternalName(singletonClassName);
        fieldDesc = "L" + Util.classNameToInternalName(type) + ";";
    }

    public void generateFactoryMethodCode(MethodVisitor mv) {
        mv.visitCode();
        mv.visitFieldInsn(Opcodes.GETSTATIC, singletonClassName, "INSTANCE", fieldDesc);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(1, 0);
        mv.visitEnd();
    }
}
