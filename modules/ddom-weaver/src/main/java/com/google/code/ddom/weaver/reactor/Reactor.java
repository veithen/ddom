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
package com.google.code.ddom.weaver.reactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.google.code.ddom.commons.cl.ClassRef;
import com.google.code.ddom.commons.dag.EdgeRelation;
import com.google.code.ddom.commons.dag.TopologicalSort;
import com.google.code.ddom.weaver.ClassDefinitionProcessor;
import com.google.code.ddom.weaver.ClassDefinitionProcessorException;
import com.google.code.ddom.weaver.ModelWeaverException;
import com.google.code.ddom.weaver.asm.ClassVisitorTee;
import com.google.code.ddom.weaver.jsr45.SourceInfo;
import com.google.code.ddom.weaver.jsr45.SourceMapper;
import com.google.code.ddom.weaver.mixin.MergeAdapter;
import com.google.code.ddom.weaver.mixin.MixinInfo;
import com.google.code.ddom.weaver.mixin.MixinInfoBuilder;
import com.google.code.ddom.weaver.mixin.MixinInfoBuilderCollaborator;
import com.google.code.ddom.weaver.realm.ClassInfo;
import com.google.code.ddom.weaver.realm.ClassRealm;

public class Reactor implements ClassRealm, WeavableClassInjector {
    private static final Logger log = Logger.getLogger(Reactor.class.getName());
    
    private static final EdgeRelation<ClassInfo> inheritanceRelation = new EdgeRelation<ClassInfo>() {
        public boolean isEdge(ClassInfo from, ClassInfo to) {
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
    
    private final Extensions extensions = new Extensions();
    private final ClassLoader classLoader;
    private final List<ReactorPlugin> plugins = new ArrayList<ReactorPlugin>();
    private final Map<String,WeavableClassInfoBuilder> weavableClassInfoBuilders = new HashMap<String,WeavableClassInfoBuilder>();
    private final Map<String,ClassInfo> classInfos = new HashMap<String,ClassInfo>();
    private final List<MixinInfo> mixins = new ArrayList<MixinInfo>();
    private final List<WeavableClassInfo> weavableClasses = new ArrayList<WeavableClassInfo>();
    
    public Reactor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public <T> T get(Class<T> key) {
        return extensions.get(key);
    }

    public void addPlugin(ReactorPlugin plugin) {
        plugins.add(plugin);
        plugin.init(this, extensions);
    }
    
    public void loadWeavableClass(ClassRef classRef) throws ClassNotFoundException {
        loadWeavableClass(new CompiledClass(classRef.getClassDefinition()));
    }
    
    public void loadWeavableClass(ClassDefinitionSource classDefinitionSource) {
        List<WeavableClassInfoBuilderCollaborator> collaborators = new ArrayList<WeavableClassInfoBuilderCollaborator>(plugins.size());
        for (ReactorPlugin plugin : plugins) {
            WeavableClassInfoBuilderCollaborator collaborator = plugin.newWeavableClassInfoBuilderCollaborator();
            if (collaborator != null) {
                collaborators.add(collaborator);
            }
        }
        WeavableClassInfoBuilder builder = new WeavableClassInfoBuilder(classDefinitionSource, collaborators);
        ClassVisitorTee tee = new ClassVisitorTee(builder);
        tee.addVisitors(collaborators);
        classDefinitionSource.accept(tee);
        weavableClassInfoBuilders.put(builder.getName(), builder);
    }
    
    public void loadMixin(ClassRef classRef) throws ClassNotFoundException, ModelWeaverException {
        List<MixinInfoBuilderCollaborator> collaborators = new ArrayList<MixinInfoBuilderCollaborator>(plugins.size());
        for (ReactorPlugin plugin : plugins) {
            MixinInfoBuilderCollaborator collaborator = plugin.newMixinInfoBuilderCollaborator();
            if (collaborator != null) {
                collaborators.add(collaborator);
            }
        }
        MixinInfoBuilder builder = new MixinInfoBuilder(SimpleErrorHandler.INSTANCE, collaborators);
        ClassVisitorTee tee = new ClassVisitorTee(builder);
        tee.addVisitors(collaborators);
        new ClassReader(classRef.getClassDefinition()).accept(tee, 0);
        mixins.add(builder.build(this));
    }
    
    public ClassInfo getClassInfo(String className) {
        return getClassInfo(new ClassRef(classLoader, className));
    }
    
    public ClassInfo getClassInfo(ClassRef classRef) {
        String className = classRef.getClassName();
        ClassInfo classInfo = classInfos.get(className);
        if (classInfo != null) {
            return classInfo;
        } else {
            WeavableClassInfoBuilder builder = weavableClassInfoBuilders.get(className);
            if (builder != null) {
                WeavableClassInfo weavableClassInfo = builder.build(this);
                weavableClassInfoBuilders.remove(className);
                weavableClasses.add(weavableClassInfo);
                classInfo = weavableClassInfo;
            } else {
                Class<?> clazz;
                try {
                    clazz = classRef.load();
                } catch (ClassNotFoundException ex) {
                    throw new ReactorException("Class not found: " + classRef.getClassName());
                }
                Class<?> superclass = clazz.getSuperclass();
                Class<?>[] interfaces = clazz.getInterfaces();
                ClassInfo[] interfaceInfos = new ClassInfo[interfaces.length];
                for (int i=0; i<interfaces.length; i++) {
                    interfaceInfos[i] = getClassInfo(new ClassRef(interfaces[i]));
                }
                Extensions extensions = new Extensions();
                NonWeavableClassInfo nonWeavableClassInfo = new NonWeavableClassInfo(className, clazz.isInterface(),
                        superclass == null ? null : getClassInfo(new ClassRef(clazz.getSuperclass())),
                        interfaceInfos, extensions);
                for (ReactorPlugin plugin : plugins) {
                    plugin.processNonWeavableClassInfo(nonWeavableClassInfo, clazz, extensions);
                }
                classInfo = nonWeavableClassInfo;
            }
            classInfos.put(className, classInfo);
            return classInfo;
        }
    }
    
    public void generateModel(ClassDefinitionProcessor processor) throws ClassNotFoundException, ClassDefinitionProcessorException, ModelWeaverException {
        resolveWeavableClasses();
        for (ReactorPlugin plugin : plugins) {
            plugin.resolve(this);
        }
        for (ReactorPlugin plugin : plugins) {
            plugin.generateWeavableClasses(this, this);
        }
        resolveWeavableClasses();
        weave(processor);
    }
    
    private void resolveWeavableClasses() throws ClassNotFoundException, ModelWeaverException {
        Collection<String> classes = weavableClassInfoBuilders.keySet();
        while (!classes.isEmpty()) {
            getClassInfo(classes.iterator().next());
        }
    }
    
    private void weave(ClassDefinitionProcessor processor) throws ClassNotFoundException, ClassDefinitionProcessorException {
        // We need to sort the weavable classes so that we can satisfy the ClassDefinitionProcessor
        // contract.
        for (WeavableClassInfo weavableClass : TopologicalSort.sort(weavableClasses, inheritanceRelation)) {
            if (weavableClass.isInterface()) {
                processor.processClassDefinition(weavableClass.getName(), weavableClass.getClassDefinitionSource().getClassDefinition(this));
            } else {
                // Select the mixins that apply to weavableClass
                List<MixinInfo> selectedMixins = new ArrayList<MixinInfo>();
                for (MixinInfo mixin : mixins) {
                    for (ClassInfo target : mixin.getTargets()) {
                        if (target.isAssignableFrom(weavableClass) && !target.isAssignableFrom(weavableClass.getSuperclass())) {
                            selectedMixins.add(mixin);
                            break;
                        }
                    }
                }
                // Weave the class. Note that the weave method will take care of the case where no mixins apply.
                weave(processor, weavableClass, selectedMixins);
            }
        }
    }
    
    private void weave(ClassDefinitionProcessor processor, WeavableClassInfo weavableClass, List<MixinInfo> mixins) throws ClassDefinitionProcessorException {
        ClassWriter cw = new ReactorAwareClassWriter(this, 0);
        ClassDefinitionSource classDefinitionSource = weavableClass.getClassDefinitionSource();
        boolean woven = !mixins.isEmpty();
        boolean generated = classDefinitionSource instanceof GeneratedClass;
        ClassVisitor out = cw;
        for (ReactorPlugin plugin : plugins) {
            out = plugin.prepareForOutput(out, generated, woven);
        }
        if (out == cw && !woven) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Copying unmodified class " + weavableClass);
            }
            processor.processClassDefinition(weavableClass.getName(), classDefinitionSource.getClassDefinition(this));
        } else {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Weaving " + weavableClass + "; mixins: " + mixins);
            }
            SourceMapper sourceMapper = new SourceMapper();
            sourceMapper.addSourceInfo(weavableClass.get(SourceInfo.class));
            for (MixinInfo mixin : mixins) {
                sourceMapper.addSourceInfo(mixin.get(SourceInfo.class));
            }
            weavableClass.getClassDefinitionSource().accept(sourceMapper.getClassAdapter(new MergeAdapter(out, mixins, sourceMapper, SimpleErrorHandler.INSTANCE)));
            processor.processClassDefinition(weavableClass.getName(), cw.toByteArray());
        }
    }
}
