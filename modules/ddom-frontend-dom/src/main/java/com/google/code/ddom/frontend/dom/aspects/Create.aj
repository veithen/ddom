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

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.google.code.ddom.frontend.dom.support.NSUtil;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect Create {
    public final Element DOMDocument.createElement(String tagName) throws DOMException {
        NSUtil.validateName(tagName);
        return (Element)getNodeFactory().createElement(this, tagName, true);
    }
    
    public final Element DOMDocument.createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
        NSUtil.validateNamespace(namespaceURI, prefix);
        return (Element)getNodeFactory().createElement(this, namespaceURI, localName, prefix, true);
    }
    
    public final Attr DOMDocument.createAttribute(String name) throws DOMException {
        NSUtil.validateName(name);
        return (Attr)getNodeFactory().createAttribute(this, name, null, null);
    }

    public final Attr DOMDocument.createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return (Attr)getNodeFactory().createNSDecl(this, NSUtil.getDeclaredPrefix(localName, prefix), null);
        } else {
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            return (Attr)getNodeFactory().createAttribute(this, namespaceURI, localName, prefix, null, null);
        }
    }

    public final ProcessingInstruction DOMDocument.createProcessingInstruction(String target, String data) throws DOMException {
        NSUtil.validateName(target);
        return (ProcessingInstruction)getNodeFactory().createProcessingInstruction(this, target, data);
    }
    
    public final DocumentFragment DOMDocument.createDocumentFragment() {
        return (DocumentFragment)getNodeFactory().createDocumentFragment(this);
    }

    public final Text DOMDocument.createTextNode(String data) {
        return (Text)getNodeFactory().createText(this, data);
    }

    public final Comment DOMDocument.createComment(String data) {
        return (Comment)getNodeFactory().createComment(this, data);
    }

    public final CDATASection DOMDocument.createCDATASection(String data) throws DOMException {
        return (CDATASection)getNodeFactory().createCDATASection(this, data);
    }

    public final EntityReference DOMDocument.createEntityReference(String name) throws DOMException {
        return (EntityReference)getNodeFactory().createEntityReference(this, name);
    }
}
