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
import com.google.code.ddom.backend.CoreDocumentTypeDeclaration;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNamespaceDeclaration;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.CoreTextNode;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.Mapper;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.backend.NodeInUseException;
import com.google.code.ddom.backend.NodeMigrationException;
import com.google.code.ddom.backend.NodeMigrationPolicy;
import com.google.code.ddom.backend.WrongDocumentException;
import com.google.code.ddom.backend.linkedlist.support.AttributesByTypeIterator;

@Implementation
public abstract class Element extends ParentNode implements ChildNode, CoreElement {
    private final Document document;
    private int children;
    private Attribute firstAttribute;

    public Element(Document document, boolean complete) {
        super(complete);
        this.document = document;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    final void setFirstAttribute(Attribute firstAttribute) {
        this.firstAttribute = firstAttribute;
    }

    @Override
    final void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException {
        // TODO: need a test case here!
        if (newChild instanceof CoreDocumentTypeDeclaration) {
            throw new ChildTypeNotAllowedException();
        }
    }

    public final int coreGetChildCount() throws DeferredParsingException {
        coreBuild();
        return children;
    }

    public final CoreAttribute coreGetFirstAttribute() {
        return firstAttribute;
    }
    
    public final Document internalGetDocument() {
        return document;
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
        Attribute attr = firstAttribute;
        Attribute previousAttr = null;
        while (attr != null && !matcher.matches(attr, namespaceURI, name)) {
            previousAttr = attr;
            // TODO: avoid cast here
            attr = (Attribute)attr.coreGetNextAttribute();
        }
        if (attr == null) {
            CoreDocument document = internalGetDocument();
            NodeFactory factory = document.getNodeFactory();
            Attribute newAttr = (Attribute)matcher.createAttribute(factory, document, namespaceURI, name, prefix, value);
            if (previousAttr == null) {
                appendAttribute(newAttr);
            } else {
                previousAttr.insertAttributeAfter(newAttr);
            }
        } else {
            matcher.update(attr, prefix, value);
        }
    }

    private Attribute accept(CoreAttribute coreAttr, NodeMigrationPolicy policy) throws NodeMigrationException {
        boolean hasParent = coreAttr.coreHasOwnerElement();
        boolean isForeignDocument = coreAttr.coreGetDocument() != internalGetDocument();
        boolean isForeignModel = !(coreAttr instanceof Attribute);
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
                        coreAttr.coreRemove();
                        return (Attribute)coreAttr;
                    }
                case CLONE:
                    // TODO
                    throw new UnsupportedOperationException();
                default:
                    // Should never get here unless new values are added to the enum
                    throw new IllegalStateException();
            }
        } else {
            return (Attribute)coreAttr;
        }
    }
    
    public final CoreAttribute coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, CoreAttribute coreAttr, NodeMigrationPolicy policy) throws NodeMigrationException {
        Attribute attr = accept(coreAttr, policy);
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
            return null;
        } else {
            if (previousAttr == null) {
                firstAttribute = attr;
            } else {
                previousAttr.setNextAttribute(attr);
            }
            existingAttr.setOwnerElement(null);
            attr.setNextAttribute(existingAttr.coreGetNextAttribute());
            existingAttr.setNextAttribute(null);
            return existingAttr;
        }
    }

    final void appendAttribute(Attribute attr) {
        // TODO: throw exception if attribute already has an owner (see also coreInsertAttributeAfter)
        attr.setOwnerElement(this);
        if (firstAttribute == null) {
            firstAttribute = attr;
        } else {
            // TODO: avoid cast here
            ((Attribute)coreGetLastAttribute()).insertAttributeAfter(attr);
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
        CoreDocument document = internalGetDocument();
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
