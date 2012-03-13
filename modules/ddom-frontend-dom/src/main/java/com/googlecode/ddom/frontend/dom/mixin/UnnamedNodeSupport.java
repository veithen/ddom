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
package com.googlecode.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreLeafNode;
import com.googlecode.ddom.core.CoreProcessingInstruction;
import com.googlecode.ddom.frontend.Mixin;

/**
 * Provides implementations for {@link Node#getNamespaceURI()}, {@link Node#getPrefix()},
 * {@link Node#setPrefix(String)} and {@link Node#getLocalName()} for nodes that have no name.
 * 
 * @author Andreas Veithen
 */
@Mixin({CoreDocumentFragment.class,CoreDocument.class,CoreLeafNode.class, CoreProcessingInstruction.class, CoreComment.class, CoreCDATASection.class})
public abstract class UnnamedNodeSupport implements Node {
    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String getLocalName() {
        return null;
    }
}
