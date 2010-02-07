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
package com.google.code.ddom.backend;

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
    
    CoreChildNode coreGetNextSibling() throws DeferredParsingException;
    CoreChildNode coreGetPreviousSibling() throws DeferredParsingException;
    
    /**
     * 
     * @param sibling
     * @throws NoParentException
     *             if this node has no parent
     * @throws SelfRelationshipException
     *             if this node and the new sibling are the same
     * @throws CyclicRelationshipException
     *             if the sibling to be inserted is an ancestor of this node
     * @throws ChildTypeNotAllowedException
     *             if the new sibling is of a type that is not allowed in this position in the
     *             document
     */
    void coreInsertSiblingAfter(CoreChildNode sibling) throws CoreModelException;
    
    void coreInsertSiblingsAfter(CoreDocumentFragment fragment) throws CoreModelException;
    
    /**
     * 
     * @param sibling
     * @throws NoParentException
     *             if this node has no parent
     * @throws SelfRelationshipException
     *             if this node and the new sibling are the same
     * @throws CyclicRelationshipException
     *             if the sibling to be inserted is an ancestor of this node
     * @throws ChildTypeNotAllowedException
     *             if the new sibling is of a type that is not allowed in this position in the
     *             document
     */
    void coreInsertSiblingBefore(CoreChildNode sibling) throws CoreModelException;
    
    void coreInsertSiblingsBefore(CoreDocumentFragment fragment) throws CoreModelException;
    
    /**
     * Detach this node from its parent. If the node has no parent, then this method does nothing.
     * @throws DeferredParsingException 
     */
    void coreDetach() throws DeferredParsingException;
    
    void coreReplaceWith(CoreChildNode newNode) throws CoreModelException;
    void coreReplaceWith(CoreDocumentFragment newNodes) throws CoreModelException;
}
