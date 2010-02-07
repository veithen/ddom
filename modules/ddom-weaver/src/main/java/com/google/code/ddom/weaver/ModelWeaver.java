/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.weaver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.aspectj.bridge.AbortException;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.IClassFileProvider;
import org.aspectj.weaver.IUnwovenClassFile;
import org.aspectj.weaver.IWeaveRequestor;
import org.aspectj.weaver.bcel.BcelWeaver;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.bcel.UnwovenClassFile;

import com.google.code.ddom.commons.cl.ClassLoaderUtils;
import com.google.code.ddom.commons.cl.ClassUtils;
import com.google.code.ddom.commons.cl.Package;
import com.google.code.ddom.spi.model.Backend;
import com.google.code.ddom.spi.model.Frontend;

public class ModelWeaver implements IClassFileProvider, IWeaveRequestor, IMessageHandler {
    private static final Logger log = Logger.getLogger(ModelWeaver.class.getName());
    
    private final ClassLoader classLoader;
    private final ClassDefinitionProcessor processor;
    private final UnwovenClassFile[] classFiles;
    
    public ModelWeaver(ClassLoader classLoader, ClassDefinitionProcessor processor, Backend backend) throws ClassNotFoundException {
        this.classLoader = classLoader;
        this.processor = processor;
        Collection<Class<?>> classes = Package.forClassName(classLoader, backend.getNodeFactoryClassName()).getClasses();
        classFiles = new UnwovenClassFile[classes.size()];
        int i = 0;
        // We sort the classes hierarchically to prevent BcelWeaver from emitting the same class
        // multiple times (this occurs if a subclass is woven before its superclass).
        for (Class<?> clazz : ClassUtils.sortHierarchically(classes)) {
            String className = clazz.getName();
            classFiles[i++] = new UnwovenClassFile(
                    ClassLoaderUtils.getResourceNameForClassName(className),
                    className,
                    ClassLoaderUtils.getClassDefinition(classLoader, className));
        }
    }

    public void weave(Map<String,Frontend> frontends) {
        BcelWorld world = new BcelWorld(classLoader, this, null);
        BcelWeaver weaver = new BcelWeaver(world);
        for (UnwovenClassFile classFile : classFiles) {
            weaver.addClassFile(classFile, true);
        }
        for (Frontend frontend : frontends.values()) {
            for (String aspectClass : frontend.getAspectClasses(Collections.unmodifiableMap(frontends))) {
                weaver.addLibraryAspect(aspectClass);
            }
        }
        weaver.prepareForWeave();
        try {
            weaver.weave(this);
        } catch (IOException ex) {
            throw new RuntimeException(ex); // TODO
        }
    }

    public Iterator getClassFileIterator() {
        return Arrays.asList(classFiles).iterator();
    }

    public IWeaveRequestor getRequestor() {
        return this;
    }

    public boolean isApplyAtAspectJMungersOnly() {
        return false;
    }

    public void acceptResult(IUnwovenClassFile result) {
        processor.processClassDefinition(result.getClassName(), result.getBytes());
    }

    public void addingTypeMungers() {}
    public void processingReweavableState() {}
    public void weaveCompleted() {}
    public void weavingAspects() {}
    public void weavingClasses() {}
    
    public boolean handleMessage(IMessage message) {
        if (message.getKind().compareTo(IMessage.ERROR) >= 0) {
            log.severe(message.toString());
            throw new AbortException(message);
        } else {
            log.warning(message.toString());
            return true;
        }
    }
    
    public boolean isIgnoring(IMessage.Kind kind) {
        return kind.compareTo(IMessage.WARNING) < 0; 
    }
    
    public void dontIgnore(IMessage.Kind kind) {
    }
    
    public void ignore(IMessage.Kind kind) {
    }
}
