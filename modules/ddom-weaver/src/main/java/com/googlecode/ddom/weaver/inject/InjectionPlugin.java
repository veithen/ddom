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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassVisitor;

import com.googlecode.ddom.weaver.reactor.ClassTransformation;
import com.googlecode.ddom.weaver.reactor.ReactorPlugin;
import com.googlecode.ddom.weaver.reactor.WeavableClassInfo;
import com.googlecode.ddom.weaver.reactor.WeavableClassInfoBuilderCollaborator;

public class InjectionPlugin extends ReactorPlugin {
    private static final Log log = LogFactory.getLog(InjectionPlugin.class);
    
    private final Map<String,Injector> bindings = new HashMap<String,Injector>();
    
    public void addBinding(String fieldType, Injector injector) {
        bindings.put(fieldType, injector);
    }
    
    /**
     * Add the given binding. The binding will be configured with a {@link SingletonInjector} or a
     * {@link PrototypeInjector} depending on whether the injected class is a singleton as defined
     * in the Javadoc of the {@link SingletonInjector} class.
     * 
     * @param fieldType
     *            the target field type
     * @param injectedClass
     *            the injected class or <code>null</code> to inject a <code>null</code> value
     */
    public <T> void addBinding(Class<T> fieldType, Class<? extends T> injectedClass) {
        Injector injector;
        if (injectedClass == null) {
            injector = null;
        } else {
            Field instanceField;
            try {
                Field candidateField = injectedClass.getField("INSTANCE");
                int modifiers = candidateField.getModifiers();
                // Note: getField only returns public fields, so no need to check for that
                // modifier here
                if (!Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
                    if (log.isWarnEnabled()) {
                        log.warn("Ignoring public INSTANCE field in " + injectedClass.getName() + " that is not static final");
                    }
                    instanceField = null;
                } else if (!fieldType.isAssignableFrom(candidateField.getType())) {
                    if (log.isWarnEnabled()) {
                        log.warn("Ignoring INSTANCE field of incompatible type in " + injectedClass.getName());
                    }
                    instanceField = null;
                } else {
                    instanceField = candidateField;
                }
            } catch (NoSuchFieldException ex) {
                instanceField = null;
            }
            if (instanceField != null) {
                injector = new SingletonInjector(injectedClass.getName(), instanceField.getType().getName());
            } else {
                injector = new PrototypeInjector(injectedClass.getName());
            }
        }
        addBinding(fieldType.getName(), injector);
    }

    @Override
    public WeavableClassInfoBuilderCollaborator newWeavableClassInfoBuilderCollaborator() {
        return new InjectionInfoBuilder(bindings);
    }

    @Override
    public ClassTransformation getPreTransformation(WeavableClassInfo classInfo) {
        final InjectionInfo injectionInfo = classInfo.get(InjectionInfo.class);
        if (injectionInfo == null) {
            return null;
        } else {
            return new ClassTransformation() {
                public ClassVisitor adapt(ClassVisitor cv) {
                    return new InjectionAdapter(cv, injectionInfo);
                }
            };
        }
    }
}
