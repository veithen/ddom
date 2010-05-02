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
package com.google.code.ddom.weaver.asm;

import com.google.code.ddom.weaver.asm.util.AbstractClassVisitor;

public class WeavableClassInfoBuilder extends AbstractClassVisitor {
    private final Reactor reactor;
    private final SourceInfoBuilder sourceInfoBuilder;
    private String superName;
    private String[] interfaceNames;
    
    public WeavableClassInfoBuilder(Reactor reactor, SourceInfoBuilder sourceInfoBuilder) {
        this.reactor = reactor;
        this.sourceInfoBuilder = sourceInfoBuilder;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.superName = superName;
        interfaceNames = interfaces;
    }

    public WeavableClassInfo build() throws ClassNotFoundException {
        ClassInfo[] interfaces = new ClassInfo[interfaceNames.length];
        for (int i=0; i<interfaces.length; i++) {
            interfaces[i] = reactor.getClassInfo(Util.internalNameToClassName(interfaceNames[i]));
        }
        return new WeavableClassInfo(reactor.getClassInfo(Util.internalNameToClassName(superName)),
                interfaces, sourceInfoBuilder.getSourceInfo());
    }
}
