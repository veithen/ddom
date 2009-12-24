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
package com.google.code.ddom.core.model;

import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreDocumentFragment;
import com.google.code.ddom.spi.model.Implementation;

@Implementation
public class DocumentFragmentImpl extends BuilderWrapperImpl implements CoreDocumentFragment {
    private final CoreDocument document;
    private CoreChildNode firstChild;
    private int children;
    
    public DocumentFragmentImpl(CoreDocument document) {
        this.document = document;
    }

    public final void internalSetFirstChild(CoreChildNode child) {
        this.firstChild = child;
    }
    
    public final CoreChildNode coreGetFirstChild() {
        return firstChild;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(CoreChildNode newChild) {
        // All node type are allowed
    }

    public final int coreGetChildCount() {
        return children;
    }

    public final CoreDocument getDocument() {
        return document;
    }
}
