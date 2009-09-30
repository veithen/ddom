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
package com.google.code.ddom.dom.impl.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class NodeFactoryAspect {
    /**
     * Advice that checks that all nodes are created using a
     * {@link com.google.code.ddom.dom.impl.NodeFactory} implementation.
     */
    // TODO: for the moment we create DocumentImpl instances without going through a NodeFactory; this should be changed also
    @Before("execution(com.google.code.ddom.dom.impl.NodeImpl.new(..))" +
    		" && !cflow(execution(* com.google.code.ddom.dom.impl.NodeFactory+.*(..)))" +
    		" && !cflow(call(com.google.code.ddom.dom.impl.DocumentImpl.new(..)))")
    public void nodeCreatedOutsideFactory() {
        System.out.println("Node instance created by code not in a NodeFactory implementation!");
        new Throwable().printStackTrace(System.out);
    }
}
