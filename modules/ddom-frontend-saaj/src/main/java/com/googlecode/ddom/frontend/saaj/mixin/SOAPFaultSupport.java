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
package com.googlecode.ddom.frontend.saaj.mixin;

import java.util.Iterator;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.Sequence;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPFault;
import com.googlecode.ddom.frontend.saaj.support.NameFactory;
import com.googlecode.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;

@Mixin(SAAJSOAPFault.class)
public abstract class SOAPFaultSupport implements SAAJSOAPFault {
    private CoreNSAwareElement getFaultCodeElement() throws CoreModelException {
        SOAPVersion version = getSOAPVersion();
        CoreNSAwareElement faultCodeElement = coreGetElementFromSequence(version.getFaultSequence(), version.getFaultCodeIndex(), false);
        if (faultCodeElement == null) {
            return null;
        } else {
            Sequence faultCodeSequence = version.getFaultCodeSequence();
            if (faultCodeSequence == null) {
                return faultCodeElement;
            } else {
                return faultCodeElement.coreGetElementFromSequence(faultCodeSequence, 0, false);
            }
        }
    }
    
    public final String getFaultCode() {
        try {
            CoreNSAwareElement faultCodeElement = getFaultCodeElement();
            return faultCodeElement == null ? null : faultCodeElement.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public final Name getFaultCodeAsName() {
        return getFaultCode(NameFactory.NAME);
    }

    public final QName getFaultCodeAsQName() {
        return getFaultCode(NameFactory.QNAME);
    }
    
    private <T> T getFaultCode(NameFactory<T> nameFactory) {
        try {
            CoreNSAwareElement faultCodeElement = getFaultCodeElement();
            if (faultCodeElement == null) {
                return null;
            } else {
                String qname = faultCodeElement.coreGetTextContent(TextCollectorPolicy.DEFAULT);
                String prefix;
                String localName;
                int colonIndex = qname.indexOf(':');
                if (colonIndex == -1) {
                    prefix = "";
                    localName = qname;
                } else {
                    prefix = qname.substring(0, colonIndex);
                    localName = qname.substring(colonIndex+1);
                }
                String namespaceURI = faultCodeElement.coreLookupNamespaceURI(prefix, false);
                if (namespaceURI == null) {
                    return null;
                } else if (namespaceURI.length() == 0) {
                    return nameFactory.createName(localName);
                } else if (prefix.length() == 0) {
                    return nameFactory.createName(namespaceURI, localName);
                } else {
                    return nameFactory.createName(namespaceURI, localName, prefix);
                }
            }
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public void setFaultCode(Name arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setFaultCode(QName faultCode) throws SOAPException {
        try {
            SOAPVersion version = getSOAPVersion();
            CoreNSAwareElement faultCodeElement = coreGetElementFromSequence(version.getFaultSequence(), version.getFaultCodeIndex(), true);
            // TODO: incorrect; generate namespace declaration
            faultCodeElement.coreSetValue(faultCode.getPrefix() + ":" + faultCode.getLocalPart());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final void setFaultCode(String faultCode) throws SOAPException {
        try {
            int idx = faultCode.indexOf(':');
            // TODO: handle -1 case
            String prefix = faultCode.substring(0, idx);
            String namespaceURI = coreLookupNamespaceURI(prefix, false);
            if (namespaceURI == null) {
                throw new SOAPException("No NamespaceURI, SOAP requires faultcode content to be a QName");
            }
            SOAPVersion version = getSOAPVersion();
            CoreNSAwareElement faultCodeElement = coreGetElementFromSequence(version.getFaultSequence(), version.getFaultCodeIndex(), true);
            faultCodeElement.coreSetValue(faultCode);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final String getFaultString() {
        try {
            SOAPVersion version = getSOAPVersion();
            CoreNSAwareElement faultStringElement = coreGetElementFromSequence(version.getFaultSequence(), version.getFaultReasonIndex(), false);
            return faultStringElement == null ? null : faultStringElement.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public Locale getFaultStringLocale() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setFaultString(String arg0, Locale arg1) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setFaultString(String faultString) throws SOAPException {
        try {
            SOAPVersion version = getSOAPVersion();
            CoreNSAwareElement faultStringElement = coreGetElementFromSequence(version.getFaultSequence(), version.getFaultReasonIndex(), true);
            faultStringElement.coreSetValue(faultString);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }
    
    public final String getFaultNode() {
        try {
            SOAPVersion version = getSOAPVersion();
            int index = version.getFaultNodeIndex();
            if (index == -1) {
                throw new UnsupportedOperationException("setFaultNode not supported by this SOAP version");
            }
            CoreNSAwareElement faultNodeElement = coreGetElementFromSequence(version.getFaultSequence(), index, false);
            return faultNodeElement == null ? null : faultNodeElement.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public final void setFaultNode(String faultNode) throws SOAPException {
        try {
            SOAPVersion version = getSOAPVersion();
            int index = version.getFaultNodeIndex();
            if (index == -1) {
                throw new UnsupportedOperationException("setFaultNode not supported by this SOAP version");
            }
            CoreNSAwareElement faultNodeElement = coreGetElementFromSequence(version.getFaultSequence(), index, true);
            faultNodeElement.coreSetValue(faultNode);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public String getFaultActor() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setFaultActor(String faultActor) throws SOAPException {
        try {
            SOAPVersion version = getSOAPVersion();
            CoreNSAwareElement faultActorElement = coreGetElementFromSequence(version.getFaultSequence(), version.getFaultRoleIndex(), true);
            faultActorElement.coreSetValue(faultActor);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public Detail getDetail() {
        try {
            SOAPVersion version = getSOAPVersion();
            return (Detail)coreGetElementFromSequence(version.getFaultSequence(), version.getFaultDetailIndex(), false);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public Detail addDetail() throws SOAPException {
        try {
            SOAPVersion version = getSOAPVersion();
            return (Detail)coreCreateElementInSequence(version.getFaultSequence(), version.getFaultDetailIndex());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public void addFaultReasonText(String arg0, Locale arg1)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void appendFaultSubcode(QName arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getFaultReasonLocales() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getFaultReasonText(Locale arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getFaultReasonTexts() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getFaultSubcodes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean hasDetail() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void removeAllFaultSubcodes() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
