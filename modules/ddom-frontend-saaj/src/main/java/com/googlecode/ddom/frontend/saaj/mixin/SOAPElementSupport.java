/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ChildIterator;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.ElementMatcher;
import com.googlecode.ddom.core.IdentityMapper;
import com.googlecode.ddom.core.Selector;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.support.DOM2AttributeMatcher;
import com.googlecode.ddom.frontend.dom.support.Policies;
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
    public final void ensureNamespaceIsDeclared(String prefix, String namespaceURI) throws SOAPException {
        try {
            String existingNamespaceURI = coreLookupNamespaceURI(prefix, true);
            if (!namespaceURI.equals(existingNamespaceURI)) {
                coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
            }
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

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

    public final SOAPElement addChildElement(String localName, String prefix) throws SOAPException {
        String namespaceURI = lookupNamespaceURI(prefix);
        if (namespaceURI == null) {
            throw new SOAPException("The prefix " + prefix + " is not bound");
        }
        return internalAddChildElement(namespaceURI, localName, prefix);
    }

    public final SOAPElement addChildElement(String localName, String prefix, String uri) throws SOAPException {
        return internalAddChildElement(uri == null ? "" : uri, localName, prefix == null ? "" : prefix);
    }
    
    private SAAJSOAPElement internalAddChildElement(String namespaceURI, String localName, String prefix) throws SOAPException {
        try {
            Class<? extends SAAJSOAPElement> childType = getChildType();
            Class<?> extensionInterface = SAAJModelExtension.INSTANCE.mapElement(namespaceURI, localName);
            if (extensionInterface != null && childType.isAssignableFrom(extensionInterface)) {
                childType = extensionInterface.asSubclass(childType);
            }
            SAAJSOAPElement element;
            if (childType.equals(SAAJSOAPElement.class)) {
                element = (SAAJSOAPElement)coreAppendElement(namespaceURI, localName, prefix);
            } else {
                element = coreAppendElement(childType, namespaceURI, localName, prefix);
            }
            if (namespaceURI.length() > 0) {
                element.ensureNamespaceIsDeclared(prefix, namespaceURI);
            }
            return element;
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
    
    public final void removeContents() {
        try {
            coreClear();
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    private SOAPElement internalAddAttribute(String namespaceURI, String localName, String prefix, String value) throws SOAPException {
        try {
            coreSetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI, localName, prefix, value);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
        if (namespaceURI.length() > 0) {
            ensureNamespaceIsDeclared(prefix, namespaceURI);
        }
        return this;
    }
    
    public final SOAPElement addAttribute(Name name, String value) throws SOAPException {
        return internalAddAttribute(name.getURI(), name.getLocalName(), name.getPrefix(), value);
    }

    public final SOAPElement addAttribute(QName qname, String value) throws SOAPException {
        return internalAddAttribute(qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix(), value);
    }

    public final boolean removeAttribute(Name name) {
        try {
            return coreRemoveAttribute(DOM2AttributeMatcher.INSTANCE, name.getURI(), name.getLocalName());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public final boolean removeAttribute(QName qname) {
        try {
            return coreRemoveAttribute(DOM2AttributeMatcher.INSTANCE, qname.getNamespaceURI(), qname.getLocalPart());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public final SOAPElement addNamespaceDeclaration(String prefix, String uri) throws SOAPException {
        try {
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, uri);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
        return this;
    }

    public final boolean removeNamespaceDeclaration(String prefix) {
        try {
            return coreRemoveAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    private String internalGetAttributeValue(String namespaceURI, String localName) {
        try {
            // TODO: we should really have a coreGetAttributeValue method
            CoreAttribute attr = coreGetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI, localName);
            return attr == null ? null : attr.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }
    
    public final String getAttributeValue(Name name) {
        return internalGetAttributeValue(name.getURI(), name.getLocalName());
    }
    
    public final String getAttributeValue(QName qname) {
        return internalGetAttributeValue(qname.getNamespaceURI(), qname.getLocalPart());
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
        } catch (CoreModelException ex) {
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
    
    public final QName createQName(String localName, String prefix) throws SOAPException {
        try {
            String namespaceURI = coreLookupNamespaceURI(prefix, true);
            if (namespaceURI == null) {
                throw new SOAPException("Unable to locate namespace prefix '" + prefix + "'");
            } else {
                return new QName(namespaceURI, localName, prefix);
            }
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final Name getElementName() {
        try {
            return new NameImpl(coreGetLocalName(), coreGetPrefix(), coreGetNamespaceURI());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }
    
    public final QName getElementQName() {
        try {
            return coreGetQName();
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    // May be overridden by other mixins!
    public Class<? extends SAAJSOAPElement> getChildType() {
        return SAAJSOAPElement.class;
    }
    
    private Iterator getChildElements(ChildIterator<?> childIterator) {
        Class<? extends SAAJSOAPElement> childType = getChildType();
        if (childType.equals(SAAJSOAPElement.class)) {
            // The iterator actually returns SOAPElements
            return childIterator;
        } else {
            return new ReifyingIterator(childIterator, childType);
        }
    }
    
    public final Iterator getChildElements() {
        return getChildElements(coreGetNodes(Axis.CHILDREN, Selector.ANY, CoreChildNode.class));
    }

    public final Iterator getChildElements(Name name) {
        return getChildElements(coreGetElements(Axis.CHILDREN, CoreNSAwareElement.class, ElementMatcher.BY_QNAME, name.getURI(), name.getLocalName()));
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
