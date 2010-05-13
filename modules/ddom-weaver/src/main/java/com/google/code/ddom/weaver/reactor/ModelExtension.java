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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.code.ddom.weaver.ModelWeaverException;

public class ModelExtension {
    private static final Logger log = Logger.getLogger(ModelExtension.class.getName());
    
    private final ClassInfo rootInterface;
    private final List<ClassInfo> extensionInterfaces = new ArrayList<ClassInfo>();
    private List<WeavableClassInfo> implementations;
    
    public ModelExtension(ClassInfo rootInterface) {
        this.rootInterface = rootInterface;
    }
    
    public void addExtensionInterface(ClassInfo classInfo) {
        extensionInterfaces.add(classInfo);
    }
    
    public void resolve(Reactor reactor) throws ModelWeaverException {
        implementations = reactor.getImplementations(rootInterface);
        if (implementations.isEmpty()) {
            throw new ModelWeaverException("No implementations found for root interface " + rootInterface);
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("Resolved model extension:\n  Root interface: " + rootInterface
                    + "\n  Extension interfaces: " + extensionInterfaces
                    + "\n  Implementations: " + implementations);
        }
    }
}
