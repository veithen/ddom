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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreLeafNode;
import com.googlecode.ddom.core.CoreProcessingInstruction;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.AbortNormalizationException;
import com.googlecode.ddom.frontend.dom.intf.DOMLeafNode;
import com.googlecode.ddom.frontend.dom.intf.NormalizationConfig;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionTranslator;
import com.googlecode.ddom.frontend.dom.support.EmptyNodeList;

@Mixin({CoreLeafNode.class, CoreProcessingInstruction.class, CoreComment.class, CoreCDATASection.class})
public abstract class LeafNodeSupport implements DOMLeafNode {
    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final Node getFirstChild() {
        return null;
    }
    
    public final Node getLastChild() {
        return null;
    }

    public final boolean hasChildNodes() {
        return false;
    }
    
    public final NodeList getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    public final Node appendChild(@SuppressWarnings("unused") Node newChild) throws DOMException {
        throw DOMExceptionTranslator.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
    }

    public final Node insertBefore(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node refChild) throws DOMException {
        throw DOMExceptionTranslator.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node removeChild(@SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionTranslator.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node replaceChild(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionTranslator.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final void normalize(NormalizationConfig config) throws AbortNormalizationException {
        
    }
}
