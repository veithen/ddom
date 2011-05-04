/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.weaver.reactor;

import org.objectweb.asm.ClassVisitor;

import com.googlecode.ddom.weaver.mixin.MixinInfoBuilderCollaborator;
import com.googlecode.ddom.weaver.realm.ClassRealm;

public abstract class ReactorPlugin {
    public void init(ClassRealm realm, Extensions extensions) {
    }
    
    public WeavableClassInfoBuilderCollaborator newWeavableClassInfoBuilderCollaborator() {
        return null;
    }
    
    public MixinInfoBuilderCollaborator newMixinInfoBuilderCollaborator() {
        return null;
    }
    
    public void resolve(ClassRealm realm) {
    }
    
    public void processNonWeavableClassInfo(NonWeavableClassInfo classInfo, Class<?> clazz, Extensions extensions, ClassRealm realm) {
    }
    
    public void generateWeavableClasses(ClassRealm realm, WeavableClassInjector weavableClassInjector) {
    }
    
    /**
     * Get the transformation that is to be applied before weaving the given class.
     * 
     * @param classInfo
     *            the class that will be woven
     * @return the transformation that is to be applied, or <code>null</code> if the plugin doesn't
     *         wish to transform the class
     */
    public ClassTransformation getPreTransformation(WeavableClassInfo classInfo) {
        return null;
    }
    
    /**
     * Get the transformation that is to be applied after weaving the given class.
     * 
     * @param classInfo
     *            the class that will be woven
     * @return the transformation that is to be applied, or <code>null</code> if the plugin doesn't
     *         wish to transform the class
     */
    public ClassTransformation getPostTransformation(WeavableClassInfo classInfo) {
        return null;
    }
    
    /**
     * Prepare classes for output. Implementations may override this method to add a
     * {@link org.objectweb.asm.ClassAdapter} in front of the {@link ClassVisitor} that outputs the
     * code to the {@link com.googlecode.ddom.weaver.output.ClassDefinitionProcessor}.
     * 
     * @param realm
     *            TODO
     * @param outputClassVisitor
     *            the class visitor that outputs the classes
     * @param generated
     *            specifies whether the submitted class has been generated
     * @param enhanced
     *            specifies whether any mixins or transformations have been applied to the submitted
     *            class
     * @return a class visitor that wraps the class visitor passed as parameter
     */
    public ClassVisitor prepareForOutput(ClassRealm realm, ClassVisitor outputClassVisitor, boolean generated, boolean enhanced) {
        return outputClassVisitor;
    }
}
