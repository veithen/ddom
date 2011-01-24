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
package com.google.code.ddom.frontend.dom.support;

import org.w3c.dom.Node;

import com.google.code.ddom.frontend.dom.intf.DOMDocumentType;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentTypeDeclaration;
import com.google.code.ddom.frontend.dom.intf.DOMNode;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.core.CoreNode;

public final class NodeUtil {
    private NodeUtil() {}
    
    public static DOMNode toDOM(CoreNode coreNode) {
        if (coreNode instanceof DOMDocumentTypeDeclaration) {
            return toDOM((DOMDocumentTypeDeclaration)coreNode);
        } else {
            return (DOMNode)coreNode;
        }
    }
    
    public static DOMDocumentType toDOM(CoreDocumentTypeDeclaration coreNode) {
        if (coreNode == null) {
            return null;
        } else {
            DOMDocumentTypeDeclaration decl = (DOMDocumentTypeDeclaration)coreNode;
            DOMDocumentType doctype = decl.getDocumentType();
            if (doctype == null) {
                doctype = new DocumentTypeImpl(decl);
                decl.setDocumentType(doctype);
            }
            return doctype;
        }
    }
}
