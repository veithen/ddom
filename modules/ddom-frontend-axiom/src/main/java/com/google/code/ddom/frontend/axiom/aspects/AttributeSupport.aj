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
package com.google.code.ddom.frontend.axiom.aspects;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;

public aspect AttributeSupport {
    public String AxiomAttribute.getLocalName() {
        return coreGetLocalName();
    }

    public void AxiomAttribute.setLocalName(String localName) {
        coreSetLocalName(localName);
    }

    public String AxiomAttribute.getAttributeValue() {
        return coreGetValue();
    }

    public void AxiomAttribute.setAttributeValue(String value) {
        coreSetValue(value);
    }

    public String AxiomAttribute.getAttributeType() {
        return coreGetType();
    }

    public void AxiomAttribute.setAttributeType(String value) {
        coreSetType(value);
    }

    public void AxiomAttribute.setOMNamespace(OMNamespace omNamespace) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMNamespace AxiomAttribute.getNamespace() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public QName AxiomAttribute.getQName() {
        return coreGetQName();
    }

    public OMElement AxiomAttribute.getOwner() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
