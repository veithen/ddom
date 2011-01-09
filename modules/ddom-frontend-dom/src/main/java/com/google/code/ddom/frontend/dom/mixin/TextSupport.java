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
package com.google.code.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.google.code.ddom.core.CoreText;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.DOMText;
import com.google.code.ddom.frontend.dom.intf.DOMTextNode;

@Mixin(CoreText.class)
public abstract class TextSupport implements DOMText {
    public final DOMTextNode createNewTextNode(String data) {
        return (DOMTextNode)coreGetNodeFactory().createText(coreGetOwnerDocument(true), data);
    }

    public final String getData() {
        return coreGetData();
    }
    
    public final void setData(String data) throws DOMException {
        coreSetData(data);
    }
    
    public final short getNodeType() {
        return Node.TEXT_NODE;
    }

    public final String getNodeName() {
        return "#text";
    }

    public final boolean isElementContentWhitespace() {
        return coreIsIgnorable();
    }
}
