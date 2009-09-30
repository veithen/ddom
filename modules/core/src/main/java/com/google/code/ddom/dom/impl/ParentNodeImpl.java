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
package com.google.code.ddom.dom.impl;

import java.util.Iterator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;
import com.google.code.ddom.utils.dom.iterator.ElementLocalNameFilterIterator;
import com.google.code.ddom.utils.dom.iterator.ElementNameFilterIterator;
import com.google.code.ddom.utils.dom.iterator.ElementNamespaceFilterIterator;
import com.google.code.ddom.utils.dom.iterator.FilterIterator;

public abstract class ParentNodeImpl extends NodeImpl implements ParentNode, NodeList {
    public final ChildNode getLastChild() {
        ChildNode previousChild = null;
        ChildNode child = getFirstChild();
        while (child != null) {
            previousChild = child;
            child = child.getNextSibling();
        }
        return previousChild;
    }
    
    public final boolean hasChildNodes() {
        // TODO: not the best way if content is optimized
        return getFirstChild() != null;
    }

    public final NodeList getChildNodes() {
        return this;
    }

    public final Node item(int index) {
        // TODO: need unit test to check that this works when parsing is deferred
        // TODO: wrong result for negavite indexes
        ChildNode node = getFirstChild();
        for (int i=0; i<index && node != null; i++) {
            node = node.getNextSibling();
        }
        return node;
    }

    public final NodeList getElementsByTagName(final String tagname) {
        return new ElementsBy(getDocument()) {
            @Override
            protected Iterator<Element> createIterator() {
                Iterator<Element> iterator = new DescendantsIterator<Element>(Element.class, ParentNodeImpl.this);
                if (tagname.equals("*")) {
                    return iterator;
                } else {
                    return new FilterIterator<Element>(iterator) {
                        @Override
                        protected boolean matches(Element element) {
                            return tagname.equals(element.getTagName());
                        }
                    };
                }
            }
        };
    }

    public final NodeList getElementsByTagNameNS(final String namespaceURI, final String localName) {
        return new ElementsBy(getDocument()) {
            @Override
            protected Iterator<Element> createIterator() {
                boolean nsWildcard = "*".equals(namespaceURI);
                boolean localNameWildcard = localName.equals("*");
                Iterator<Element> iterator = new DescendantsIterator<Element>(Element.class, ParentNodeImpl.this);
                if (nsWildcard && localNameWildcard) {
                    return iterator;
                } else if (nsWildcard) {
                    return new ElementLocalNameFilterIterator(iterator, localName);
                } else if (localNameWildcard) {
                    return new ElementNamespaceFilterIterator(iterator, namespaceURI);
                } else {
                    return new ElementNameFilterIterator(iterator, namespaceURI, localName);
                }
            }
        };
    }

    public final Node appendChild(Node newChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        return merge(newChild, null, false);
    }

    public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
        // Note: The specification of the insertBefore method says that "if refChild
        // is null, insert newChild at the end of the list of children". That is, in this
        // case the behavior is identical to appendChild. (This is covered by the DOM 1
        // test suite)
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        return merge(newChild, refChild, false);
    }

    private void prepareNewChild(Node newChild) {
        validateOwnerDocument(newChild);
        
        // Check that the new node is not an ancestor of this node
        Node current = this;
        do {
            if (current == newChild) {
                throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
            }
            current = current.getParentNode();
        } while (current != null);
        
        if (newChild instanceof BuilderTarget) {
            ((BuilderTarget)newChild).build();
        }
    }
    
    protected abstract void validateChildType(ChildNode newChild);
    
    public final Node removeChild(Node oldChild) throws DOMException {
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        return merge(null, oldChild, true);
    }

    public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        return merge(newChild, oldChild, true);
    }

    // insertBefore: newChild != null, refChild != null, removeRefChild == false
    // appendChild:  newChild != null, refChild == null, removeRefChild == false
    // replaceChild: newChild != null, refChild != null, removeRefChild == true
    // removeChild:  newChild == null, refChild != null, removeRefChild == true
    private Node merge(Node newChild, Node refChild, boolean removeRefChild) throws DOMException {
        if (newChild != null) {
            prepareNewChild(newChild);
        }
        ChildNode previousSibling; // The sibling that will precede the new child
        ChildNode nextSibling; // The sibling that will follow the new child
        if (refChild == null) { // implies removeRefChild == false
            previousSibling = getLastChild();
            nextSibling = null;
        } else {
            previousSibling = null;
            ChildNode node = getFirstChild();
            while (node != null && node != refChild) {
                previousSibling = node;
                node = node.getNextSibling();
            }
            if (node == null) {
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
            }
            nextSibling = removeRefChild ? node.getNextSibling() : node;
        }
        if (newChild == null && removeRefChild) {
            if (previousSibling == null) {
                internalSetFirstChild(nextSibling);
            } else {
                previousSibling.internalSetNextSibling(nextSibling);
            }
            notifyChildrenModified(-1);
        } else {
            ChildNode firstNodeToInsert;
            ChildNode lastNodeToInsert;
            int delta; // The difference in number of children before and after the operation
            if (newChild instanceof DocumentFragmentImpl) {
                DocumentFragmentImpl fragment = (DocumentFragmentImpl)newChild;
                firstNodeToInsert = fragment.getFirstChild();
                lastNodeToInsert = null;
                for (ChildNode node = firstNodeToInsert; node != null; node = node.getNextSibling()) {
                    // TODO: if validateChildType throws an exception, this will leave the DOM tree in a corrupt state!
                    validateChildType(node);
                    node.internalSetParent(this);
                    lastNodeToInsert = node;
                }
                delta = fragment.getLength();
                // TODO: need to clear the document fragment?
            } else if (newChild instanceof ChildNode) {
                // TODO: what if this is already a child of some container?
                firstNodeToInsert = lastNodeToInsert = (ChildNode)newChild;
                validateChildType(firstNodeToInsert);
                firstNodeToInsert.internalSetParent(this);
                delta = 1;
            } else {
                throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
            }
            if (removeRefChild) {
                delta--;
            }
            if (delta != 0) {
                notifyChildrenModified(delta);
            }
            if (previousSibling == null) {
                internalSetFirstChild(firstNodeToInsert);
            } else {
                previousSibling.internalSetNextSibling(firstNodeToInsert);
            }
            if (nextSibling != null) {
                lastNodeToInsert.internalSetNextSibling(nextSibling);
            }
        }
        if (removeRefChild) {
            ((ChildNode)refChild).internalSetParent(null);
        }
        return removeRefChild ? refChild : newChild;
    }
    
    protected final Node deepClone() {
        Node clone = shallowClone();
        ChildNode child = getFirstChild();
        while (child != null) {
            clone.appendChild(child.cloneNode(true));
            child = child.getNextSibling();
        }
        return clone;
    }
    
    protected abstract Node shallowClone();

    public final CharSequence collectTextContent(CharSequence appendTo) {
        CharSequence content = appendTo;
        for (ChildNode node = getFirstChild(); node != null; node = node.getNextSibling()) {
            content = node.collectTextContent(content);
        }
        return content;
    }
}