package com.google.code.ddom.weaver.asm;

import java.util.Arrays;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.EmptyVisitor;

import com.google.code.ddom.weaver.DynamicClassLoader;

public class MixinTest {
    @Test
    public void test() throws Exception {
        ClassLoader parentClassLoader = Test.class.getClassLoader();
        DynamicClassLoader targetClassLoader = new DynamicClassLoader(parentClassLoader);
        MixinInfo mixin = new MixinInfo();
        SourceInfoBuilder sourceInfoBuilder = new SourceInfoBuilder(mixin);
        new ClassReader(parentClassLoader.getResourceAsStream("com/google/code/ddom/weaver/asm/BaseMixin.class")).accept(sourceInfoBuilder, 0);
        SourceInfo sourceInfo = sourceInfoBuilder.getSourceInfo();
        System.out.println("absoluteSourceFile = " + sourceInfo.getAbsoluteSourceFile());
        System.out.println("maxLine = " + sourceInfo.getMaxLine());
        
        SourceInfoBuilder sourceInfoAdapter2 = new SourceInfoBuilder(new EmptyVisitor());
        new ClassReader(parentClassLoader.getResourceAsStream("com/google/code/ddom/weaver/asm/Base.class")).accept(sourceInfoAdapter2, 0);
        
        ClassReader cr = new ClassReader(parentClassLoader.getResourceAsStream("com/google/code/ddom/weaver/asm/Base.class"));
        ClassWriter cw = new ClassWriter(cr, 0);
        SourceMapper sma = new SourceMapper();
        sma.addSourceInfo(sourceInfoAdapter2.getSourceInfo());
        sma.addSourceInfo(sourceInfo);
        cr.accept(sma.getClassAdapter(new MergeAdapter(cw, mixin)), 0);
        targetClassLoader.processClassDefinition("com.google.code.ddom.weaver.asm.Base", cw.toByteArray());
        
        IBase base = (IBase)targetClassLoader.loadClass("com.google.code.ddom.weaver.asm.Base").newInstance();
        System.out.println(Arrays.asList(base.getClass().getInterfaces()));
        ((IBaseMixin)base).addItem("test");
    }
}
