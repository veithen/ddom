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
package com.google.code.ddom.frontend.saaj.mixin;

import java.util.Iterator;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.core.Axis;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.ext.SOAPFaultExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPFault;

@Mixin(SOAPFaultExtension.class)
public abstract class SOAPFaultSupport implements SAAJSOAPFault {
    private CoreNSAwareElement getFaultCodeElement() {
        // TODO: we should have a method in the core model for this
        Iterator<CoreNSAwareElement> it = coreGetElementsByName(Axis.CHILDREN, getFaultSubElementsNamespaceURI(), getFaultCodeElementLocalName());
        return it.hasNext() ? it.next() : null;
    }
    
    public String getFaultCode() {
        // TODO
        throw new UnsupportedOperationException();
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

    public void setFaultCode(QName faultCode) throws SOAPException {
        try {
            CoreNSAwareElement faultCodeElement = getFaultCodeElement();
            if (faultCodeElement == null) {
                // TODO: prefix!
                faultCodeElement = coreGetDocument().coreCreateElement(getFaultSubElementsNamespaceURI(), getFaultCodeElementLocalName(), null);
                // TODO: order!
                coreAppendChild(faultCodeElement);
            }
            // TODO: incorrect; generate namespace declaration
            faultCodeElement.coreSetValue(faultCode.getPrefix() + ":" + faultCode.getLocalPart());
        } catch (CoreModelException ex) {
            throw new SOAPException(ex); // TODO
        }
    }

    public void setFaultCode(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Detail addDetail() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
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

    public Detail getDetail() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getFaultActor() {
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

    public String getFaultRole() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getFaultString() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Locale getFaultStringLocale() {
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

    public void setFaultActor(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setFaultNode(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setFaultRole(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setFaultString(String arg0, Locale arg1) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setFaultString(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
