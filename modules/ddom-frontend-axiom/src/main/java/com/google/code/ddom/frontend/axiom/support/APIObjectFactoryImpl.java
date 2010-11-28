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
package com.google.code.ddom.frontend.axiom.support;

import org.apache.axiom.om.OMFactory;

import com.google.code.ddom.frontend.APIObjectFactory;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;

public class APIObjectFactoryImpl implements APIObjectFactory {
    private final AxiomNodeFactory nodeFactory;
    
    public APIObjectFactoryImpl(AxiomNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public Object getAPIObject(Class<?> clazz) {
        if (clazz.equals(OMFactory.class)) {
            return nodeFactory.getOMFactory();
        } else {
            return null;
        }
    }
}