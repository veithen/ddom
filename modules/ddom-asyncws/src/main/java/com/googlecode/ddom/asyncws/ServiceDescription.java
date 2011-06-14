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
package com.googlecode.ddom.asyncws;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;

public class ServiceDescription {
    private final List<OperationDescription> operations = new ArrayList<OperationDescription>();
    private final JAXBContext jaxbContext;
    
    public ServiceDescription(Class<?> iface) throws ServiceDescriptionException {
        Set<Class<?>> contextClasses = new HashSet<Class<?>>();
        for (Method method : iface.getMethods()) {
            OperationDescription operation = new OperationDescription(method);
            operations.add(operation);
            contextClasses.add(operation.getRequestClass());
            contextClasses.add(operation.getResponseClass());
        }
        try {
            jaxbContext = JAXBContext.newInstance(contextClasses.toArray(new Class<?>[contextClasses.size()]));
        } catch (JAXBException ex) {
            throw new ServiceDescriptionException("Unable to create JAXBContext", ex);
        }
        JAXBIntrospector jaxbIntrospector = jaxbContext.createJAXBIntrospector();
        for (Class<?> contextClass : contextClasses) {
            Object instance;
            try {
                instance = contextClass.newInstance();
            } catch (Exception ex) {
                throw new ServiceDescriptionException("Class " + contextClass.getName() + " is not instantiable", ex);
            }
            jaxbIntrospector.getElementName(instance);
        }
    }
}
