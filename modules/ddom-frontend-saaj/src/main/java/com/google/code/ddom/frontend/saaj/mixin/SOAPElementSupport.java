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

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.core.AttributeMatcher;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.IdentityMapper;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.intf.SAAJNSAwareAttribute;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPElement;

@Mixin(CoreNSAwareElement.class)
public abstract class SOAPElementSupport implements SAAJSOAPElement {
    public SOAPElement addChildElement(Name name) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public SOAPElement addChildElement(QName qname) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addChildElement(String localName) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addChildElement(String localName, String prefix) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addChildElement(String localName, String prefix, String uri) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addChildElement(SOAPElement element) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void removeContents() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addTextNode(String text) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addAttribute(Name name, String value) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addAttribute(QName qname, String value) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addNamespaceDeclaration(String prefix, String uri) throws SOAPException {
        coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, uri);
        return this;
    }

    public String getAttributeValue(Name name) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String getAttributeValue(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    private static final IdentityMapper<SAAJNSAwareAttribute> attributeIdentityMapper = new IdentityMapper<SAAJNSAwareAttribute>();
    
    public Iterator getAllAttributes() {
        // TODO: this doesn't return namespace unaware attributes!
        // See AttributesAsName for the explanation of this
        return coreGetAttributesByType(SAAJNSAwareAttribute.class, attributeIdentityMapper);
    }

    public Iterator getAllAttributesAsQNames() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getNamespaceURI(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getNamespacePrefixes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getVisibleNamespacePrefixes() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName createQName(String localName, String prefix) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Name getElementName() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName getElementQName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean removeAttribute(Name name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean removeAttribute(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean removeNamespaceDeclaration(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getChildElements() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getChildElements(Name name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getChildElements(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String getEncodingStyle() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
