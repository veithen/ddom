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

import java.util.List;

class InjectionInfo {
    private final List<InjectableFieldInfo> injectableFields;
    private final boolean hasInjectableInstanceFields;
    private final boolean hasInjectableClassFields;

    InjectionInfo(List<InjectableFieldInfo> injectableFields) {
        this.injectableFields = injectableFields;
        boolean hasInjectableInstanceFields = false;
        boolean hasInjectableClassFields = false;
        for (InjectableFieldInfo injectableField : injectableFields) {
            if (injectableField.isStatic()) {
                hasInjectableClassFields = true;
            } else {
                hasInjectableInstanceFields = true;
            }
        }
        this.hasInjectableInstanceFields = hasInjectableInstanceFields;
        this.hasInjectableClassFields = hasInjectableClassFields;
    }

    boolean hasInjectableInstanceFields() {
        return hasInjectableInstanceFields;
    }

    boolean hasInjectableClassFields() {
        return hasInjectableClassFields;
    }

    List<InjectableFieldInfo> getInjectableFields() {
        return injectableFields;
    }
}
