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

import com.google.code.ddom.weaver.mixin.MixinInfoBuilderCollaborator;
import com.google.code.ddom.weaver.realm.ClassRealm;

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
    
    public void generateWeavableClasses(ClassRealm realm, WeavableClassInjector weavableClassInjector) {
    }
}
