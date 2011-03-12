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
package com.googlecode.ddom.frontend.dom.support;

import java.util.Iterator;

import org.w3c.dom.Node;

import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ElementMatcher;
import com.googlecode.ddom.frontend.dom.intf.DOMDocument;
import com.googlecode.ddom.frontend.dom.intf.DOMElement;
import com.googlecode.ddom.frontend.dom.intf.DOMParentNode;

public class ElementsByTagName extends ElementsBy {
    private final DOMParentNode node;
    private final String tagname;
    
    public ElementsByTagName(DOMDocument document, DOMParentNode node, String tagname) {
        super(document);
        this.node = node;
        this.tagname = tagname;
    }

    @Override
    protected Iterator<? extends Node> createIterator() {
        if (tagname.equals("*")) {
            return node.coreGetChildrenByType(Axis.DESCENDANTS, DOMElement.class);
        } else {
            return node.coreGetElements(Axis.DESCENDANTS, DOMElement.class, ElementMatcher.BY_NAME, null, tagname);
        }
    }
}
