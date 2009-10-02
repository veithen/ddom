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
package com.google.code.ddom.dom.impl;

import com.google.code.ddom.spi.model.DOMCDATASection;
import com.google.code.ddom.spi.model.DOMDocument;
import com.google.code.ddom.spi.model.TextNode;

public class CDATASectionImpl extends TextNodeImpl implements DOMCDATASection {
    public CDATASectionImpl(DOMDocument document, String data) {
        super(document, data);
    }

    public final short getNodeType() {
        return CDATA_SECTION_NODE;
    }

    public final String getNodeName() {
        return "#cdata-section";
    }

    @Override
    protected TextNode createNewTextNode(String data) {
        DOMDocument document = getDocument();
        return document.getNodeFactory().createCDATASection(document, data);
    }
}
