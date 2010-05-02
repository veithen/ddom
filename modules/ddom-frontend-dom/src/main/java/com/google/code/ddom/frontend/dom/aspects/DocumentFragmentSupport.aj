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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect DocumentFragmentSupport {
    public final boolean DOMDocumentFragment.hasAttributes() {
        return false;
    }

    public final NamedNodeMap DOMDocumentFragment.getAttributes() {
        return null;
    }

    public final Node DOMDocumentFragment.cloneNode(boolean deep) {
        // TODO: check this (maybe a fragment is always deeply cloned?)
        return deep ? deepClone() : shallowClone();
    }

    public final Node DOMDocumentFragment.shallowClone() {
        return (Node)coreGetDocument().coreCreateDocumentFragment();
    }

    public final Document DOMDocumentFragment.getOwnerDocument() {
        return (Document)coreGetDocument();
    }

    public final Node DOMDocumentFragment.getParentNode() {
        return null;
    }
}
