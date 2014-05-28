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
package com.googlecode.ddom.frontend.axiom.soap.mixin;

import javax.xml.namespace.QName;

import org.apache.axiom.soap.SOAPConstants;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axiom.soap.SOAPVersion;

import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPHeaderBlock;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPVersionEx;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionTranslator;
import com.googlecode.ddom.frontend.axiom.support.Policies;

@Mixin(AxiomSOAPHeaderBlock.class)
public abstract class SOAPHeaderBlockSupport implements AxiomSOAPHeaderBlock {
    private boolean processed;
    
    public final SOAPVersion getVersion() {
        return getSOAPVersionEx().getSOAPVersion();
    }

    private void setAttributeValue(QName qname, String value) {
        try {
            coreSetAttribute(Policies.ATTRIBUTE_MATCHER, qname.getNamespaceURI(), qname.getLocalPart(), SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX, value);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public final String getRole() {
        return getAttributeValue(getSOAPVersionEx().getSOAPVersion().getRoleAttributeQName());
    }

    public final void setRole(String roleURI) {
        setAttributeValue(getSOAPVersionEx().getSOAPVersion().getRoleAttributeQName(), roleURI);
    }

    public final boolean getMustUnderstand() throws SOAPProcessingException {
        try {
            CoreAttribute attr = coreGetAttribute(Policies.ATTRIBUTE_MATCHER, getSOAPVersionEx().getEnvelopeURI(), SOAPConstants.ATTR_MUSTUNDERSTAND);
            if (attr == null) {
                return false;
            } else {
                String value = attr.coreGetTextContent(TextCollectorPolicy.DEFAULT);
                if (value.equals(SOAPConstants.ATTR_MUSTUNDERSTAND_TRUE) || value.equals(SOAPConstants.ATTR_MUSTUNDERSTAND_1)) {
                    return true;
                } else if (value.equals(SOAPConstants.ATTR_MUSTUNDERSTAND_FALSE) || value.equals(SOAPConstants.ATTR_MUSTUNDERSTAND_0)) {
                    return false;
                } else {
                    throw new SOAPProcessingException("Invalid value for mustUnderstand attribute");
                }
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final void setMustUnderstand(boolean mustUnderstand) {
        SOAPVersionEx version = getSOAPVersionEx();
        setAttributeValue(version.getMustUnderstandQName(), version.formatMustUnderstand(mustUnderstand));
    }

    public final void setMustUnderstand(String mustUnderstand) throws SOAPProcessingException {
        if (SOAPConstants.ATTR_MUSTUNDERSTAND_TRUE.equals(mustUnderstand) ||
                SOAPConstants.ATTR_MUSTUNDERSTAND_FALSE.equals(mustUnderstand) ||
                SOAPConstants.ATTR_MUSTUNDERSTAND_0.equals(mustUnderstand) ||
                SOAPConstants.ATTR_MUSTUNDERSTAND_1.equals(mustUnderstand)) {
            setAttributeValue(getSOAPVersionEx().getMustUnderstandQName(), mustUnderstand);
        } else {
            throw new SOAPProcessingException("mustUnderstand must be one of \"true\", \"false\", \"0\" or \"1\"");
        }
    }

    public boolean getRelay() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setRelay(boolean relay) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean isProcessed() {
        return processed;
    }

    public final void setProcessed() {
        processed = true;
    }
}
