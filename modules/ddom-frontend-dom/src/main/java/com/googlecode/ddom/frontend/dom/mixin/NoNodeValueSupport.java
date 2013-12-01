/*
 * Copyright 2013 Andreas Veithen
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

import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreEntityReference;
import com.googlecode.ddom.frontend.Mixin;

/**
 * Implements {@link Node#getNodeValue()} and {@link Node#setNodeValue(String)} for node types for
 * which the node value is defined to be <code>null</code>.
 * 
 * @author Andreas Veithen
 */
@Mixin({CoreDocument.class, CoreDocumentFragment.class, CoreDocumentTypeDeclaration.class, CoreElement.class, CoreEntityReference.class})
public abstract class NoNodeValueSupport {
    public final String getNodeValue() {
        return null;
    }

    public final void setNodeValue(String nodeValue) {
        // Setting the node value has no effect
    }
}
