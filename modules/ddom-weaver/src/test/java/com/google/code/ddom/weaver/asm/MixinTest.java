package com.google.code.ddom.weaver.asm;

import java.util.Arrays;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.google.code.ddom.weaver.DynamicClassLoader;

public class MixinTest {
    @Test
    public void test() throws Exception {
        ClassLoader parentClassLoader = Test.class.getClassLoader();
        DynamicClassLoader targetClassLoader = new DynamicClassLoader(parentClassLoader);
        MixinInfo mixin = new MixinInfo();
        new ClassReader(parentClassLoader.getResourceAsStream("com/google/code/ddom/weaver/asm/BaseMixin.class")).accept(mixin, 0);
        
        SourceInfoBuilder sourceInfoAdapter2 = new SourceInfoBuilder();
        new ClassReader(parentClassLoader.getResourceAsStream("com/google/code/ddom/weaver/asm/Base.class")).accept(sourceInfoAdapter2, 0);
        
        ClassReader cr = new ClassReader(parentClassLoader.getResourceAsStream("com/google/code/ddom/weaver/asm/Base.class"));
        ClassWriter cw = new ClassWriter(cr, 0);
        SourceMapper sourceMapper = new SourceMapper();
        sourceMapper.addSourceInfo(sourceInfoAdapter2.getSourceInfo());
        sourceMapper.addSourceInfo(mixin.getSourceInfo());
        cr.accept(sourceMapper.getClassAdapter(new MergeAdapter(cw, mixin, sourceMapper)), 0);
        targetClassLoader.processClassDefinition("com.google.code.ddom.weaver.asm.Base", cw.toByteArray());
        
        IBase base = (IBase)targetClassLoader.loadClass("com.google.code.ddom.weaver.asm.Base").newInstance();
        System.out.println(Arrays.asList(base.getClass().getInterfaces()));
        ((IBaseMixin)base).addItem("test");
    }
}
