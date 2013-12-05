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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMParentNode;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionTranslator;

/**
 * Implements the {@link Node#cloneNode(boolean)} for {@link Element}, {@link Document} and
 * {@link DocumentFragment} nodes. Note that the {@link Node#cloneNode(boolean)} behaves differently
 * on {@link Attribute} nodes (which is why it is not implemented in {@link ParentNodeSupport}).
 * 
 * @author Andreas Veithen
 */
@Mixin({CoreElement.class, CoreDocument.class, CoreDocumentFragment.class})
public abstract class DeepCloneSupport implements DOMParentNode {
    public final Node cloneNode(boolean deep) {
        try {
            return deep ? deepClone() : shallowClone();
        } catch (CoreModelException ex) {
            throw DOMExceptionTranslator.translate(ex);
        }
    }
}
