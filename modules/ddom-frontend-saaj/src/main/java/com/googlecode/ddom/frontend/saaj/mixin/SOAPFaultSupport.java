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
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPFault;
import com.googlecode.ddom.frontend.saaj.support.SAAJExceptionUtil;

@Mixin(SAAJSOAPFault.class)
public abstract class SOAPFaultSupport implements SAAJSOAPFault {
    private CoreNSAwareElement getFaultCodeElement(boolean create) throws CoreModelException {
        return coreGetElementFromSequence(getSOAPVersion().getFaultSequence(), 0, create);
    }
    
    public String getFaultCode() {
        try {
            CoreNSAwareElement faultCodeElement = getFaultCodeElement(false);
            return faultCodeElement == null ? null : faultCodeElement.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public Name getFaultCodeAsName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public QName getFaultCodeAsQName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setFaultCode(Name arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setFaultCode(QName faultCode) throws SOAPException {
        try {
            CoreNSAwareElement faultCodeElement = getFaultCodeElement(true);
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
            CoreNSAwareElement faultCodeElement = getFaultCodeElement(true);
            faultCodeElement.coreSetValue(faultCode);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    private CoreNSAwareElement getFaultStringElement(boolean create) throws CoreModelException {
        return coreGetElementFromSequence(getSOAPVersion().getFaultSequence(), 1, create);
    }
    
    public final String getFaultString() {
        try {
            CoreNSAwareElement faultStringElement = getFaultStringElement(false);
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
            getFaultStringElement(true).coreSetValue(faultString);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }
    
    private CoreNSAwareElement getFaultActorElement(boolean create) throws CoreModelException {
        return coreGetElementFromSequence(getSOAPVersion().getFaultSequence(), 2, create);
    }
    
    public String getFaultActor() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setFaultActor(String faultActor) throws SOAPException {
        try {
            getFaultActorElement(true).coreSetValue(faultActor);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public Detail getDetail() {
        try {
            return (Detail)coreGetElementFromSequence(getSOAPVersion().getFaultSequence(), 3, false);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public Detail addDetail() throws SOAPException {
        try {
            return (Detail)coreCreateElementInSequence(getSOAPVersion().getFaultSequence(), 3);
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

    public String getFaultNode() {
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

    public void setFaultNode(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
