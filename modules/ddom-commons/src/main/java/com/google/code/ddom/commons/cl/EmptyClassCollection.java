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
package com.google.code.ddom.commons.cl;

import java.util.Collection;
import java.util.Collections;

public final class EmptyClassCollection implements ClassCollection {
    public static final EmptyClassCollection INSTANCE = new EmptyClassCollection();
    
    private EmptyClassCollection() {}
    
    public Collection<ClassRef> getClassRefs() {
        return Collections.emptySet();
    }

    public Collection<Class<?>> getClasses() {
        return Collections.emptySet();
    }

    public boolean isInPackage(String pkg) {
        return true;
    }

    public String getRootPackageName() {
        return null;
    }
}
