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

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMCoreChildNode;
import com.google.code.ddom.frontend.dom.intf.DOMCDATASection;
import com.google.code.ddom.frontend.dom.intf.DOMComment;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentFragment;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentType;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.intf.DOMEntityReference;
import com.google.code.ddom.frontend.dom.intf.DOMNamespaceDeclaration;
import com.google.code.ddom.frontend.dom.intf.DOMNSAwareAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMNSAwareElement;
import com.google.code.ddom.frontend.dom.intf.DOMNSUnawareAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMNSUnawareElement;
import com.google.code.ddom.frontend.dom.intf.DOMParentNode;
import com.google.code.ddom.frontend.dom.intf.DOMProcessingInstruction;
import com.google.code.ddom.frontend.dom.intf.DOMText;
import com.google.code.ddom.frontend.dom.intf.DOMTextNode;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

public aspect Clone {
    public final CoreElement DOMNSAwareElement.shallowCloneWithoutAttributes() {
        return coreGetDocument().coreCreateElement(coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix());
    }
    
    public final CoreElement DOMNSUnawareElement.shallowCloneWithoutAttributes() {
        return coreGetDocument().coreCreateElement(coreGetName());
    }

    public final Node DOMEntityReference.cloneNode(boolean deep) {
        return (Node)coreGetDocument().coreCreateEntityReference(coreGetName());
    }

    public final Node DOMProcessingInstruction.cloneNode(boolean deep) {
        return (Node)coreGetDocument().coreCreateProcessingInstruction(getTarget(), getData());
    }

    public final Node DOMTextNode.cloneNode(boolean deep) {
        return createNewTextNode(getData());
    }
    
    public final DOMTextNode DOMText.createNewTextNode(String data) {
        return (DOMTextNode)coreGetDocument().coreCreateText(data);
    }

    public final DOMTextNode DOMCDATASection.createNewTextNode(String data) {
        return (DOMTextNode)coreGetDocument().coreCreateCDATASection(data);
    }
}
