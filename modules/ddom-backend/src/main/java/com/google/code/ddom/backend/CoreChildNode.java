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
    CoreParentNode coreGetParent();
    
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
     * @throws NoParentException if this node has no parent
     * @throws SelfRelationshipException if this node and the new sibling are the same
     * @throws CyclicRelationshipException if the sibling to be inserted is an ancestor of this node
     */
    void coreInsertSiblingAfter(CoreNode sibling) throws CoreModelException;
    
    /**
     * 
     * @param sibling
     * @throws NoParentException if this node has no parent
     * @throws SelfRelationshipException if this node and the new sibling are the same
     * @throws CyclicRelationshipException if the sibling to be inserted is an ancestor of this node
     */
    void coreInsertSiblingBefore(CoreNode sibling) throws CoreModelException;
    
    /**
     * Detach this node from its parent. If the node has no parent, then this method does nothing.
     * @throws DeferredParsingException 
     */
    void coreDetach() throws DeferredParsingException;
    
    void internalSetParent(CoreParentNode parent);
    CoreChildNode internalGetNextSibling();
    void internalSetNextSibling(CoreChildNode nextSibling);
}
