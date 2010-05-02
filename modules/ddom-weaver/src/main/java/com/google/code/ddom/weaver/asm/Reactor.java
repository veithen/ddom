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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.google.code.ddom.commons.cl.ClassLoaderUtils;
import com.google.code.ddom.weaver.ClassDefinitionProcessor;
import com.google.code.ddom.weaver.ClassDefinitionProcessorException;
import com.google.code.ddom.weaver.asm.util.ClassVisitorTee;

public class Reactor {
    private final ClassLoader classLoader;
    private final Map<String,WeavableClassInfoBuilder> weavableClassInfoBuilders = new HashMap<String,WeavableClassInfoBuilder>();
    private final Map<String,ClassInfo> classInfos = new HashMap<String,ClassInfo>();
    private final List<MixinInfo> mixins = new ArrayList<MixinInfo>();
    
    public Reactor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void loadWeavableClass(String className) throws ClassNotFoundException, IOException {
        byte[] classDefinition = ClassLoaderUtils.getClassDefinition(classLoader, className);
        SourceInfoBuilder sourceInfoBuilder = new SourceInfoBuilder();
        WeavableClassInfoBuilder builder = new WeavableClassInfoBuilder(this, classDefinition, sourceInfoBuilder);
        new ClassReader(classDefinition).accept(new ClassVisitorTee(sourceInfoBuilder, builder), 0);
        weavableClassInfoBuilders.put(className, builder);
    }
    
    public void loadMixin(String className) throws ClassNotFoundException, IOException {
        SourceInfoBuilder sourceInfoBuilder = new SourceInfoBuilder();
        MixinInfoBuilder builder = new MixinInfoBuilder(this, sourceInfoBuilder);
        new ClassReader(ClassLoaderUtils.getClassDefinition(classLoader, className)).accept(new ClassVisitorTee(sourceInfoBuilder, builder), 0);
        mixins.add(builder.build());
    }
    
    public ClassInfo getClassInfo(String className) throws ClassNotFoundException {
        ClassInfo classInfo = classInfos.get(className);
        if (classInfo != null) {
            return classInfo;
        } else {
            WeavableClassInfoBuilder builder = weavableClassInfoBuilders.get(className);
            if (builder != null) {
                classInfo = builder.build();
            } else {
                Class<?> clazz = classLoader.loadClass(className);
                Class<?> superclass = clazz.getSuperclass();
                Class<?>[] interfaces = clazz.getInterfaces();
                ClassInfo[] interfaceInfos = new ClassInfo[interfaces.length];
                for (int i=0; i<interfaces.length; i++) {
                    interfaceInfos[i] = getClassInfo(interfaces[i].getName());
                }
                classInfo = new NonWeavableClassInfo(className,
                        superclass == null ? null : getClassInfo(clazz.getSuperclass().getName()),
                        interfaceInfos);
            }
            classInfos.put(className, classInfo);
            return classInfo;
        }
    }
    
    public void weave(ClassDefinitionProcessor processor) throws ClassNotFoundException, ClassDefinitionProcessorException {
        for (String className : weavableClassInfoBuilders.keySet()) {
            WeavableClassInfo weavableClass = (WeavableClassInfo)getClassInfo(className);
            List<MixinInfo> selectedMixins = new ArrayList<MixinInfo>();
            for (MixinInfo mixin : mixins) {
                ClassInfo target = mixin.getTarget();
                if (target.isAssignableFrom(weavableClass) && !target.isAssignableFrom(weavableClass.getSuperclass())) {
                    selectedMixins.add(mixin);
                }
            }
            weave(processor, weavableClass, selectedMixins);
        }
    }
    
    private void weave(ClassDefinitionProcessor processor, WeavableClassInfo weavableClass, List<MixinInfo> mixins) throws ClassDefinitionProcessorException {
        System.out.println("Weaving " + weavableClass + "; mixins: " + mixins);
        ClassReader cr = new ClassReader(weavableClass.getClassDefinition());
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        SourceMapper sourceMapper = new SourceMapper();
        sourceMapper.addSourceInfo(weavableClass.getSourceInfo());
        for (MixinInfo mixin : mixins) {
            sourceMapper.addSourceInfo(mixin.getSourceInfo());
        }
        MixinInfo mixin = mixins.get(0); // TODO
        cr.accept(sourceMapper.getClassAdapter(new MergeAdapter(new TraceClassVisitor(cw, new PrintWriter(System.out)), mixin, sourceMapper)), 0);
        processor.processClassDefinition("com.google.code.ddom.weaver.asm.Base", cw.toByteArray());
    }
}
