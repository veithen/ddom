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

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;

public abstract class GeneratedClass implements ClassDefinitionSource {
    public final byte[] getClassDefinition(Reactor reactor) {
        ClassWriter cw = new ReactorAwareClassWriter(reactor, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        // TODO: the verifier should be optional
        accept(new CheckClassAdapter(cw));
        return cw.toByteArray();
    }
}
