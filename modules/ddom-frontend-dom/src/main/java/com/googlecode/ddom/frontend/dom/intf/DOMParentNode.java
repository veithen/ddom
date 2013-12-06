/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.dom.intf;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.DeferredParsingException;

/**
 * Interface implemented by all nodes that are parent nodes in the sense of DOM: {@link Document},
 * {@link DocumentFragment}, {@link Element} and {@link Attr}.
 * 
 * @author Andreas Veithen
 */
public interface DOMParentNode extends CoreParentNode, NodeList, DOMCoreNode {
    DOMParentNode shallowClone() throws DeferredParsingException;
    DOMParentNode deepClone();
    void normalizeChildren(NormalizationConfig config) throws AbortNormalizationException;
}
