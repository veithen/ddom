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

import org.w3c.dom.Text;

public class TextImpl extends TextNode {
    public TextImpl(DocumentImpl document, String data) {
        super(document, data);
    }
    
    public final short getNodeType() {
        return TEXT_NODE;
    }

    public final String getNodeName() {
        return "#text";
    }

    @Override
    protected TextNode createNewTextNode(String data) {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createText(document, data);
    }
}
