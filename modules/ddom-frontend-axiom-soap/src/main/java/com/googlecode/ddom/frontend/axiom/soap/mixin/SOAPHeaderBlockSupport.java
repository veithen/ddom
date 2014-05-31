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
    
    private boolean getBooleanAttributeValue(QName qname) {
        try {
            CoreAttribute attr = coreGetAttribute(Policies.ATTRIBUTE_MATCHER, qname.getNamespaceURI(), qname.getLocalPart());
            return attr == null ? false : getSOAPVersionEx().parseBoolean(attr.coreGetTextContent(TextCollectorPolicy.DEFAULT));
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
        return getBooleanAttributeValue(getSOAPVersionEx().getMustUnderstandQName());
    }

    public final void setMustUnderstand(boolean mustUnderstand) {
        SOAPVersionEx version = getSOAPVersionEx();
        setAttributeValue(version.getMustUnderstandQName(), version.formatBoolean(mustUnderstand));
    }

    public final void setMustUnderstand(String mustUnderstand) throws SOAPProcessingException {
        SOAPVersionEx version = getSOAPVersionEx();
        version.parseBoolean(mustUnderstand);
        setAttributeValue(version.getMustUnderstandQName(), mustUnderstand);
    }

    public final boolean getRelay() {
        SOAPVersionEx version = getSOAPVersionEx();
        QName relayQName = version.getRelayQName();
        if (relayQName == null) {
            throw new UnsupportedOperationException();
        } else {
            return getBooleanAttributeValue(relayQName);
        }
    }

    public final void setRelay(boolean relay) {
        SOAPVersionEx version = getSOAPVersionEx();
        QName relayQName = version.getRelayQName();
        if (relayQName == null) {
            throw new UnsupportedOperationException();
        } else {
            setAttributeValue(version.getRelayQName(), version.formatBoolean(relay));
        }
    }

    public final boolean isProcessed() {
        return processed;
    }

    public final void setProcessed() {
        processed = true;
    }
}
