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
package com.google.code.ddom.weaver.implementation;

import org.objectweb.asm.AnnotationVisitor;

import com.google.code.ddom.weaver.ModelWeaverException;
import com.google.code.ddom.weaver.asm.AbstractClassVisitor;
import com.google.code.ddom.weaver.reactor.WeavableClassInfo;
import com.google.code.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;

class ImplementationAnnotationExtractor extends AbstractClassVisitor implements WeavableClassInfoBuilderCollaborator {
    private final ImplementationMap implementationMap;
    private boolean isImplementation;

    ImplementationAnnotationExtractor(ImplementationMap implementationMap) {
        this.implementationMap = implementationMap;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc.equals("Lcom/google/code/ddom/backend/Implementation;")) {
            isImplementation = true;
        }
        // No need to actually get the details of the annotation => always return null
        return null;
    }

    public void process(WeavableClassInfo classInfo) throws ModelWeaverException {
        classInfo.set(new ImplementationInfo(isImplementation));
        if (isImplementation) {
            implementationMap.addImplementation(classInfo);
        }
    }
}
