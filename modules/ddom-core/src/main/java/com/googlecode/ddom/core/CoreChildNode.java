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
package com.googlecode.ddom.core;

/**
 * Represents a child information item.
 * 
 * @author Andreas Veithen
 */
public interface CoreChildNode extends CoreNode {
    /**
     * Get the parent of this node.
     * 
     * @return the parent of this node
     */
    CoreParentNode coreGetParent();
    
    /**
     * Check if this node has a parent.
     * 
     * @return <code>true</code> if and only if this node currently has a parent
     */
    boolean coreHasParent();
    
    /**
     * Get the parent element of this node.
     * 
     * @return the parent element of this node or <code>null</code> if the node has no parent or if
     *         the parent is not an element
     */
    CoreElement coreGetParentElement();
    
    CoreChildNode coreGetNextSibling() throws DeferredBuildingException;
    CoreChildNode coreGetPreviousSibling() throws DeferredParsingException;
    
    /**
     * 
     * @param sibling
     * @param policy
     *            the policy to apply if the new sibling already has a parent or belongs to a
     *            different document
     * @throws NoParentException
     *             if this node has no parent
     * @throws SelfRelationshipException
     *             if this node and the new sibling are the same
     * @throws CyclicRelationshipException
     *             if the sibling to be inserted is an ancestor of this node
     * @throws ChildNotAllowedException
     *             if the new sibling is of a type that is not allowed in this position in the
     *             document
     */
    void coreInsertSiblingAfter(CoreChildNode sibling, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException;
    
    void coreInsertSiblingsAfter(CoreDocumentFragment fragment) throws HierarchyException, NodeMigrationException, DeferredBuildingException;
    
    /**
     * 
     * @param sibling
     * @param policy
     *            the policy to apply if the new sibling already has a parent or belongs to a
     *            different document
     * @throws NoParentException
     *             if this node has no parent
     * @throws SelfRelationshipException
     *             if this node and the new sibling are the same
     * @throws CyclicRelationshipException
     *             if the sibling to be inserted is an ancestor of this node
     * @throws ChildNotAllowedException
     *             if the new sibling is of a type that is not allowed in this position in the
     *             document
     */
    void coreInsertSiblingBefore(CoreChildNode sibling, NodeMigrationPolicy policy) throws HierarchyException, NodeMigrationException, DeferredParsingException;
    
    // TODO: document that NodeConsumedException may occur because the fragment needs to be built
    void coreInsertSiblingsBefore(CoreDocumentFragment fragment) throws HierarchyException, NodeMigrationException, DeferredBuildingException;
    
    /**
     * Detach this node from its parent. The node will keep its current owner document. If the node
     * has no parent, then this method does nothing.
     * 
     * @throws DeferredParsingException
     */
    void coreDetach() throws DeferredParsingException;
    
    /**
     * Detach this node from its parent and assign it to a new owner document. The owner document
     * will always be changed, even if the node has no parent.
     * 
     * @param document
     *            the new owner document, or <code>null</code> if the node will have its own owner
     *            document (which may be created lazily at a later moment)
     * @throws DeferredParsingException
     */
    void coreDetach(CoreDocument document) throws DeferredParsingException;
    
    /**
     * Replace this node by another node. If the replacing node has a parent, it will be detached
     * from its parent. If both nodes are the same, then this method does nothing.
     * 
     * @param newNode
     *            the replacing node
     * @throws CoreModelException
     *             TODO
     */
    void coreReplaceWith(CoreChildNode newNode) throws CoreModelException;
    
    void coreReplaceWith(CoreDocumentFragment newNodes) throws CoreModelException;
}
