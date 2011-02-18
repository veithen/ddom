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

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.frontend.dom.support.DOM2AttributeMatcher;
import com.google.code.ddom.frontend.dom.support.Policies;
import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ChildIterator;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ElementMatcher;
import com.googlecode.ddom.core.IdentityMapper;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.saaj.SAAJModelExtension;
import com.googlecode.ddom.frontend.saaj.intf.SAAJNSAwareAttribute;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPElement;
import com.googlecode.ddom.frontend.saaj.support.NameImpl;
import com.googlecode.ddom.frontend.saaj.support.ReifyingIterator;
import com.googlecode.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.frontend.saaj.support.SAAJPolicies;
import com.googlecode.ddom.frontend.saaj.support.SAAJUtil;

@Mixin(CoreNSAwareElement.class)
public abstract class SOAPElementSupport implements SAAJSOAPElement {
    public final String getValue() {
        try {
            String value = coreGetTextContent(SAAJPolicies.GET_VALUE);
            return value.length() == 0 ? null : value;
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }
    
    public final void setValue(String value) {
        try {
            if (coreIsEmpty() || coreHasValue()) {
                coreSetValue(value);
            } else {
                CoreChildNode firstChild = coreGetFirstChild();
                if (!(firstChild instanceof CoreCharacterData) || firstChild.coreGetNextSibling() != null) {
                    throw new IllegalStateException("Can only use setValue on a SOAPElement that has a single child of type Text");
                }
                ((CoreCharacterData)firstChild).coreSetData(value);
            }
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public final SOAPElement addChildElement(Name name) throws SOAPException {
        // TODO: need unit test with empty prefix/namespace
        return internalAddChildElement(name.getURI(), name.getLocalName(), name.getPrefix());
    }
    
    public final SOAPElement addChildElement(QName qname) throws SOAPException {
        return internalAddChildElement(qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
    }

    public SOAPElement addChildElement(String localName) throws SOAPException {
        // From the Javadoc: "The new SOAPElement inherits any in-scope default namespace."
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addChildElement(String localName, String prefix) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPElement addChildElement(String localName, String prefix, String uri) throws SOAPException {
        return internalAddChildElement(uri == null ? "" : uri, localName, prefix == null ? "" : prefix);
    }
    
    private SOAPElement internalAddChildElement(String namespaceURI, String localName, String prefix) throws SOAPException {
        try {
            Class<? extends SAAJSOAPElement> childType = getChildType();
            Class<?> extensionInterface = SAAJModelExtension.INSTANCE.mapElement(namespaceURI, localName);
            if (extensionInterface != null && childType.isAssignableFrom(extensionInterface)) {
                childType = extensionInterface.asSubclass(childType);
            }
            if (childType.equals(SAAJSOAPElement.class)) {
                return (SOAPElement)coreAppendElement(namespaceURI, localName, prefix);
            } else {
                return coreAppendElement(childType, namespaceURI, localName, prefix);
            }
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final SOAPElement addChildElement(SOAPElement element) throws SOAPException {
        try {
            SAAJSOAPElement reifiedElement = SAAJUtil.reify((CoreNSAwareElement)element, getChildType());
            // TODO: probably we need to use another policy here
            coreAppendChild(reifiedElement, Policies.NODE_MIGRATION_POLICY);
            return reifiedElement;
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public void removeContents() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPElement addTextNode(String text) throws SOAPException {
        try {
            if (coreIsEmpty()) {
                coreSetValue(text);
            } else {
                coreAppendCharacterData(text);
            }
            return this;
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final SOAPElement addAttribute(Name name, String value) throws SOAPException {
        // TODO: this requires some unit tests
        coreSetAttribute(DOM2AttributeMatcher.INSTANCE, name.getURI(), name.getLocalName(), name.getPrefix(), value);
        return this;
    }

    public SOAPElement addAttribute(QName qname, String value) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement addNamespaceDeclaration(String prefix, String uri) throws SOAPException {
        coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, uri);
        return this;
    }

    public final String getAttributeValue(Name name) {
        try {
            // TODO: we should really have a coreGetAttributeValue method
            CoreAttribute attr = coreGetAttribute(DOM2AttributeMatcher.INSTANCE, name.getURI(), name.getLocalName());
            return attr == null ? null : attr.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
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

    public final String getNamespaceURI(String prefix) {
        try {
            return coreLookupNamespaceURI(prefix, true);
        } catch (DeferredParsingException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
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

    public final Name getElementName() {
        return new NameImpl(coreGetLocalName(), coreGetPrefix(), coreGetNamespaceURI());
    }
    
    public final QName getElementQName() {
        return coreGetQName();
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

    // May be overridden by other mixins!
    public Class<? extends SAAJSOAPElement> getChildType() {
        return SAAJSOAPElement.class;
    }
    
    private Iterator getChildElements(ChildIterator<CoreNSAwareElement> childIterator) {
        Class<? extends SAAJSOAPElement> childType = getChildType();
        if (childType.equals(SAAJSOAPElement.class)) {
            // The iterator actually returns SOAPElements
            return childIterator;
        } else {
            return new ReifyingIterator<SAAJSOAPElement>(childIterator, childType);
        }
    }
    
    public final Iterator getChildElements() {
        return getChildElements(coreGetChildrenByType(Axis.CHILDREN, CoreNSAwareElement.class));
    }

    public Iterator getChildElements(Name name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Iterator getChildElements(QName qname) {
        return getChildElements(coreGetElements(Axis.CHILDREN, CoreNSAwareElement.class, ElementMatcher.BY_QNAME, qname.getNamespaceURI(), qname.getLocalPart()));
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
