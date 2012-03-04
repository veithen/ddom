/*
 * Copyright 2009-2012 Andreas Veithen
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

import com.googlecode.ddom.backend.linkedlist.Flags;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.DeferredParsingException;

public interface LLChildNode extends LLNode, CoreChildNode {
    /**
     * Return the owner of the node. This is either the parent or the owner document, depending on
     * the {@link Flags#HAS_PARENT} flag.
     * 
     * @return the parent or owner document of the node
     */
    LLParentNode internalGetOwner();
    
    void internalSetOwner(LLParentNode owner);
    
    LLParentNode internalGetParent();
    void internalSetParent(LLParentNode parent);
    void internalUnsetParent(LLDocument newOwnerDocument);
    
    LLChildNode internalGetNextSiblingIfMaterialized();
    void internalSetNextSibling(LLChildNode nextSibling);
    
    // Type safe variants of several core model methods
    LLChildNode internalGetNextSibling() throws DeferredParsingException;
    LLChildNode internalGetPreviousSibling();
    
    void internalDetach();
}
