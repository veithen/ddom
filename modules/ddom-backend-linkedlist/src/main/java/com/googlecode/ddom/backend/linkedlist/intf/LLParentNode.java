/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist.intf;

import com.googlecode.ddom.backend.linkedlist.Document;
import com.googlecode.ddom.core.ChildNotAllowedException;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.CyclicRelationshipException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.NodeMigrationException;
import com.googlecode.ddom.core.NodeMigrationPolicy;
import com.googlecode.ddom.core.WrongDocumentException;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

public interface LLParentNode extends LLNode, CoreParentNode {
    void internalNotifyChildrenModified(int delta);
    void internalNotifyChildrenCleared();
    LLChildNode internalGetFirstChildIfMaterialized();
    void internalSetFirstChild(CoreChildNode child);
    int internalGetState();
    void internalSetState(int state);
    @Deprecated // TODO: should be replaced by the variant taking a NodeMigrationPolicy argument
    void internalPrepareNewChild(CoreChildNode newChild) throws WrongDocumentException, CyclicRelationshipException;
    LLChildNode internalPrepareNewChild(CoreChildNode newChild, NodeMigrationPolicy policy) throws NodeMigrationException, CyclicRelationshipException;
    
    /**
     * Check if the given node is allowed as a child.
     * 
     * @param newChild
     *            the child that will be added
     * @param replacedChild
     *            the child that will be replaced by the new node, or <code>null</code> if the new
     *            child will be inserted and doesn't replace any existing node
     * @throws ChildNotAllowedException
     *             if the child is not allowed
     * @throws DeferredParsingException
     *             if a parsing error occurs. This exception may be thrown because the
     *             implementation may need to check if a child of a given type already exist. If the
     *             node is incomplete, then this may result in a parsing error.
     */
    void internalValidateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildNotAllowedException, DeferredParsingException;
    
    void internalSetValue(String value);

    InputContext internalGetOrCreateInputContext() throws DeferredParsingException;
    
    void internalImportBuilder(Document foreignDocument, LLParentNode node);
    
    // Type safe variant of core model method
    LLChildNode internalGetFirstChild() throws DeferredParsingException;

    void internalGenerateStartEvent(XmlHandler handler) throws StreamException;
    void internalGenerateEndEvent(XmlHandler handler) throws StreamException;
}
