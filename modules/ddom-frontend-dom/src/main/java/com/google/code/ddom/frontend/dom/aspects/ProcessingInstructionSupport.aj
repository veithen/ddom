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

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.frontend.dom.intf.*;

public aspect ProcessingInstructionSupport {
    public final String DOMProcessingInstruction.getData() {
        return coreGetData();
    }

    public final void DOMProcessingInstruction.setData(String data) throws DOMException {
        coreSetData(data);
    }

    public final String DOMProcessingInstruction.getTarget() {
        return coreGetTarget();
    }

    public final Node DOMProcessingInstruction.cloneNode(boolean deep) {
        return (Node)coreGetDocument().coreCreateProcessingInstruction(getTarget(), getData());
    }

    public final String DOMProcessingInstruction.getTextContent() {
        return coreGetData();
    }

    public final void DOMProcessingInstruction.setTextContent(String textContent) {
        coreSetData(textContent);
    }

    public final short DOMProcessingInstruction.getNodeType() {
        return Node.PROCESSING_INSTRUCTION_NODE;
    }

    public final String DOMProcessingInstruction.getNodeValue() throws DOMException {
        return getData();
    }

    public final void DOMProcessingInstruction.setNodeValue(String nodeValue) throws DOMException {
        setData(nodeValue);
    }

    public final String DOMProcessingInstruction.getNodeName() {
        return getTarget();
    }
    
    public final CoreElement DOMProcessingInstruction.getNamespaceContext() {
        return coreGetParentElement();
    }
}
