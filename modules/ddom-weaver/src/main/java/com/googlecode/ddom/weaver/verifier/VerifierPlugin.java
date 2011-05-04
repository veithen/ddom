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
package com.googlecode.ddom.weaver.verifier;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.util.CheckClassAdapter;

import com.googlecode.ddom.weaver.reactor.ReactorPlugin;
import com.googlecode.ddom.weaver.realm.ClassRealm;

public class VerifierPlugin extends ReactorPlugin {
    public static final VerifierPlugin INSTANCE = new VerifierPlugin();
    
    private VerifierPlugin() {}
    
    @Override
    public ClassVisitor prepareForOutput(ClassRealm realm, ClassVisitor outputClassVisitor, boolean generated, boolean enhanced) {
        if (generated || enhanced) {
            return new CheckClassAdapter(outputClassVisitor);
        } else {
            return outputClassVisitor;
        }
    }
}
