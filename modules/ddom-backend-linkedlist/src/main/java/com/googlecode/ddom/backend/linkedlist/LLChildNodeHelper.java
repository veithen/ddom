/*
 * Copyright 2009-2013 Andreas Veithen
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

import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.backend.linkedlist.intf.LLNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.HierarchyException;
import com.googlecode.ddom.core.NoParentException;
import com.googlecode.ddom.core.NodeMigrationException;
import com.googlecode.ddom.core.NodeMigrationPolicy;
import com.googlecode.ddom.core.SelfRelationshipException;
import com.googlecode.ddom.core.ext.ModelExtension;

public final class LLChildNodeHelper {
    private LLChildNodeHelper() {}
    
    public static LLDocument internalGetOwnerDocument(LLChildNode that, boolean create) {
        LLNode root = that;
        while (root.internalGetFlag(Flags.HAS_PARENT)) {
            root = ((LLChildNode)root).internalGetOwner();
        }
        if (root instanceof LLChildNode) {
            LLChildNode rootChildNode = (LLChildNode)root;
            LLDocument document = (LLDocument)rootChildNode.internalGetOwner();
            if (document == null && create) {
                document = new Document(ModelExtension.NULL); // TODO: model extension???
                rootChildNode.internalSetOwner(document);
            }
            return document;
        } else {
            // We get here if the root node is a document or document fragment
            return ((LLNode)root).internalGetOwnerDocument(create);
        }
    }
    
    public static LLParentNode internalGetParent(LLChildNode that) {
        return that.internalGetFlag(Flags.HAS_PARENT) ? that.internalGetOwner() : null;
    }
    
    public static void internalSetParent(LLChildNode that, LLParentNode parent) {
        if (parent == null) {
            // TODO: this case should be covered by internalUnsetParent
            that.internalSetOwner(internalGetOwnerDocument(that, false));
            that.internalSetFlag(Flags.HAS_PARENT, false);
        } else {
            that.internalSetOwner(parent);
            that.internalSetFlag(Flags.HAS_PARENT, true);
        }
    }
    
    public static void internalUnsetParent(LLChildNode that, LLDocument newOwnerDocument) {
        that.internalSetOwner(newOwnerDocument);
        that.internalSetFlag(Flags.HAS_PARENT, false);
    }
    
    public static LLChildNode internalGetNextSibling(LLChildNode that) throws DeferredParsingException {
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            return null;
        } else {
            if (that.internalGetNextSiblingIfMaterialized() == null && !parent.coreIsComplete()) {
                InputContext context = that.internalGetOwnerDocument(false).internalGetInputContext(parent);
                do {
                    context.next();
                } while (that.internalGetNextSiblingIfMaterialized() == null && !parent.coreIsComplete());
            }
            return that.internalGetNextSiblingIfMaterialized();
        }
    }
    
    public static LLChildNode internalGetPreviousSibling(LLChildNode that) {
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            return null;
        } else {
            LLChildNode previousSibling = null;
            LLChildNode sibling = parent.internalGetFirstChildIfMaterialized();
            while (sibling != that) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSiblingIfMaterialized();
            }
            return sibling == null ? null : previousSibling;
        }
    }
    
    public static void coreInsertSiblingAfter(LLChildNode that, CoreChildNode coreSibling, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        if (coreSibling == that) {
            throw new SelfRelationshipException();
        }
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.internalValidateChildType(coreSibling, null);
            LLChildNode sibling = parent.internalPrepareNewChild(coreSibling, policy);
            // Note: since we have a builder of type 2, we don't need to materialize the next sibling
            // and we can use nextSibling instead of coreGetNextSibling()
            sibling.internalSetNextSibling(that.internalGetNextSiblingIfMaterialized());
            that.internalSetNextSibling(sibling);
            sibling.internalSetParent(parent);
            parent.internalNotifyChildrenModified(1);
        }
    }
    
    public static void coreInsertSiblingsAfter(LLChildNode that, CoreDocumentFragment coreFragment) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        DocumentFragment fragment = (DocumentFragment)coreFragment;
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            // TODO: we need to validate the children types; note that this is especially tricky if the children will be added later during deferred parsing
            that.internalValidateOwnerDocument(fragment);
            if (parent.coreIsComplete() && that.internalGetNextSiblingIfMaterialized() == null && !fragment.coreIsComplete()) {
                // This is a special case: we don't need to build the fragment, but only to move
                // the already materialized children and then to migrate the builder. This is
                // possible because we have a builder of type 2.
                fragment.internalGetOrCreateInputContext().setTargetNode(parent);
                LLChildNode node = fragment.internalGetFirstChildIfMaterialized();
                that.internalSetNextSibling(node);
                int nodeCount = 0;
                while (node != null) {
                    node.internalSetParent(parent);
                    node = node.internalGetNextSiblingIfMaterialized();
                    nodeCount++;
                }
                fragment.internalSetFirstChild(null);
                fragment.internalNotifyChildrenCleared();
                parent.internalNotifyChildrenModified(nodeCount);
            } else {
                // TODO
            }
        }
    }
    
    public static void coreInsertSiblingBefore(LLChildNode that, CoreChildNode coreSibling, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException {
        if (coreSibling == that) {
            throw new SelfRelationshipException();
        }
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.internalValidateChildType(coreSibling, null);
            LLChildNode sibling = parent.internalPrepareNewChild(coreSibling, policy);
            LLChildNode previousSibling = null;
            LLChildNode node = parent.internalGetFirstChildIfMaterialized();
            while (node != that) {
                previousSibling = node;
                node = node.internalGetNextSiblingIfMaterialized();
            }
            sibling.internalSetNextSibling(that);
            if (previousSibling == null) {
                parent.internalSetFirstChild(sibling);
            } else {
                previousSibling.internalSetNextSibling(sibling);
            }
            sibling.internalSetParent(parent);
            parent.internalNotifyChildrenModified(1);
        }
    }
    
    public static void coreInsertSiblingsBefore(LLChildNode that, CoreDocumentFragment coreFragment) throws HierarchyException, NodeMigrationException, DeferredBuildingException {
        DocumentFragment fragment = (DocumentFragment)coreFragment;
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            that.internalValidateOwnerDocument(fragment);
            fragment.coreBuild();
            LLChildNode node = fragment.internalGetFirstChild();
            if (node != null) {
                LLChildNode previousSibling = that.internalGetPreviousSibling();
                if (previousSibling == null) {
                    parent.internalSetFirstChild(node);
                } else {
                    previousSibling.internalSetNextSibling(node);
                }
                int nodeCount = 0;
                LLChildNode previousNode;
                do {
                    node.internalSetParent(parent);
                    previousNode = node;
                    node = node.internalGetNextSibling();
                    nodeCount++;
                } while (node != null);
                previousNode.internalSetNextSibling(that);
                fragment.internalSetFirstChild(null);
                fragment.internalNotifyChildrenCleared();
                parent.internalNotifyChildrenModified(nodeCount);
            }
        }
    }
    
    public static void coreDetach(LLChildNode that) throws DeferredParsingException {
        that.internalDetach();
        that.internalUnsetParent(internalGetOwnerDocument(that, false));
    }
    
    public static void coreDetach(LLChildNode that, CoreDocument document) throws DeferredParsingException {
        if (that instanceof LLParentNode) {
            LLDocument oldDocument = that.internalGetOwnerDocument(false);
            that.internalDetach();
            that.internalUnsetParent((LLDocument)document);
            if (oldDocument != null) {
                // TODO: we only need this if the new document is not the same as the old one!
                ((LLParentNode)that).internalImportBuilder((Document)oldDocument, (LLParentNode)that);
            }
        } else {
            that.internalDetach();
            that.internalUnsetParent((LLDocument)document);
        }
    }
    
    public static void internalDetach(LLChildNode that) {
        LLParentNode parent = that.internalGetParent();
        if (parent != null) {
            LLChildNode previousSibling = that.internalGetPreviousSibling();
            // We have a builder of type 2; thus we don't need to build
            // the node being detached. Therefore we can use internalGetNextSibling
            // instead of coreGetNextSibling.
            if (previousSibling == null) {
                parent.internalSetFirstChild(that.internalGetNextSiblingIfMaterialized());
            } else {
                previousSibling.internalSetNextSibling(that.internalGetNextSiblingIfMaterialized());
            }
            that.internalSetNextSibling(null);
            parent.internalNotifyChildrenModified(-1);
        }
    }
    
    public static void coreReplaceWith(LLChildNode that, CoreChildNode coreNewNode) throws CoreModelException {
        LLChildNode newNode = (LLChildNode)coreNewNode;
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else if (newNode != that) {
            parent.internalValidateChildType(newNode, that);
            parent.internalPrepareNewChild(newNode);
            newNode.coreDetach();
            LLChildNode previousSibling = that.internalGetPreviousSibling();
            if (previousSibling == null) {
                parent.internalSetFirstChild(newNode);
            } else {
                previousSibling.internalSetNextSibling(newNode);
            }
            newNode.internalSetNextSibling(that.internalGetNextSiblingIfMaterialized());
            newNode.internalSetParent(parent);
            that.internalSetNextSibling(null);
            that.internalSetParent(null);
        }
    }
    
    public static void coreReplaceWith(LLChildNode that, CoreDocumentFragment newNodes) throws CoreModelException {
        DocumentFragment fragment = (DocumentFragment)newNodes;
        LLParentNode parent = that.internalGetParent();
        if (parent == null) {
            throw new NoParentException();
        } else {
            that.internalValidateOwnerDocument(newNodes);
            LLChildNode previousSibling = that.internalGetPreviousSibling();
            int nodeCount = 0;
            if (parent.coreIsComplete() && that.internalGetNextSiblingIfMaterialized() == null && !fragment.coreIsComplete()) {
                // This is the same case as considered in coreInsertSiblingsAfter
                that.internalGetOwnerDocument(false).internalGetInputContext(fragment).setTargetNode(parent);
                LLChildNode node = fragment.internalGetFirstChildIfMaterialized();
                if (previousSibling == null) {
                    parent.internalSetFirstChild(node);
                } else {
                    previousSibling.internalSetNextSibling(node);
                }
                while (node != null) {
                    node.internalSetParent(parent);
                    node = node.internalGetNextSiblingIfMaterialized();
                    nodeCount++;
                }
            } else {
                fragment.coreBuild(); // Avoids repetitive lookup of builder; anyway we need all nodes
                LLChildNode node = (LLChildNode)fragment.coreGetFirstChild(); // TODO: internal... method here
                if (previousSibling == null) {
                    parent.internalSetFirstChild(node);
                } else {
                    previousSibling.internalSetNextSibling(node);
                }
                LLChildNode previousNode;
                do {
                    node.internalSetParent(parent);
                    previousNode = node;
                    node = node.internalGetNextSibling();
                    nodeCount++;
                } while (node != null);
                previousNode.internalSetNextSibling(that.internalGetNextSiblingIfMaterialized());
            }
            parent.internalNotifyChildrenModified(nodeCount-1);
            fragment.internalSetFirstChild(null);
            fragment.internalNotifyChildrenCleared();
            that.internalSetParent(null);
        }
    }
}
