/*
 * Copyright 2009-2010,2014 Andreas Veithen
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.code.ddom.commons.cl.ClassRef;
import com.googlecode.ddom.weaver.jsr45.JSR45Plugin;
import com.googlecode.ddom.weaver.output.DynamicClassLoader;
import com.googlecode.ddom.weaver.reactor.Reactor;

public class InjectionPluginTest {
    @Test
    public void testPrototypeInjection() throws Exception {
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
        Class<? extends TargetInterface> targetClass = classLoader.loadClass(TargetClass.class.getName()).asSubclass(TargetInterface.class);
        TargetInterface target1 = targetClass.newInstance();
        TargetInterface target2 = targetClass.newInstance();
        InjectedInterface injectedInstanceField1 = target1.getInjectedInstanceField();
        InjectedInterface injectedInstanceField2 = target2.getInjectedInstanceField();
        InjectedInterface injectedClassField1 = target1.getInjectedClassField();
        InjectedInterface injectedClassField2 = target2.getInjectedClassField();
        assertNotNull(injectedInstanceField1);
        assertEquals(InjectedClass.class, injectedInstanceField1.getClass());
        assertNotNull(injectedClassField1);
        assertEquals(InjectedClass.class, injectedClassField1.getClass());
        assertNotSame(injectedInstanceField1, injectedInstanceField2);
        assertSame(injectedClassField1, injectedClassField2);
    }
    
    @Test
    public void testSingletonInjection() throws Exception {
        ClassLoader parentClassLoader = InjectionPluginTest.class.getClassLoader();
        DynamicClassLoader classLoader = new DynamicClassLoader(parentClassLoader);
        Reactor reactor = new Reactor(parentClassLoader);
        InjectionPlugin plugin = new InjectionPlugin();
        plugin.addBinding(InjectedInterface.class.getName(), new SingletonInjector(InjectedSingleton.class.getName(), InjectedInterface.class.getName()));
        reactor.addPlugin(plugin);
        // TODO: this should not be required; this is a bug
        reactor.addPlugin(new JSR45Plugin());
        reactor.loadWeavableClass(new ClassRef(TargetClass.class));
        reactor.generateModel(classLoader);
        Class<? extends TargetInterface> targetClass = classLoader.loadClass(TargetClass.class.getName()).asSubclass(TargetInterface.class);
        TargetInterface target = targetClass.newInstance();
        assertSame(InjectedSingleton.INSTANCE, target.getInjectedInstanceField());
        assertSame(InjectedSingleton.INSTANCE, target.getInjectedClassField());
    }
}
