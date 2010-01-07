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
package com.google.code.ddom.frontend.saaj.aspects;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPElement;

public aspect SOAPElementSupport {
    public SOAPElement SAAJSOAPElement.addChildElement(Name name) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public SOAPElement SAAJSOAPElement.addChildElement(QName qname) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addChildElement(String localName) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addChildElement(String localName, String prefix) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addChildElement(String localName, String prefix, String uri) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addChildElement(SOAPElement element) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void SAAJSOAPElement.removeContents() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addTextNode(String text) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addAttribute(Name name, String value) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addAttribute(QName qname, String value) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.addNamespaceDeclaration(String prefix, String uri) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String SAAJSOAPElement.getAttributeValue(Name name) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String SAAJSOAPElement.getAttributeValue(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator SAAJSOAPElement.getAllAttributes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator SAAJSOAPElement.getAllAttributesAsQNames() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String SAAJSOAPElement.getNamespaceURI(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator SAAJSOAPElement.getNamespacePrefixes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator SAAJSOAPElement.getVisibleNamespacePrefixes() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName SAAJSOAPElement.createQName(String localName, String prefix) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Name SAAJSOAPElement.getElementName() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName SAAJSOAPElement.getElementQName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement SAAJSOAPElement.setElementQName(QName newName) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean SAAJSOAPElement.removeAttribute(Name name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean SAAJSOAPElement.removeAttribute(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean SAAJSOAPElement.removeNamespaceDeclaration(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator SAAJSOAPElement.getChildElements() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator SAAJSOAPElement.getChildElements(Name name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator SAAJSOAPElement.getChildElements(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void SAAJSOAPElement.setEncodingStyle(String encodingStyle) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String SAAJSOAPElement.getEncodingStyle() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
