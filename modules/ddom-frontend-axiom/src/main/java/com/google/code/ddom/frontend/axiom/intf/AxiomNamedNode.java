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
package com.google.code.ddom.frontend.axiom.intf;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.backend.CoreNSAwareNamedNode;

/**
 * Defines methods common to {@link org.apache.axiom.om.OMElement} and
 * {@link org.apache.axiom.om.OMAttribute}.
 * 
 * @author Andreas Veithen
 */
public interface AxiomNamedNode extends CoreNSAwareNamedNode, AxiomInformationItem {
    /**
     * @see org.apache.axiom.om.OMElement#getLocalName()
     * @see org.apache.axiom.om.OMAttribute#getLocalName()
     */
    String getLocalName();

    /**
     * @see org.apache.axiom.om.OMElement#setLocalName(String)
     * @see org.apache.axiom.om.OMAttribute#setLocalName(String)
     */
    void setLocalName(String localName);

    /**
     * @see org.apache.axiom.om.OMElement#getNamespace()
     * @see org.apache.axiom.om.OMAttribute#getNamespace()
     */
    OMNamespace getNamespace();

    /**
     * @see org.apache.axiom.om.OMElement#setNamespace(OMNamespace)
     * @see org.apache.axiom.om.OMAttribute#setOMNamespace(OMNamespace)
     */
    void setNamespace(OMNamespace omNamespace);

    /**
     * @see org.apache.axiom.om.OMElement#getQName()
     * @see org.apache.axiom.om.OMAttribute#getQName()
     */
    QName getQName();
}
