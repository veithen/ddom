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
package com.google.code.ddom.weaver.inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import com.google.code.ddom.weaver.asm.AbstractClassVisitor;
import com.google.code.ddom.weaver.asm.AbstractFieldVisitor;
import com.google.code.ddom.weaver.reactor.Extensions;
import com.google.code.ddom.weaver.reactor.ReactorException;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;
import com.google.code.ddom.weaver.realm.ClassRealm;

class InjectionInfoBuilder extends AbstractClassVisitor implements WeavableClassInfoBuilderCollaborator {
    private final Map<String,Injector> bindings;
    private List<InjectableFieldInfo> fieldInfos;

    public InjectionInfoBuilder(Map<String,Injector> bindings) {
        this.bindings = bindings;
    }

    @Override
    public FieldVisitor visitField(int access, final String fieldName, final String fieldDesc, String signature, Object value) {
        return new AbstractFieldVisitor() {
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                if (desc.equals("Lcom/google/code/ddom/backend/Inject;")) {
                    String fieldType = Type.getType(fieldDesc).getClassName();
                    if (!bindings.containsKey(fieldType)) {
                        throw new ReactorException("Don't know how to inject a field with type " + fieldType);
                    }
                    Injector injector = bindings.get(fieldType);
                    // A null value means ignoring the @Inject annotation and inject nothing
                    if (injector != null) {
                        if (fieldInfos == null) {
                            fieldInfos = new ArrayList<InjectableFieldInfo>();
                        }
                        fieldInfos.add(new InjectableFieldInfo(fieldName, fieldDesc, injector));
                    }
                }
                return null;
            }
        };
    }

    public void process(ClassRealm realm, WeavableClassInfo classInfo, Extensions classInfoExtensions) {
        InjectionInfo injectionInfo;
        if (fieldInfos == null) {
            injectionInfo = null;
        } else {
            injectionInfo = new InjectionInfo(fieldInfos);
        }
        classInfoExtensions.set(InjectionInfo.class, injectionInfo);
    }
}
