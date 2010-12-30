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
package com.google.code.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.core.CoreAttribute;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNode;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.AbortNormalizationException;
import com.google.code.ddom.frontend.dom.intf.DOMCoreChildNode;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentType;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentTypeDeclaration;
import com.google.code.ddom.frontend.dom.intf.DOMParentNode;
import com.google.code.ddom.frontend.dom.intf.NormalizationConfig;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.ElementsByTagName;
import com.google.code.ddom.frontend.dom.support.ElementsByTagNameNS;
import com.google.code.ddom.frontend.dom.support.NodeUtil;
import com.google.code.ddom.frontend.dom.support.Policies;

@Mixin({CoreElement.class, CoreAttribute.class, CoreDocument.class, CoreDocumentFragment.class})
public abstract class ParentNodeSupport implements DOMParentNode {
    public final boolean hasChildNodes() {
        // TODO: not the best way if content is optimized
        return getFirstChild() != null;
    }
    
    public final Node getFirstChild() {
        try {
            return NodeUtil.toDOM(coreGetFirstChild());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final Node getLastChild() {
        try {
            return (Node)coreGetLastChild();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final NodeList getChildNodes() {
        return this;
    }
    
    public final int getLength() {
        try {
            return coreGetChildCount();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final Node item(int index) {
        try {
            // TODO: need unit test to check that this works when parsing is deferred
            // TODO: wrong result for negavite indexes
            CoreChildNode node = coreGetFirstChild();
            for (int i=0; i<index && node != null; i++) {
                node = node.coreGetNextSibling();
            }
            return NodeUtil.toDOM(node);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    private CoreNode toCore(Node node) {
        if (node instanceof DOMDocumentType) {
            DOMDocumentType doctype = (DOMDocumentType)node;
            DOMDocumentTypeDeclaration declaration = doctype.getDeclaration();
            if (declaration == null) {
                declaration = doctype.attach((DOMDocument)coreGetOwnerDocument(true));
            }
            return declaration;
        } else {
            return (CoreNode)node;
        }
    }
    
    public final Node appendChild(Node newChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        try {
            CoreNode coreNewChild = toCore(newChild);
            if (coreNewChild instanceof CoreChildNode) {
                coreAppendChild((CoreChildNode)coreNewChild, Policies.NODE_MIGRATION_POLICY);
            } else if (coreNewChild instanceof CoreDocumentFragment) {
                coreAppendChildren((CoreDocumentFragment)coreNewChild);
            } else {
                throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return newChild;
    }

    // TODO: apparently this is not tested with a DocumentType as newChild
    public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        try {
            // Note: The specification of the insertBefore method says that "if refChild
            // is null, insert newChild at the end of the list of children". That is, in this
            // case the behavior is identical to appendChild. (This is covered by the DOM 1
            // test suite)
            if (refChild == null) {
                appendChild(newChild);
            } else if (refChild.getParentNode() != this) {
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
            } else if (newChild instanceof CoreChildNode) {
                ((CoreChildNode)toCore(refChild)).coreInsertSiblingBefore((CoreChildNode)toCore(newChild));
            } else if (newChild instanceof CoreDocumentFragment) {
                ((CoreChildNode)toCore(refChild)).coreInsertSiblingsBefore((CoreDocumentFragment)newChild);
            } else {
                throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return newChild;
    }

    public final Node removeChild(Node oldChild) throws DOMException {
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        if (oldChild.getParentNode() == this) {
            try {
                ((CoreChildNode)toCore(oldChild)).coreDetach();
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
            return oldChild;
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
    }

    public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        if (oldChild.getParentNode() == this) {
            try {
                CoreNode coreNewChild = toCore(newChild);
                if (coreNewChild instanceof CoreChildNode) {
                    ((CoreChildNode)toCore(oldChild)).coreReplaceWith((CoreChildNode)coreNewChild);
                } else if (coreNewChild instanceof CoreDocumentFragment) {
                    ((CoreChildNode)toCore(oldChild)).coreReplaceWith((CoreDocumentFragment)coreNewChild);
                } else {
                    throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
                }
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
            return oldChild;
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
    }

    public final Node deepClone() {
        try {
            Node clone = shallowClone();
            DOMCoreChildNode child = (DOMCoreChildNode)coreGetFirstChild();
            while (child != null) {
                clone.appendChild(child.cloneNode(true));
                child = (DOMCoreChildNode)child.coreGetNextSibling();
            }
            return clone;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final NodeList getElementsByTagName(String tagname) {
        return new ElementsByTagName((DOMDocument)coreGetOwnerDocument(true), this, tagname);
    }

    public final NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return new ElementsByTagNameNS((DOMDocument)coreGetOwnerDocument(true), this, namespaceURI, localName);
    }
    
    public void normalizeChildren(NormalizationConfig config) throws AbortNormalizationException {
        try {
            CoreChildNode child = coreGetFirstChild();
            while (child != null) {
                NodeUtil.toDOM(child).normalize(config);
                child = child.coreGetNextSibling();
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
}
