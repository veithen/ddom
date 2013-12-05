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
package com.googlecode.ddom.frontend.dom.mixin;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreProcessingInstruction;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.DOMProcessingInstruction;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionTranslator;

@Mixin(CoreProcessingInstruction.class)
public abstract class ProcessingInstructionSupport implements DOMProcessingInstruction {
    public final String getData() {
        try {
            return coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw DOMExceptionTranslator.translate(ex);
        }
    }

    public final void setData(String data) throws DOMException {
        try {
            coreSetValue(data);
        } catch (CoreModelException ex) {
            throw DOMExceptionTranslator.translate(ex);
        }
    }

    public final String getTarget() {
        return coreGetTarget();
    }

    public final Node cloneNode(boolean deep) {
        return (Node)coreGetNodeFactory().createProcessingInstruction(coreGetOwnerDocument(true), getTarget(), getData());
    }

    public final String getTextContent() {
        return getData();
    }

    public final void setTextContent(String textContent) {
        setData(textContent);
    }

    public final short getNodeType() {
        return Node.PROCESSING_INSTRUCTION_NODE;
    }

    public final String getNodeValue() throws DOMException {
        return getData();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        setData(nodeValue);
    }

    public final String getNodeName() {
        return getTarget();
    }
    
    public final CoreElement getNamespaceContext() {
        return coreGetParentElement();
    }
}
