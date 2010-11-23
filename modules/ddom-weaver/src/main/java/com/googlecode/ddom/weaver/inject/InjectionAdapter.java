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
package com.googlecode.ddom.weaver.inject;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.googlecode.ddom.weaver.mixin.ConstructorEnhancer;

class InjectionAdapter extends ClassAdapter {
    private final InjectionInfo injectionInfo;
    private String className;
    
    public InjectionAdapter(ClassVisitor cv, InjectionInfo injectionInfo) {
        super(cv);
        this.injectionInfo = injectionInfo;
    }
    
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && name.equals("<init>")) {
            List<String> injectorMethodNames = new ArrayList<String>();
            for (InjectableFieldInfo fieldInfo : injectionInfo.getInjectableFields()) {
                injectorMethodNames.add(fieldInfo.getInjectorMethodName());
            }
            mv = new ConstructorEnhancer(mv, className, injectorMethodNames);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        for (InjectableFieldInfo fieldInfo : injectionInfo.getInjectableFields()) {
            MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PRIVATE, fieldInfo.getInjectorMethodName(), "()V", null, new String[0]);
            if (mv != null) {
                fieldInfo.getInjector().generateInjectorMethod(className, fieldInfo.getFieldName(), fieldInfo.getFieldDesc(), mv);
            }
        }
        super.visitEnd();
    }
}
