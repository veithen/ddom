/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.frontend.Frontend;
import com.googlecode.ddom.weaver.dump.DumpPlugin;
import com.googlecode.ddom.weaver.ext.ModelExtensionPlugin;
import com.googlecode.ddom.weaver.inject.InjectionPlugin;
import com.googlecode.ddom.weaver.inject.PrototypeInjector;
import com.googlecode.ddom.weaver.jsr45.JSR45Plugin;
import com.googlecode.ddom.weaver.output.ClassDefinitionProcessor;
import com.googlecode.ddom.weaver.output.ClassDefinitionProcessorException;
import com.googlecode.ddom.weaver.reactor.Reactor;
import com.googlecode.ddom.weaver.reactor.ReactorException;
import com.googlecode.ddom.weaver.remapper.RemapperPlugin;
import com.googlecode.ddom.weaver.verifier.VerifierPlugin;

public class ModelWeaver {
    private ClassLoader classLoader;
    private ClassDefinitionProcessor processor;
    private Backend backend;
    private Map<String,Frontend> frontends;
    private String outputPackage;
    private String rootPackage;
    
    /**
     * Set the class loader from which the weaver will load classes.
     * 
     * @param classLoader
     *            the class loader
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    /**
     * Set the class definition processor to which the woven classes will be submitted.
     * 
     * @param processor
     *            the class definition processor
     */
    public void setProcessor(ClassDefinitionProcessor processor) {
        this.processor = processor;
    }
    
    /**
     * Set the back-end implementation to be used.
     * 
     * @param backend
     *            the back-end implementation
     */
    public void setBackend(Backend backend) {
        this.backend = backend;
        rootPackage = null;
    }
    
    /**
     * Set the front-end implementations to be used.
     * 
     * @param backend
     *            the back-end implementation
     */
    public void setFrontends(Map<String, Frontend> frontends) {
        this.frontends = frontends;
    }

    /**
     * Set the Java package for classes produced by the weaver. If no output package is set, then
     * woven classes will preserve their original name. Note that this only works if the
     * {@link ClassDefinitionProcessor} set with {@link #setProcessor(ClassDefinitionProcessor)}
     * loads the woven classes into a class loader other than the one specified with
     * {@link #setClassLoader(ClassLoader)}.
     * 
     * @param outputPackage
     *            the name of the output package
     */
    public void setOutputPackage(String outputPackage) {
        this.outputPackage = outputPackage;
    }

    private String getRootPackage() {
        if (rootPackage == null) {
            rootPackage = backend.getWeavableClasses().getRootPackageName();
        }
        return rootPackage;
    }
    
    /**
     * Get the name of the {@link NodeFactory} implementation for the woven model. This method takes
     * into account the output package set with {@link #setOutputPackage(String)}, i.e. it does not
     * just return {@link Backend#getNodeFactoryClassName()}.
     * 
     * @return the name of the (woven) node factory
     */
    public String getNodeFactoryClassName() {
        if (backend == null) {
            throw new IllegalStateException("backend property not set");
        }
        String nodeFactoryClassName = backend.getNodeFactoryClassName();
        if (outputPackage != null) {
            String rootPackage = getRootPackage();
            if (nodeFactoryClassName.startsWith(rootPackage + ".")) {
                nodeFactoryClassName = outputPackage + nodeFactoryClassName.substring(rootPackage.length());
            }
        }
        return nodeFactoryClassName;
    }
    
    public void weave() throws ModelWeaverException {
        if (classLoader == null) {
            throw new IllegalStateException("classLoader property not set");
        }
        if (processor == null) {
            throw new IllegalStateException("processor property not set");
        }
        if (backend == null) {
            throw new IllegalStateException("backend property not set");
        }
        if (frontends == null) {
            throw new IllegalStateException("frontends property not set");
        }
        Reactor reactor = new Reactor(classLoader);
        reactor.addPlugin(new JSR45Plugin());
        ModelExtensionPlugin modelExtensionPlugin = new ModelExtensionPlugin();
        modelExtensionPlugin.addRequiredImplementation(new ClassRef(CoreDocument.class));
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
        // TODO: shouldn't these plugins be registered last??
//        reactor.addPlugin(DumpPlugin.INSTANCE);
        reactor.addPlugin(VerifierPlugin.INSTANCE);
        reactor.addPlugin(modelExtensionPlugin);
        InjectionPlugin injectionPlugin = new InjectionPlugin();
        // TODO: clean this up
        ModelExtension modelExtension = frontends.values().iterator().next().getModelExtension();
        if (modelExtension == null) {
            injectionPlugin.addBinding(ModelExtension.class.getName(), null);
        } else {
            String modelExtensionClass = modelExtension.getClass().getName();
            injectionPlugin.addBinding(ModelExtension.class.getName(), new PrototypeInjector(modelExtensionClass));
        }
        reactor.addPlugin(injectionPlugin);
        if (outputPackage != null) {
            reactor.addPlugin(new RemapperPlugin(backend.getWeavableClasses().getRootPackageName(), outputPackage));
        }
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
