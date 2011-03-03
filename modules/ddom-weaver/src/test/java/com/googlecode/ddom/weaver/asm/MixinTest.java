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
package com.googlecode.ddom.weaver.asm;

import org.junit.Test;

import com.google.code.ddom.commons.cl.ClassRef;
import com.googlecode.ddom.weaver.DynamicClassLoader;
import com.googlecode.ddom.weaver.jsr45.JSR45Plugin;
import com.googlecode.ddom.weaver.reactor.Reactor;

public class MixinTest {
    @Test
    public void test() throws Exception {
        ClassLoader parentClassLoader = Test.class.getClassLoader();
        
        Reactor reactor = new Reactor(parentClassLoader);
        reactor.addPlugin(new JSR45Plugin());
        reactor.loadWeavableClass(new ClassRef(Base.class));
        reactor.loadMixin(new ClassRef(BaseMixin.class));
        
        DynamicClassLoader targetClassLoader = new DynamicClassLoader(parentClassLoader);
        
        reactor.generateModel(targetClassLoader);
        
        IBase base = (IBase)targetClassLoader.loadClass("com.googlecode.ddom.weaver.asm.Base").newInstance();
        ((IBaseMixin)base).addItem("test");
    }
}
