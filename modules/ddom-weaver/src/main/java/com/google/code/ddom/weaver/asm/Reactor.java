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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.google.code.ddom.commons.cl.ClassLoaderUtils;
import com.google.code.ddom.commons.dag.EdgeRelation;
import com.google.code.ddom.commons.dag.TopologicalSort;
import com.google.code.ddom.weaver.ClassDefinitionProcessor;
import com.google.code.ddom.weaver.ClassDefinitionProcessorException;
import com.google.code.ddom.weaver.ModelWeaverException;
import com.google.code.ddom.weaver.asm.util.ClassVisitorTee;

public class Reactor {
    // TODO: introduce system property for this
    private static final boolean dump = false;
    
    private static final EdgeRelation<WeavableClassInfo> inheritanceRelation = new EdgeRelation<WeavableClassInfo>() {
        public boolean isEdge(WeavableClassInfo from, WeavableClassInfo to) {
            if (to.isInterface()) {
                for (ClassInfo iface : from.getInterfaces()) {
                    if (iface == to) {
                        return true;
                    }
                }
                return false;
            } else {
                return from.getSuperclass() == to;
            }
        }
    };
    
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
    
    public void loadMixin(String className) throws ClassNotFoundException, IOException, ModelWeaverException {
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
                classInfo = new NonWeavableClassInfo(className, clazz.isInterface(),
                        superclass == null ? null : getClassInfo(clazz.getSuperclass().getName()),
                        interfaceInfos);
            }
            classInfos.put(className, classInfo);
            return classInfo;
        }
    }
    
    public void weave(ClassDefinitionProcessor processor) throws ClassNotFoundException, ClassDefinitionProcessorException {
        // We need to sort the weavable classes so that defineClass doesn't complain when
        // a DynamicClassLoader is used.
        List<WeavableClassInfo> weavableClasses = new ArrayList<WeavableClassInfo>(weavableClassInfoBuilders.size());
        for (String className : weavableClassInfoBuilders.keySet()) {
            weavableClasses.add((WeavableClassInfo)getClassInfo(className));
        }
        for (WeavableClassInfo weavableClass : TopologicalSort.sort(weavableClasses, inheritanceRelation)) {
            if (!weavableClass.isInterface()) {
                List<MixinInfo> selectedMixins = new ArrayList<MixinInfo>();
                for (MixinInfo mixin : mixins) {
                    for (ClassInfo target : mixin.getTargets()) {
                        if (target.isAssignableFrom(weavableClass) && !target.isAssignableFrom(weavableClass.getSuperclass())) {
                            selectedMixins.add(mixin);
                            break;
                        }
                    }
                }
                weave(processor, weavableClass, selectedMixins);
            }
        }
    }
    
    private void weave(ClassDefinitionProcessor processor, WeavableClassInfo weavableClass, List<MixinInfo> mixins) throws ClassDefinitionProcessorException {
        System.out.println("Weaving " + weavableClass + "; mixins: " + mixins);
        ClassReader cr = new ClassReader(weavableClass.getClassDefinition());
        ClassWriter cw = new ReactorAwareClassWriter(this, cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        SourceMapper sourceMapper = new SourceMapper();
        sourceMapper.addSourceInfo(weavableClass.getSourceInfo());
        for (MixinInfo mixin : mixins) {
            sourceMapper.addSourceInfo(mixin.getSourceInfo());
        }
        ClassVisitor out = cw;
        if (dump) {
            out = new TraceClassVisitor(out, new PrintWriter(System.out));
        }
        cr.accept(sourceMapper.getClassAdapter(new MergeAdapter(out, mixins, sourceMapper)), 0);
        processor.processClassDefinition(weavableClass.getName(), cw.toByteArray());
    }
}
