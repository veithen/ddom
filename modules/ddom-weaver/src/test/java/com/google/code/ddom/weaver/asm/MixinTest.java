package com.google.code.ddom.weaver.asm;

import java.util.Arrays;

import org.junit.Test;

import com.google.code.ddom.weaver.DynamicClassLoader;

public class MixinTest {
    @Test
    public void test() throws Exception {
        ClassLoader parentClassLoader = Test.class.getClassLoader();
        
        Reactor reactor = new Reactor(parentClassLoader);
        reactor.loadWeavableClass("com.google.code.ddom.weaver.asm.Base");
        reactor.loadMixin("com.google.code.ddom.weaver.asm.BaseMixin");
        
        DynamicClassLoader targetClassLoader = new DynamicClassLoader(parentClassLoader);
        
        reactor.weave(targetClassLoader);
        
        IBase base = (IBase)targetClassLoader.loadClass("com.google.code.ddom.weaver.asm.Base").newInstance();
        System.out.println(Arrays.asList(base.getClass().getInterfaces()));
        ((IBaseMixin)base).addItem("test");
    }
}