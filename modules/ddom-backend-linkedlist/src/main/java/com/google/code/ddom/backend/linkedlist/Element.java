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
package com.google.code.ddom.backend.linkedlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.code.ddom.backend.AttributeMatcher;
import com.google.code.ddom.backend.ChildTypeNotAllowedException;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreCDATASection;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentType;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNamespaceDeclaration;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.CoreTextNode;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.Mapper;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.backend.NodeNotFoundException;

@Implementation
public abstract class Element extends ParentNode implements CoreElement {
    private final Document document;
    private Object content;
    private boolean complete;
    private int children;
    private CoreParentNode parent;
    private CoreChildNode nextSibling;
    private CoreAttribute firstAttribute;

    public Element(Document document, boolean complete) {
        this.document = document;
        this.complete = complete;
    }

    public final void internalSetParent(CoreParentNode parent) {
        this.parent = parent;
    }
    
    public final CoreChildNode internalGetNextSibling() {
        return nextSibling;
    }

    public final void internalSetNextSibling(CoreChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final boolean coreIsComplete() {
        return complete;
    }

    public final void coreBuild() throws DeferredParsingException {
        BuilderTargetHelper.coreBuild(this);
    }

    public final void internalSetComplete() {
        complete = true;
    }
    
    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected final void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException {
        // TODO: need a test case here!
        if (newChild instanceof CoreDocumentType) {
            throw new ChildTypeNotAllowedException();
        }
    }

    public final int coreGetChildCount() throws DeferredParsingException {
        coreBuild();
        return children;
    }

    public final Object coreGetContent() {
        return content;
    }
    
    public final void internalSetFirstChild(CoreChildNode child) {
        content = child;
    }

    public final void coreSetValue(String value) {
        // TODO: need to remove any existing children!
        this.content = value;
    }

    public final boolean coreIsExpanded() {
        return content instanceof CoreChildNode;
    }

    public final CoreChildNode coreGetFirstChild() throws DeferredParsingException {
        if (content == null && !complete) {
            document.next();
        }
        return CompactParentNodeHelper.getFirstChild(this);
    }
    
    public final CoreAttribute coreGetFirstAttribute() {
        return firstAttribute;
    }
    
    public final CoreDocument getDocument() {
        return document;
    }

    public final CoreParentNode coreGetParent() {
        return parent;
    }
    
    public final CoreElement coreGetParentElement() {
        return parent instanceof CoreElement ? (CoreElement)parent : null;
    }

    public final CoreChildNode coreGetNextSibling() throws DeferredParsingException {
        return ChildNodeHelper.coreGetNextSibling(this);
    }

    public final CoreChildNode coreGetPreviousSibling() throws DeferredParsingException {
        return ChildNodeHelper.coreGetPreviousSibling(this);
    }

    public final void coreInsertSiblingAfter(CoreNode sibling) throws CoreModelException {
        ChildNodeHelper.coreInsertSiblingAfter(this, sibling);
    }

    public final void coreInsertSiblingBefore(CoreNode sibling) throws CoreModelException {
        ChildNodeHelper.coreInsertSiblingBefore(this, sibling);
    }
    
    public final void coreDetach() throws DeferredParsingException {
        ChildNodeHelper.coreDetach(this);
    }

    public final CoreAttribute coreGetLastAttribute() {
        CoreAttribute previousAttribute = null;
        CoreAttribute attribute = firstAttribute;
        while (attribute != null) {
            previousAttribute = attribute;
            attribute = attribute.coreGetNextAttribute();
        }
        return previousAttribute;
    }

    public final CoreAttribute coreGetAttribute(AttributeMatcher matcher, String namespaceURI, String name) {
        CoreAttribute attr = firstAttribute;
        while (attr != null && !matcher.matches(attr, namespaceURI, name)) {
            attr = attr.coreGetNextAttribute();
        }
        return attr;
    }

    public final void coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, String prefix, String value) {
        CoreAttribute attr = firstAttribute;
        CoreAttribute previousAttr = null;
        while (attr != null && !matcher.matches(attr, namespaceURI, name)) {
            previousAttr = attr;
            attr = attr.coreGetNextAttribute();
        }
        if (attr == null) {
            CoreDocument document = getDocument();
            NodeFactory factory = document.getNodeFactory();
            CoreAttribute newAttr = matcher.createAttribute(factory, document, namespaceURI, name, prefix, value);
            if (previousAttr == null) {
                coreAppendAttribute(newAttr);
            } else {
                previousAttr.coreInsertAttributeAfter(newAttr);
            }
        } else {
            matcher.update(attr, prefix, value);
        }
    }

    public final CoreAttribute coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, CoreAttribute attr) {
        CoreAttribute existingAttr = firstAttribute;
        CoreAttribute previousAttr = null;
        while (existingAttr != null && !matcher.matches(existingAttr, namespaceURI, name)) {
            previousAttr = existingAttr;
            existingAttr = existingAttr.coreGetNextAttribute();
        }
        attr.internalSetOwnerElement(this);
        if (existingAttr == null) {
            if (previousAttr == null) {
                firstAttribute = attr;
            } else {
                previousAttr.internalSetNextAttribute(attr);
            }
            return null;
        } else {
            if (previousAttr == null) {
                firstAttribute = attr;
            } else {
                previousAttr.internalSetNextAttribute(attr);
            }
            existingAttr.internalSetOwnerElement(null);
            attr.internalSetNextAttribute(existingAttr.coreGetNextAttribute());
            existingAttr.internalSetNextAttribute(null);
            return existingAttr;
        }
    }

    // TODO: check if we still need this as public method
    public final void coreAppendAttribute(CoreAttribute attr) {
        // TODO: throw exception if attribute already has an owner (see also coreInsertAttributeAfter)
        attr.internalSetOwnerElement(this);
        if (firstAttribute == null) {
            firstAttribute = attr;
        } else {
            coreGetLastAttribute().coreInsertAttributeAfter(attr);
        }
    }

    public final void coreRemoveAttribute(CoreAttribute attr) throws NodeNotFoundException {
        if (attr.coreGetOwnerElement() == this) {
            CoreAttribute previousAttr = firstAttribute;
            while (previousAttr != null) {
                CoreAttribute nextAttr = previousAttr.coreGetNextAttribute();
                if (nextAttr == attr) {
                    break;
                }
                previousAttr = nextAttr;
            }
            attr.internalSetOwnerElement(null);
            if (previousAttr == null) {
                firstAttribute = attr.coreGetNextAttribute();
            } else {
                previousAttr.internalSetNextAttribute(attr.coreGetNextAttribute());
            }
        } else {
            throw new NodeNotFoundException();
        }
    }

    protected abstract String getImplicitNamespaceURI(String prefix);
    
    public final String coreLookupNamespaceURI(String prefix, boolean strict) throws DeferredParsingException {
        if (!strict) {
            String namespaceURI = getImplicitNamespaceURI(prefix);
            if (namespaceURI != null) {
                return namespaceURI;
            }
        }
        for (CoreAttribute attr = coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
            if (attr instanceof CoreNamespaceDeclaration) {
                CoreNamespaceDeclaration decl = (CoreNamespaceDeclaration)attr;
                String declaredPrefix = decl.coreGetDeclaredPrefix();
                if (prefix == null) {
                    if (declaredPrefix == null) {
                        return decl.coreGetDeclaredNamespaceURI();
                    }
                } else {
                    if (prefix.equals(declaredPrefix)) {
                        return decl.coreGetDeclaredNamespaceURI();
                    }
                }
            }
        }
        CoreElement parentElement = coreGetParentElement();
        if (parentElement != null) {
            return parentElement.coreLookupNamespaceURI(prefix, strict);
        } else {
            return null;
        }
    }

    protected abstract String getImplicitPrefix(String namespaceURI);
    
    public final String coreLookupPrefix(String namespaceURI, boolean strict) throws DeferredParsingException {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI must not be null");
        }
        if (!strict) {
            String prefix = getImplicitPrefix(namespaceURI);
            if (prefix != null) {
                return prefix;
            }
        }
        // TODO: this is not entirely correct because the namespace declaration for this prefix may be hidden by a namespace declaration in a nested scope; need to check if this is covered by the DOM3 test suite
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
            return parentElement.coreLookupPrefix(namespaceURI, strict);
        } else {
            return null;
        }
    }

    public final void coreCoalesce(boolean includeCDATASections) throws DeferredParsingException {
        CoreDocument document = getDocument();
        // TODO: using a collection here is very bad!!
        List<CoreTextNode> textNodes = new ArrayList<CoreTextNode>();
        CoreChildNode child = coreGetFirstChild();
        boolean forceReplace = false;
        while (true) {
            // Get the next child now, in case we detach the current child.
            CoreChildNode nextChild = child == null ? null : child.coreGetNextSibling();
            boolean isText;
            if (child == null) {
                isText = false;
            } else {
                if (child instanceof CoreText) {
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
                CoreTextNode textNode = (CoreTextNode)child;
                if (textNode.coreGetData().length() == 0) {
                    textNode.coreDetach();
                } else {
                    textNodes.add(textNode);
                }
            } else {
                if (forceReplace || textNodes.size() > 1) {
                    StringBuilder buffer = new StringBuilder();
                    for (CoreTextNode textNode : textNodes) {
                        buffer.append(textNode.coreGetData());
                    }
                    CoreTextNode first = textNodes.get(0);
                    CoreText newTextNode = document.getNodeFactory().createText(document, buffer.toString());
                    try {
                        first.coreInsertSiblingBefore(newTextNode);
                    } catch (CoreModelException ex) {
                        throw new Error(ex); // TODO: we should never get here
                    }
                    for (CoreTextNode textNode : textNodes) {
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
}
