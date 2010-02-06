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
package com.google.code.ddom.frontend.dom.aspects;

import com.google.code.ddom.frontend.dom.intf.*;

/**
 * Aspect implementing {@link org.w3c.dom.Node#getNodeName()}.
 * 
 * @author Andreas Veithen
 */
public aspect NodeName {
    public final String DOMAttribute.getNodeName() {
        return getName();
    }

    public final String DOMCDATASection.getNodeName() {
        return "#cdata-section";
    }

    public final String DOMComment.getNodeName() {
        return "#comment";
    }

    public final String DOMDocumentFragment.getNodeName() {
        return "#document-fragment";
    }

    public final String DOMDocument.getNodeName() {
        return "#document";
    }

    public final String DOMDocumentType.getNodeName() {
        return getName();
    }

    public final String DOMElement.getNodeName() {
        return getTagName();
    }

    public final String DOMEntityReference.getNodeName() {
        return coreGetName();
    }

    public final String DOMProcessingInstruction.getNodeName() {
        return getTarget();
    }

    public final String DOMText.getNodeName() {
        return "#text";
    }
}
