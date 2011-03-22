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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.stream.XmlHandler;

public class DocumentFragment extends ParentNode implements CoreDocumentFragment {
    private Document document;
    private int children;
    
    public DocumentFragment(Document document) {
        super(Flags.STATE_EXPANDED);
        this.document = document;
    }

    public final int coreGetNodeType() {
        return DOCUMENT_FRAGMENT_NODE;
    }

    public final void internalNotifyChildrenModified(int delta) {
        children += delta;
    }

    public final void internalNotifyChildrenCleared() {
        children = 0;
    }

    public void internalValidateChildType(CoreChildNode newChild, CoreChildNode replacedChild) {
        // All node type are allowed
    }

    public final int coreGetChildCount() {
        return children;
    }

    public final LLDocument internalGetOwnerDocument(boolean create) {
        // TODO: create?
        return document;
    }

    public final void internalGenerateStartEvent(XmlHandler handler) {
        // TODO
    }

    public final void internalGenerateEndEvent(XmlHandler handler) {
        // TODO
    }

    public final void coreSetOwnerDocument(CoreDocument document) {
        // TODO: there is probably something more to do here if the node is not complete
        this.document = (Document)document;
    }
}
