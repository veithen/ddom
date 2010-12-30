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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.linkedlist.intf.LLNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreNode;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.NodeFactory;
import com.google.code.ddom.core.WrongDocumentException;

public abstract class Node implements LLNode {
    private int flags;
    
    public final NodeFactory coreGetNodeFactory() {
        return NodeFactoryImpl.INSTANCE;
    }

    public final CoreDocument coreGetOwnerDocument(boolean create) {
        return internalGetOwnerDocument();
    }

    public final boolean coreIsSameOwnerDocument(CoreNode other) {
        return other.coreGetOwnerDocument(true) == internalGetOwnerDocument(); // TODO: this is wrong
    }

    public final boolean internalGetFlag(int flag) {
        return (flags & flag) != 0;
    }

    public final void internalSetFlag(int flag, boolean value) {
        if (value) {
            flags |= flag;
        } else {
            flags &= ~flag;
        }
    }

    public final void internalValidateOwnerDocument(CoreNode node) throws WrongDocumentException {
        CoreDocument document1 = ((Node)node).internalGetOwnerDocument(); // TODO: get rid of cast
        CoreDocument document2 = internalGetOwnerDocument();
        if (document1 != null && document2 != null && document1 != document2) {
            throw new WrongDocumentException();
        }
    }

    abstract CharSequence internalCollectTextContent(CharSequence appendTo) throws DeferredParsingException;
}
