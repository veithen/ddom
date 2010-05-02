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
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;

import com.google.code.ddom.commons.cl.ClassLoaderUtils;
import com.google.code.ddom.weaver.asm.util.ClassVisitorTee;

public class Reactor {
    private final ClassLoader classLoader;
    private final Map<String,WeavableClassInfoBuilder> weavableClassInfoBuilders = new HashMap<String,WeavableClassInfoBuilder>();
    private final Map<String,ClassInfo> classInfos = new HashMap<String,ClassInfo>();
    
    public Reactor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void loadWeavableClass(String className) throws ClassNotFoundException, IOException {
        SourceInfoBuilder sourceInfoBuilder = new SourceInfoBuilder();
        WeavableClassInfoBuilder builder = new WeavableClassInfoBuilder(this, sourceInfoBuilder);
        InputStream in = ClassLoaderUtils.getClassDefinitionAsStream(classLoader, className);
        try {
            new ClassReader(in).accept(new ClassVisitorTee(sourceInfoBuilder, builder), 0);
        } finally {
            in.close();
        }
        weavableClassInfoBuilders.put(className, builder);
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
                classInfo = new NonWeavableClassInfo(
                        superclass == null ? null : getClassInfo(clazz.getSuperclass().getName()),
                        interfaceInfos);
            }
            classInfos.put(className, classInfo);
            return classInfo;
        }
    }
}
