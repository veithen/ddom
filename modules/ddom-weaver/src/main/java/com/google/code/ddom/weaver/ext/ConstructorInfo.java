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

import org.objectweb.asm.Type;

class ConstructorInfo {
    private final String descriptor;
    private final String signature;
    private final String[] exceptions;
    private Type[] argumentTypes;
    
    ConstructorInfo(String descriptor, String signature, String[] exceptions) {
        this.descriptor = descriptor;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    String getDescriptor() {
        return descriptor;
    }
    
    Type[] getArgumentTypes() {
        if (argumentTypes == null) {
            argumentTypes = Type.getArgumentTypes(descriptor);
        }
        return argumentTypes;
    }

    String getSignature() {
        return signature;
    }

    String[] getExceptions() {
        return exceptions;
    }
}
