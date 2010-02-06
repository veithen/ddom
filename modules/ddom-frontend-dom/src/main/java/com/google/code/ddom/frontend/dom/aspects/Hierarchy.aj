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

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.code.ddom.frontend.dom.intf.*;

/**
 * Aspect implementing {@link Node#getOwnerDocument()} and {@link Node#getParentNode()}.
 * 
 * @author Andreas Veithen
 */
public aspect Hierarchy {
    public final Document DOMDocument.getOwnerDocument() {
        return null;
    }

    public final Document DOMAttribute.getOwnerDocument() {
        return (Document)coreGetDocument();
    }
    
    public final Document DOMDocumentFragment.getOwnerDocument() {
        return (Document)coreGetDocument();
    }

    public final Document DOMCoreChildNode.getOwnerDocument() {
        return (Document)coreGetDocument();
    }
    
    public final Document DOMDocumentType.getOwnerDocument() {
        DOMDocumentTypeDeclaration declaration = getDeclaration();
        return declaration == null ? null : (Document)declaration.coreGetDocument();
    }
    
    public final Node DOMAttribute.getParentNode() {
        return null;
    }

    public final Node DOMCoreChildNode.getParentNode() {
        return (Node)coreGetParent();
    }

    public final Node DOMDocumentType.getParentNode() {
        DOMDocumentTypeDeclaration declaration = getDeclaration();
        return declaration == null ? null : (Node)declaration.coreGetParent();
    }

    public final Node DOMDocumentFragment.getParentNode() {
        return null;
    }
    
    public final Node DOMDocument.getParentNode() {
        return null;
    }
}
