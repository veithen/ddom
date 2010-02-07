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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

public aspect AttributeSupport {
    public String AxiomAttribute.getAttributeValue() {
        try {
            return coreGetValue();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void AxiomAttribute.setAttributeValue(String value) {
        try {
            coreSetValue(value);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public String AxiomAttribute.getAttributeType() {
        return coreGetType();
    }

    public void AxiomAttribute.setAttributeType(String value) {
        coreSetType(value);
    }

    public void AxiomAttribute.setOMNamespace(OMNamespace omNamespace) {
        setNamespace(omNamespace);
    }

    public OMElement AxiomAttribute.getOwner() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
