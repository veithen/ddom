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
package com.googlecode.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMImplementation;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMNodeFactory;
import com.googlecode.ddom.frontend.dom.support.DOMImplementationImpl;

@Mixin(NodeFactory.class)
public abstract class NodeFactorySupport implements DOMNodeFactory {
    private final DOMImplementation domImplementation;
    
    public NodeFactorySupport() {
        domImplementation = new DOMImplementationImpl(this);
    }
    
    public final DOMImplementation getDOMImplementation() {
        return domImplementation;
    }
}