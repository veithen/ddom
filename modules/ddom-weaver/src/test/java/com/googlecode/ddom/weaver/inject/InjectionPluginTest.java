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

import org.junit.Assert;
import org.junit.Test;

import com.google.code.ddom.commons.cl.ClassRef;
import com.googlecode.ddom.weaver.DynamicClassLoader;
import com.googlecode.ddom.weaver.jsr45.JSR45Plugin;
import com.googlecode.ddom.weaver.reactor.Reactor;

public class InjectionPluginTest {
    @Test
    public void test() throws Exception {
        ClassLoader parentClassLoader = InjectionPluginTest.class.getClassLoader();
        DynamicClassLoader classLoader = new DynamicClassLoader(parentClassLoader);
        Reactor reactor = new Reactor(parentClassLoader);
        InjectionPlugin plugin = new InjectionPlugin();
        plugin.addBinding(InjectedInterface.class.getName(), new PrototypeInjector(InjectedClass.class.getName()));
        reactor.addPlugin(plugin);
        // TODO: this should not be required; this is a bug
        reactor.addPlugin(new JSR45Plugin());
        reactor.loadWeavableClass(new ClassRef(TargetClass.class));
        reactor.generateModel(classLoader);
        TargetInterface target = (TargetInterface)classLoader.loadClass(TargetClass.class.getName()).newInstance();
        InjectedInterface injected = target.getInjected();
        Assert.assertNotNull(injected);
        Assert.assertEquals(InjectedClass.class, injected.getClass());
    }
}
