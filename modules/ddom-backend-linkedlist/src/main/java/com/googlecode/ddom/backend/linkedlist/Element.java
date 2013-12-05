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
package com.googlecode.ddom.backend.linkedlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLElement;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.backend.linkedlist.support.AttributesByTypeIterator;
import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.ChildNotAllowedException;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.CoreNamespaceDeclaration;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.Mapper;
import com.googlecode.ddom.core.NodeInUseException;
import com.googlecode.ddom.core.NodeMigrationException;
import com.googlecode.ddom.core.NodeMigrationPolicy;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.core.WrongDocumentException;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlSource;

public abstract class Element extends Container implements LLElement {
    private Attribute firstAttribute;

    public Element(Document document, boolean complete) {
        super(document, complete ? Flags.STATE_EXPANDED : Flags.STATE_ATTRIBUTES_PENDING);
    }

    final void setFirstAttribute(Attribute firstAttribute) {
        this.firstAttribute = firstAttribute;
    }

    public final void internalValidateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildNotAllowedException {
        // TODO: need a test case here!
        if (newChild instanceof CoreDocumentTypeDeclaration) {
            throw new ChildNotAllowedException();
        }
    }

    private boolean attributesBuilt() {
        int state = internalGetState();
        return state != Flags.STATE_SOURCE_SET && state != Flags.STATE_ATTRIBUTES_PENDING;
    }
    
    public final CoreAttribute coreGetFirstAttribute() throws DeferredParsingException {
        if (firstAttribute == null && !attributesBuilt()) {
            InputContext context = internalGetOrCreateInputContext();
            while (firstAttribute == null && internalGetState() == Flags.STATE_ATTRIBUTES_PENDING) {
                context.next(false);
            }
        }
        return firstAttribute;
    }
    
    final Attribute internalGetFirstAttributeIfMaterialized() {
        return firstAttribute;
    }
    
    public final CoreAttribute coreGetLastAttribute() throws DeferredParsingException {
        if (!attributesBuilt()) {
            InputContext context = internalGetOrCreateInputContext();
            while (internalGetState() == Flags.STATE_ATTRIBUTES_PENDING) {
                context.next(false);
            }
        }
        Attribute previousAttribute = null;
        Attribute attribute = firstAttribute;
        while (attribute != null) {
            previousAttribute = attribute;
            attribute = attribute.internalGetNextAttributeIfMaterialized();
        }
        return previousAttribute;
    }

    public final CoreAttribute coreGetAttribute(AttributeMatcher matcher, String namespaceURI, String name) throws DeferredParsingException {
        // TODO: can optimize this when attribute creation is deferred (get InputContext only once)
        CoreAttribute attr = coreGetFirstAttribute();
        while (attr != null && !matcher.matches(attr, namespaceURI, name)) {
            attr = attr.coreGetNextAttribute();
        }
        return attr;
    }

    public final void coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, String prefix, String value) throws DeferredParsingException {
        Attribute attr = firstAttribute; // TODO: coreGetFirstAttribute() ??
        Attribute previousAttr = null;
        while (attr != null && !matcher.matches(attr, namespaceURI, name)) {
            previousAttr = attr;
            // TODO: avoid cast here
            attr = (Attribute)attr.coreGetNextAttribute();
        }
        if (attr == null) {
            CoreDocument document = internalGetOwnerDocument(false);
            Attribute newAttr = (Attribute)matcher.createAttribute(coreGetNodeFactory(), document, namespaceURI, name, prefix, value);
            if (previousAttr == null) {
                internalAppendAttribute(newAttr);
            } else {
                previousAttr.insertAttributeAfter(newAttr);
            }
        } else {
            matcher.update(attr, prefix, value);
        }
    }
    
    private Attribute cloneAttribute(CoreAttribute attr) {
        if (attr instanceof CoreNSAwareAttribute) {
            CoreNSAwareAttribute org = ((CoreNSAwareAttribute)attr);
            try {
                // TODO: need a better way to clone the children
                return new NSAwareAttribute(null, org.coreGetNamespaceURI(), org.coreGetLocalName(), org.coreGetPrefix(), org.coreGetTextContent(TextCollectorPolicy.DEFAULT), org.coreGetType());
            } catch (DeferredBuildingException ex) {
                // TODO
                throw new RuntimeException(ex);
            }
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
    }

    private Attribute accept(CoreAttribute attr, NodeMigrationPolicy policy) throws NodeMigrationException {
        boolean hasParent = attr.coreHasOwnerElement();
        boolean isForeignDocument = !coreIsSameOwnerDocument(attr);
        boolean isForeignModel = attr.coreGetNodeFactory() != coreGetNodeFactory();
        if (hasParent || isForeignDocument || isForeignModel) {
            switch (policy.getAction(hasParent, isForeignDocument, isForeignModel)) {
                case REJECT:
                    if (isForeignDocument) {
                        // Note that since isForeignModel implies isForeignDocument, we also get here
                        // if isForeignModel is true.
                        throw new WrongDocumentException();
                    } else {
                        // We get here if isForeignDocument and isForeignModel are false. Since at least
                        // one of the three booleans must be true, this implies that hasParent is true.
                        throw new NodeInUseException();
                    }
                case MOVE:
                    if (isForeignDocument || isForeignModel) {
                        // TODO
                        throw new UnsupportedOperationException();
                    } else {
                        attr.coreRemove();
                        return (Attribute)attr;
                    }
                case CLONE:
                    // TODO: probably we need to distinguish between cloning an attribute from the same model and importing it from another model (does that actually ever occur?)
                    return cloneAttribute(attr);
                default:
                    // Should never get here unless new values are added to the enum
                    throw new IllegalStateException();
            }
        } else {
            return (Attribute)attr;
        }
    }
    
    public final CoreAttribute coreSetAttribute(AttributeMatcher matcher, CoreAttribute coreAttr, NodeMigrationPolicy policy, ReturnValue returnValue) throws NodeMigrationException, DeferredParsingException {
        if (coreAttr.coreGetOwnerElement() == this) {
            // TODO: document this and add assertion
            // TODO: take returnValue into account
            return coreAttr;
        }
        Attribute attr = accept(coreAttr, policy);
        String namespaceURI = matcher.getNamespaceURI(attr);
        String name = matcher.getName(attr); 
        Attribute existingAttr = firstAttribute;
        Attribute previousAttr = null;
        while (existingAttr != null && !matcher.matches(existingAttr, namespaceURI, name)) {
            previousAttr = existingAttr;
            // TODO: get rid of cast here!
            existingAttr = (Attribute)existingAttr.coreGetNextAttribute();
        }
        attr.setOwnerElement(this);
        if (existingAttr == null) {
            if (previousAttr == null) {
                firstAttribute = attr;
            } else {
                previousAttr.setNextAttribute(attr);
            }
        } else {
            if (previousAttr == null) {
                firstAttribute = attr;
            } else {
                previousAttr.setNextAttribute(attr);
            }
            existingAttr.setOwnerElement(null);
            // TODO: get rid of cast here!
            attr.setNextAttribute((Attribute)existingAttr.coreGetNextAttribute());
            existingAttr.setNextAttribute(null);
        }
        switch (returnValue) {
            case ADDED_ATTRIBUTE: return attr;
            case REPLACED_ATTRIBUTE: return existingAttr;
            default: return null;
        }
    }

    public final void coreAppendAttribute(CoreAttribute attr, NodeMigrationPolicy policy) throws NodeMigrationException, DeferredParsingException {
        // TODO: we should probably check if the attribute is already owned by the element
        internalAppendAttribute(accept(attr, policy));
    }

    final void internalAppendAttribute(Attribute attr) throws DeferredParsingException {
        // TODO: throw exception if attribute already has an owner (see also coreInsertAttributeAfter)
        attr.setOwnerElement(this);
        if (firstAttribute == null) {
            firstAttribute = attr;
        } else {
            // TODO: avoid cast here
            ((Attribute)coreGetLastAttribute()).insertAttributeAfter(attr);
        }
    }

    public final boolean coreRemoveAttribute(AttributeMatcher matcher, String namespaceURI, String name) throws DeferredParsingException {
        CoreAttribute att = coreGetAttribute(matcher, namespaceURI, name);
        if (att != null) {
            att.coreRemove();
            return true;
        } else {
            return false;
        }
    }

    protected abstract String getImplicitNamespaceURI(String prefix);
    
    public final String coreLookupNamespaceURI(String prefix, boolean strict) throws DeferredBuildingException {
        if (!strict) {
            String namespaceURI = getImplicitNamespaceURI(prefix);
            if (namespaceURI != null) {
                return namespaceURI;
            }
        }
        for (CoreAttribute attr = coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
            if (attr instanceof CoreNamespaceDeclaration) {
                CoreNamespaceDeclaration decl = (CoreNamespaceDeclaration)attr;
                if (prefix.equals(decl.coreGetDeclaredPrefix())) {
                    return decl.coreGetDeclaredNamespaceURI();
                }
            }
        }
        CoreElement parentElement = coreGetParentElement();
        if (parentElement != null) {
            return parentElement.coreLookupNamespaceURI(prefix, strict);
        } else if (prefix.length() == 0) {
            return "";
        } else {
            return null;
        }
    }

    protected abstract String getImplicitPrefix(String namespaceURI);
    
    public final String coreLookupPrefix(String namespaceURI, boolean strict) throws DeferredBuildingException {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI must not be null");
        }
        if (!strict) {
            String prefix = getImplicitPrefix(namespaceURI);
            if (prefix != null) {
                return prefix;
            }
        }
        for (CoreAttribute attr = coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
            if (attr instanceof CoreNamespaceDeclaration) {
                CoreNamespaceDeclaration decl = (CoreNamespaceDeclaration)attr;
                if (decl.coreGetDeclaredNamespaceURI().equals(namespaceURI)) {
                    return decl.coreGetDeclaredPrefix();
                }
            }
        }
        CoreElement parentElement = coreGetParentElement();
        if (parentElement != null) {
            String prefix = parentElement.coreLookupPrefix(namespaceURI, strict);
            // The prefix declared on one of the ancestors may be masked by another
            // namespace declaration on this element (or one of its descendants).
            for (CoreAttribute attr = coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
                if (attr instanceof CoreNamespaceDeclaration) {
                    CoreNamespaceDeclaration decl = (CoreNamespaceDeclaration)attr;
                    if (decl.coreGetDeclaredPrefix().equals(prefix)) {
                        return null;
                    }
                }
            }
            return prefix;
        } else {
            return null;
        }
    }

    public final void coreCoalesce(boolean includeCDATASections) throws DeferredBuildingException {
        // TODO: clean up local variable names (text --> characterData)
        CoreDocument document = internalGetOwnerDocument(false);
        // TODO: using a collection here is very bad!!
        List<CoreCharacterData> textNodes = new ArrayList<CoreCharacterData>();
        CoreChildNode child = coreGetFirstChild();
        boolean forceReplace = false;
        while (true) {
            // Get the next child now, in case we detach the current child.
            CoreChildNode nextChild = child == null ? null : child.coreGetNextSibling();
            boolean isText;
            if (child == null) {
                isText = false;
            } else {
                if (child instanceof CoreCharacterData) {
                    isText = true;
                } else if (child instanceof CoreCDATASection) {
                    if (includeCDATASections) {
                        isText = true;
                        forceReplace = true;
                    } else {
                        isText = false;
                    }
                } else {
                    isText = false;
                }
            }
            if (isText) {
                CoreCharacterData textNode = (CoreCharacterData)child;
                if (textNode.coreGetData().length() == 0) {
                    textNode.coreDetach();
                } else {
                    textNodes.add(textNode);
                }
            } else {
                if (forceReplace || textNodes.size() > 1) {
                    StringBuilder buffer = new StringBuilder();
                    for (CoreCharacterData textNode : textNodes) {
                        buffer.append(textNode.coreGetData());
                    }
                    CoreCharacterData first = textNodes.get(0);
                    CoreCharacterData newTextNode = coreGetNodeFactory().createCharacterData(document, buffer.toString());
                    try {
                        first.coreInsertSiblingBefore(newTextNode, null); // TODO: don't use null here
                    } catch (CoreModelException ex) {
                        throw new Error(ex); // TODO: we should never get here
                    }
                    for (CoreCharacterData textNode : textNodes) {
                        textNode.coreDetach();
                    }
                    forceReplace = false;
                }
                textNodes.clear();
            }
            if (child == null) {
                break;
            }
            child = nextChild;
        }
    }

    public final <T extends CoreAttribute,S> Iterator<S> coreGetAttributesByType(Class<T> type, Mapper<T,S> mapper) {
        return new AttributesByTypeIterator<T,S>(this, type, mapper);
    }

    public final void internalGenerateEndEvent(XmlHandler handler) throws StreamException {
        handler.endElement();
    }

    @Override
    final LLParentNode shallowClone(ClonePolicy policy) {
        LLElement clone = shallowCloneWithoutAttributes(policy);
        // TODO: copy attributes
        return clone;
    }
    
    abstract LLElement shallowCloneWithoutAttributes(ClonePolicy policy);
}
