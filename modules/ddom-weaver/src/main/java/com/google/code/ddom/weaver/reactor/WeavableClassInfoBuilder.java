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
package com.google.code.ddom.weaver.reactor;

import java.util.List;

import org.objectweb.asm.Opcodes;

import com.google.code.ddom.weaver.ModelWeaverException;
import com.google.code.ddom.weaver.asm.AbstractClassVisitor;
import com.google.code.ddom.weaver.asm.Util;
import com.google.code.ddom.weaver.realm.ClassInfo;
import com.google.code.ddom.weaver.realm.ClassRealm;

public class WeavableClassInfoBuilder extends AbstractClassVisitor {
    private final ClassRealm realm;
    private final ClassDefinitionSource classDefinitionSource;
    private final List<WeavableClassInfoBuilderCollaborator> collaborators;
    private String name;
    private boolean isInterface;
    private String superName;
    private String[] interfaceNames;
    
    public WeavableClassInfoBuilder(ClassRealm realm, ClassDefinitionSource classDefinitionSource, List<WeavableClassInfoBuilderCollaborator> collaborators) {
        this.realm = realm;
        this.classDefinitionSource = classDefinitionSource;
        this.collaborators = collaborators;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.name = Util.internalNameToClassName(name);
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
        this.superName = superName;
        interfaceNames = interfaces;
    }

    public String getName() {
        return name;
    }

    public WeavableClassInfo build() throws ClassNotFoundException, ModelWeaverException {
        ClassInfo[] interfaces = new ClassInfo[interfaceNames.length];
        for (int i=0; i<interfaces.length; i++) {
            interfaces[i] = realm.getClassInfo(Util.internalNameToClassName(interfaceNames[i]));
        }
        WeavableClassInfo classInfo = new WeavableClassInfo(name, isInterface, realm.getClassInfo(Util.internalNameToClassName(superName)),
                interfaces, classDefinitionSource);
        for (WeavableClassInfoBuilderCollaborator collaborator : collaborators) {
            collaborator.process(classInfo);
        }
        return classInfo;
    }
}
