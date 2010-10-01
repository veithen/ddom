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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

import com.google.code.ddom.commons.cl.ClassRef;
import com.google.code.ddom.commons.dag.EdgeRelation;
import com.google.code.ddom.commons.dag.TopologicalSort;
import com.google.code.ddom.weaver.ClassDefinitionProcessor;
import com.google.code.ddom.weaver.ClassDefinitionProcessorException;
import com.google.code.ddom.weaver.ModelWeaverException;
import com.google.code.ddom.weaver.asm.ClassVisitorTee;
import com.google.code.ddom.weaver.implementation.ImplementationInfo;
import com.google.code.ddom.weaver.jsr45.SourceInfo;
import com.google.code.ddom.weaver.jsr45.SourceInfoBuilder;
import com.google.code.ddom.weaver.jsr45.SourceMapper;
import com.google.code.ddom.weaver.mixin.MergeAdapter;
import com.google.code.ddom.weaver.mixin.MixinInfo;
import com.google.code.ddom.weaver.mixin.MixinInfoBuilder;
import com.google.code.ddom.weaver.realm.ClassInfo;
import com.google.code.ddom.weaver.realm.ClassRealm;

public class Reactor implements ClassRealm {
    private static final Logger log = Logger.getLogger(Reactor.class.getName());
    
    // TODO: introduce system property for this
    private static final boolean dump = false;
    
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
    
    private final ClassLoader classLoader;
    private final List<ReactorPlugin> plugins = new ArrayList<ReactorPlugin>();
    private final Map<String,WeavableClassInfoBuilder> weavableClassInfoBuilders = new HashMap<String,WeavableClassInfoBuilder>();
    private final Map<String,ClassInfo> classInfos = new HashMap<String,ClassInfo>();
    private final List<MixinInfo> mixins = new ArrayList<MixinInfo>();
    private final List<ClassInfo> requiredImplementations = new ArrayList<ClassInfo>();
    private List<WeavableClassInfo> weavableClasses;
    private Map<ClassInfo,WeavableClassInfo> implementations;
    
    public Reactor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void addPlugin(ReactorPlugin plugin) {
        plugin.init(this);
        plugins.add(plugin);
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
        WeavableClassInfoBuilder builder = new WeavableClassInfoBuilder(this, classDefinitionSource, collaborators);
        ClassVisitorTee tee = new ClassVisitorTee(builder);
        tee.addVisitors(collaborators);
        classDefinitionSource.accept(tee);
        weavableClassInfoBuilders.put(builder.getName(), builder);
    }
    
    public void loadMixin(ClassRef classRef) throws ClassNotFoundException, ModelWeaverException {
        SourceInfoBuilder sourceInfoBuilder = new SourceInfoBuilder();
        MixinInfoBuilder builder = new MixinInfoBuilder(this, sourceInfoBuilder, SimpleErrorHandler.INSTANCE);
        new ClassReader(classRef.getClassDefinition()).accept(new ClassVisitorTee(sourceInfoBuilder, builder), 0);
        mixins.add(builder.build());
    }
    
    public void addRequiredImplementation(ClassRef iface) throws ClassNotFoundException {
        requiredImplementations.add(getClassInfo(iface));
    }
    
    public ClassInfo getClassInfo(String className) throws ClassNotFoundException {
        return getClassInfo(new ClassRef(classLoader, className));
    }
    
    public ClassInfo getClassInfo(ClassRef classRef) throws ClassNotFoundException {
        String className = classRef.getClassName();
        ClassInfo classInfo = classInfos.get(className);
        if (classInfo != null) {
            return classInfo;
        } else {
            WeavableClassInfoBuilder builder = weavableClassInfoBuilders.get(className);
            if (builder != null) {
                classInfo = builder.build();
            } else {
                Class<?> clazz = classRef.load();
                Class<?> superclass = clazz.getSuperclass();
                Class<?>[] interfaces = clazz.getInterfaces();
                ClassInfo[] interfaceInfos = new ClassInfo[interfaces.length];
                for (int i=0; i<interfaces.length; i++) {
                    interfaceInfos[i] = getClassInfo(new ClassRef(interfaces[i]));
                }
                classInfo = new NonWeavableClassInfo(className, clazz.isInterface(),
                        superclass == null ? null : getClassInfo(new ClassRef(clazz.getSuperclass())),
                        interfaceInfos);
            }
            classInfos.put(className, classInfo);
            return classInfo;
        }
    }
    
    public void generateModel(ClassDefinitionProcessor processor) throws ClassNotFoundException, ClassDefinitionProcessorException, ModelWeaverException {
        resolveWeavableClasses();
        for (ReactorPlugin plugin : plugins) {
            plugin.resolve();
        }
        weave(processor);
    }
    
    private void resolveWeavableClasses() throws ClassNotFoundException, ModelWeaverException {
        weavableClasses = new ArrayList<WeavableClassInfo>(weavableClassInfoBuilders.size());
        implementations = new HashMap<ClassInfo,WeavableClassInfo>();
        for (String className : weavableClassInfoBuilders.keySet()) {
            WeavableClassInfo weavableClass = (WeavableClassInfo)getClassInfo(className);
            if (weavableClass.get(ImplementationInfo.class).isImplementation()) {
                boolean found = false;
                for (ClassInfo iface : requiredImplementations) {
                    if (iface.isAssignableFrom(weavableClass)) {
                        ClassInfo impl = implementations.get(iface);
                        if (impl != null) {
                            throw new ModelWeaverException("Duplicate implementation: an implementation of " + iface + " has already been found, namely " + impl);
                        } else {
                            implementations.put(iface, weavableClass);
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    throw new ModelWeaverException("The class " + weavableClass + " was annotated with @Implementation, but this is not expected");
                }
            }
            weavableClasses.add(weavableClass);
        }
        if (implementations.size() != requiredImplementations.size()) {
            Set<ClassInfo> missingImplementations = new HashSet<ClassInfo>(requiredImplementations);
            missingImplementations.removeAll(implementations.keySet());
            throw new ModelWeaverException("The implementations for the following interfaces have not been found: " + missingImplementations);
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("Implementation map: " + implementations);
        }
    }
    
    /**
     * Get all weavable classes that are annotated with
     * {@link com.google.code.ddom.backend.Implementation} and implement (are assignable to) a given
     * interface.
     * 
     * @param iface
     * @return
     */
    public List<WeavableClassInfo> getImplementations(ClassInfo iface) {
        List<WeavableClassInfo> implementations = new ArrayList<WeavableClassInfo>();
        for (WeavableClassInfo candidate : weavableClasses) {
            if (candidate.get(ImplementationInfo.class).isImplementation() && iface.isAssignableFrom(candidate)) {
                implementations.add(candidate);
            }
        }
        return implementations;
    }
    
    private void weave(ClassDefinitionProcessor processor) throws ClassNotFoundException, ClassDefinitionProcessorException {
        // We need to sort the weavable classes so that we can satisfy the ClassDefinitionProcessor
        // contract.
        for (WeavableClassInfo weavableClass : TopologicalSort.sort(weavableClasses, inheritanceRelation)) {
            if (!weavableClass.isInterface()) {
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
        if (mixins.isEmpty()) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Copying unmodified class " + weavableClass);
            }
            processor.processClassDefinition(weavableClass.getName(), weavableClass.getClassDefinitionSource().getClassDefinition(this));
        } else {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Weaving " + weavableClass + "; mixins: " + mixins);
            }
            ClassWriter cw = new ReactorAwareClassWriter(this, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            SourceMapper sourceMapper = new SourceMapper();
            sourceMapper.addSourceInfo(weavableClass.get(SourceInfo.class));
            for (MixinInfo mixin : mixins) {
                sourceMapper.addSourceInfo(mixin.getSourceInfo());
            }
            ClassVisitor out = cw;
            if (dump) {
                out = new TraceClassVisitor(out, new PrintWriter(System.out));
            }
            weavableClass.getClassDefinitionSource().accept(sourceMapper.getClassAdapter(new MergeAdapter(out, mixins, sourceMapper, SimpleErrorHandler.INSTANCE)));
            processor.processClassDefinition(weavableClass.getName(), cw.toByteArray());
        }
    }
}
