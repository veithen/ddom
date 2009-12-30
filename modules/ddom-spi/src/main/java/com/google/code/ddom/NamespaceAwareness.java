/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom;

public enum NamespaceAwareness {
    /**
     * Enable namespace aware parsing. If no option is specified, this mode SHOULD be the default.
     * However, it can only be enforced by setting this option explicitly.
     */
    ENABLE,
    
    /**
     * Disable namespace aware parsing.
     */
    DISABLE;
    
    public static NamespaceAwareness get(boolean value) {
        return value ? ENABLE : DISABLE;
    }
}
