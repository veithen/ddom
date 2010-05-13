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
package com.google.code.ddom.weaver;

import java.util.Collections;
import java.util.Map;

import com.google.code.ddom.backend.Backend;
import com.google.code.ddom.commons.cl.ClassRef;
import com.google.code.ddom.core.CoreCDATASection;
import com.google.code.ddom.core.CoreComment;
import com.google.code.ddom.core.CoreDocumentTypeDeclaration;
import com.google.code.ddom.core.CoreEntityReference;
import com.google.code.ddom.core.CoreNSAwareAttribute;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.CoreNSUnawareAttribute;
import com.google.code.ddom.core.CoreNSUnawareElement;
import com.google.code.ddom.core.CoreNamespaceDeclaration;
import com.google.code.ddom.core.CoreProcessingInstruction;
import com.google.code.ddom.core.CoreText;
import com.google.code.ddom.frontend.Frontend;
import com.google.code.ddom.weaver.reactor.Reactor;
import com.google.code.ddom.weaver.reactor.ReactorException;

public class ModelWeaver {
    private final ClassDefinitionProcessor processor;
    private final Reactor reactor;
    
    public ModelWeaver(ClassLoader classLoader, ClassDefinitionProcessor processor, Backend backend) throws ModelWeaverException {
        this.processor = processor;
        reactor = new Reactor(classLoader);
        try {
            reactor.addRequiredImplementation(new ClassRef(CoreCDATASection.class));
            reactor.addRequiredImplementation(new ClassRef(CoreComment.class));
            reactor.addRequiredImplementation(new ClassRef(CoreDocumentTypeDeclaration.class)); 
            reactor.addRequiredImplementation(new ClassRef(CoreEntityReference.class));
            reactor.addRequiredImplementation(new ClassRef(CoreNamespaceDeclaration.class));
            reactor.addRequiredImplementation(new ClassRef(CoreNSAwareAttribute.class));
            reactor.addRequiredImplementation(new ClassRef(CoreNSAwareElement.class));
            reactor.addRequiredImplementation(new ClassRef(CoreNSUnawareAttribute.class));
            reactor.addRequiredImplementation(new ClassRef(CoreNSUnawareElement.class));
            reactor.addRequiredImplementation(new ClassRef(CoreProcessingInstruction.class));
            reactor.addRequiredImplementation(new ClassRef(CoreText.class));
            for (ClassRef classRef : backend.getWeavableClasses().getClassRefs()) {
                reactor.loadWeavableClass(classRef);
            }
        } catch (ClassNotFoundException ex) {
            throw new ModelWeaverException(ex);
        }
    }

    public void weave(Map<String,Frontend> frontends) throws ModelWeaverException {
        try {
            for (Frontend frontend : frontends.values()) {
                for (ClassRef classRef : frontend.getMixins(Collections.unmodifiableMap(frontends)).getClassRefs()) {
                    reactor.loadMixin(classRef);
                }
                for (ClassRef classRef : frontend.getModelExtensionInterfaces().getClassRefs()) {
                    reactor.loadModelExtensionInterface(classRef);
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
