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
package com.google.code.ddom.frontend.dom.support;

import org.w3c.dom.DOMImplementation;

import com.google.code.ddom.frontend.APIObjectFactory;
import com.googlecode.ddom.core.NodeFactory;

public class APIObjectFactoryImpl implements APIObjectFactory {
    private final DOMImplementation domImplementation;
    
    public APIObjectFactoryImpl(NodeFactory nodeFactory) {
        domImplementation = new DOMImplementationImpl(nodeFactory);
    }

    public Object getAPIObject(Class<?> clazz) {
        if (clazz.equals(DOMImplementation.class)) {
            return domImplementation;
        } else {
            return null;
        }
    }
}
