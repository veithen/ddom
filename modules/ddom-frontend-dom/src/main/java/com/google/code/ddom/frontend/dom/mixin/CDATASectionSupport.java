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

import org.w3c.dom.Node;

import com.google.code.ddom.core.CoreCDATASection;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.dom.intf.DOMCDATASection;
import com.google.code.ddom.frontend.dom.intf.DOMTextNode;

@Mixin(CoreCDATASection.class)
public abstract class CDATASectionSupport implements DOMCDATASection {
    public final DOMTextNode createNewTextNode(String data) {
        return (DOMTextNode)coreGetNodeFactory().createCDATASection(coreGetDocument(), data);
    }

    public final short getNodeType() {
        return Node.CDATA_SECTION_NODE;
    }

    public final String getNodeName() {
        return "#cdata-section";
    }
}
