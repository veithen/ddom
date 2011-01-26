/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.weaver;

import java.util.Collections;
import java.util.Map;

import com.google.code.ddom.commons.cl.ClassRef;
import com.googlecode.ddom.backend.Backend;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.frontend.Frontend;
import com.googlecode.ddom.weaver.dump.DumpPlugin;
import com.googlecode.ddom.weaver.ext.ModelExtensionPlugin;
import com.googlecode.ddom.weaver.inject.InjectionPlugin;
import com.googlecode.ddom.weaver.inject.PrototypeInjector;
import com.googlecode.ddom.weaver.jsr45.JSR45Plugin;
import com.googlecode.ddom.weaver.reactor.Reactor;
import com.googlecode.ddom.weaver.reactor.ReactorException;
import com.googlecode.ddom.weaver.verifier.VerifierPlugin;

public class ModelWeaver {
    private final ClassDefinitionProcessor processor;
    private final Backend backend;
    private final Reactor reactor;
    private final ModelExtensionPlugin modelExtensionPlugin;
    private final InjectionPlugin injectionPlugin;
    
    public ModelWeaver(ClassLoader classLoader, ClassDefinitionProcessor processor, Backend backend) throws ModelWeaverException {
        this.processor = processor;
        this.backend = backend;
        reactor = new Reactor(classLoader);
        reactor.addPlugin(new JSR45Plugin());
        modelExtensionPlugin = new ModelExtensionPlugin();
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreCDATASection.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreComment.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreDocumentTypeDeclaration.class)); 
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreEntityReference.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreNamespaceDeclaration.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreNSAwareAttribute.class));
        modelExtensionPlugin.addRequiredImplementation(new ClassRef(CoreNSAwareElement.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreNSUnawareAttribute.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreNSUnawareElement.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreProcessingInstruction.class));
//        implementationPlugin.addRequiredImplementation(new ClassRef(CoreText.class));
//        reactor.addPlugin(DumpPlugin.INSTANCE);
        reactor.addPlugin(VerifierPlugin.INSTANCE);
        injectionPlugin = new InjectionPlugin();
    }

    public void weave(Map<String,Frontend> frontends) throws ModelWeaverException {
        reactor.addPlugin(modelExtensionPlugin);
        // TODO: clean this up
        ModelExtension modelExtension = frontends.values().iterator().next().getModelExtension();
        if (modelExtension == null) {
            injectionPlugin.addBinding(ModelExtension.class.getName(), null);
        } else {
            String modelExtensionClass = modelExtension.getClass().getName();
            injectionPlugin.addBinding(ModelExtension.class.getName(), new PrototypeInjector(modelExtensionClass));
        }
        reactor.addPlugin(injectionPlugin);
        try {
            for (ClassRef classRef : backend.getWeavableClasses().getClassRefs()) {
                reactor.loadWeavableClass(classRef);
            }
        } catch (ClassNotFoundException ex) {
            throw new ModelWeaverException(ex);
        }
        try {
            for (Frontend frontend : frontends.values()) {
                for (ClassRef classRef : frontend.getMixins(Collections.unmodifiableMap(frontends)).getClassRefs()) {
                    reactor.loadMixin(classRef);
                }
            }
            reactor.generateModel(processor);
        } catch (ReactorException ex) {
            ModelWeaverException ex2 = new ModelWeaverException(ex.getMessage(), ex.getCause());
            ex2.setStackTrace(ex.getStackTrace());
            throw ex2;
        } catch (ClassNotFoundException ex) {
            throw new ModelWeaverException(ex);
        } catch (ClassDefinitionProcessorException ex) {
            throw new ModelWeaverException(ex); // TODO
        }
    }
}
