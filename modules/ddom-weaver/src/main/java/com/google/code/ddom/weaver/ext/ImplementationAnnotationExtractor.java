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

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.google.code.ddom.weaver.asm.AbstractAnnotationVisitor;
import com.google.code.ddom.weaver.asm.AbstractClassVisitor;
import com.google.code.ddom.weaver.reactor.Extensions;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.google.code.ddom.weaver.realm.ClassRealm;

class ImplementationAnnotationExtractor extends AbstractClassVisitor implements WeavableClassInfoBuilderCollaborator {
    private boolean isImplementation;
    String factoryInterfaceName;
    private List<ConstructorInfo> constructors;
    
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lcom/google/code/ddom/backend/Implementation;")) {
            isImplementation = true;
            constructors = new ArrayList<ConstructorInfo>();
            return new AbstractAnnotationVisitor() {
                @Override
                public void visit(String name, Object value) {
                    factoryInterfaceName = ((Type)value).getClassName();
                }
            };
        } else {
            return null;
        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (isImplementation && name.equals("<init>") && access == Opcodes.ACC_PUBLIC) {
            constructors.add(new ConstructorInfo(desc, signature, exceptions));
        }
        return null;
    }

    public void process(ClassRealm realm, WeavableClassInfo classInfo, Extensions classInfoExtensions) {
        if (isImplementation) {
            classInfoExtensions.set(new ImplementationInfo(classInfo, realm.getClassInfo(factoryInterfaceName), constructors));
            realm.get(ModelExtensionGenerator.class).addImplementation(classInfo);
        } else {
            classInfoExtensions.set(ImplementationInfo.class, null);
        }
    }
}
