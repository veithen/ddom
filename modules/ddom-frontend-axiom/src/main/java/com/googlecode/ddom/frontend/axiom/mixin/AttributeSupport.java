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
package com.googlecode.ddom.frontend.axiom.mixin;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomAttribute;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.googlecode.ddom.util.lang.ObjectUtils;

@Mixin(CoreNSAwareAttribute.class)
public abstract class AttributeSupport implements AxiomAttribute {
    public final String getAttributeValue() {
        try {
            return coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final void setAttributeValue(String value) {
        try {
            coreSetValue(value);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final String getAttributeType() {
        return coreGetType();
    }

    public final void setAttributeType(String value) {
        coreSetType(value);
    }

    public void setOMNamespace(OMNamespace omNamespace) {
        setNamespace(omNamespace);
    }

    public final OMElement getOwner() {
        return (AxiomElement)coreGetOwnerElement();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof OMAttribute) {
            try {
                OMAttribute other = (OMAttribute)obj;
                // TODO: using getNamespace is not efficient because it will create a new OMNamespace instance
                return coreGetLocalName().equals(other.getLocalName())
                        && ObjectUtils.equals(getNamespace(), other.getNamespace())
                        && getAttributeValue().equals(other.getAttributeValue());
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        } else {
            return false;
        }
    }

    public final int hashCode() {
        try {
            String value = getAttributeValue();
            // TODO: using getNamespace is not efficient because it will create a new OMNamespace instance
            OMNamespace namespace = getNamespace();
            return coreGetLocalName().hashCode() ^ (value != null ? value.hashCode() : 0) ^
                    (namespace != null ? namespace.hashCode() : 0);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
}
