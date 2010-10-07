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
package com.google.code.ddom.frontend.axiom.mixin;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareAttribute;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin(CoreNSAwareAttribute.class)
public abstract class AttributeSupport implements AxiomAttribute {
    public String getAttributeValue() {
        try {
            return coreGetTextContent();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void setAttributeValue(String value) {
        try {
            coreSetValue(value);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public String getAttributeType() {
        return coreGetType();
    }

    public void setAttributeType(String value) {
        coreSetType(value);
    }

    public void setOMNamespace(OMNamespace omNamespace) {
        setNamespace(omNamespace);
    }

    public OMElement getOwner() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMFactory getOMFactory() {
        return (OMFactory)coreGetDocument();
    }
}
