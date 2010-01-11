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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.Implementation;

@Implementation
public class DocumentFragment extends ParentNode implements CoreDocumentFragment {
    private final Document document;
    private int children;
    
    public DocumentFragment(Document document) {
        super(true);
        this.document = document;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) {
        // All node type are allowed
    }

    public final int coreGetChildCount() {
        return children;
    }

    @Override
    final Document getDocument() {
        return document;
    }
}
