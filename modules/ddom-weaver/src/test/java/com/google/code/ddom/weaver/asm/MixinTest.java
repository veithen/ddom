package com.google.code.ddom.weaver.asm;

import java.util.Arrays;

import org.junit.Test;

import com.google.code.ddom.commons.cl.ClassRef;
import com.google.code.ddom.weaver.DynamicClassLoader;
import com.google.code.ddom.weaver.jsr45.JSR45Plugin;
import com.google.code.ddom.weaver.reactor.Reactor;

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
        
        IBase base = (IBase)targetClassLoader.loadClass("com.google.code.ddom.weaver.asm.Base").newInstance();
        System.out.println(Arrays.asList(base.getClass().getInterfaces()));
        ((IBaseMixin)base).addItem("test");
    }
}
