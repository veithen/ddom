/*
 * Copyright 2009-2012,2014 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.intf;

import java.util.Map;

import org.apache.axiom.om.OMElement;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;

public interface AxiomElement extends CoreNSAwareElement, OMElement, AxiomContainer, AxiomChildNode, AxiomNamedNode {
    /**
     * Check if a namespace declaration for the given namespace is in scope on this element and
     * optionally create one if no such namespace declaration is found.
     * 
     * @param prefix
     *            the namespace prefix, or <code>null</code> if any existing prefix bound to the
     *            given namespace URI should be used or a new one generated
     * @param namespaceURI
     *            the namespace URI
     * @param allowDefaultNamespace
     *            specifies whether it is allowed to reuse an existing namespace declaration for the
     *            default namespace; only taken into account if <code>prefix</code> is
     *            <code>null</code>
     * @param declare
     *            specifies whether a namespace declaration should be generated if necessary
     * @return the actual prefix: the value of <code>prefix</code> if it is not <code>null</code> or
     *         the existing/generated prefix if <code>prefix</code> is <code>null</code>
     * @throws CoreModelException
     */
    String checkNamespaceIsDeclared(String prefix, String namespaceURI, boolean allowDefaultNamespace, boolean declare) throws CoreModelException;
    
    Map<String,String> getNamespaceContextMap();
}
