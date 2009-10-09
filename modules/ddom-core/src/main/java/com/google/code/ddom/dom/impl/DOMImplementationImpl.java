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

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreDocumentType;
import com.google.code.ddom.spi.model.NodeFactory;

public class DOMImplementationImpl implements DOMImplementation {
    private final NodeFactory nodeFactory;
    
    public DOMImplementationImpl(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
    
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
        CoreDocument document = nodeFactory.createDocument(null);
        if (doctype != null) {
            if (doctype.getOwnerDocument() == null) {
                ((CoreDocumentType)doctype).internalSetDocument(document);
            }
            document.appendChild(doctype);
        }
        if (qualifiedName != null) {
            document.appendChild(document.createElementNS(namespaceURI, qualifiedName));
        }
        return document;
    }

    public DocumentType createDocumentType(String qualifiedName, String publicId, String systemId)
            throws DOMException {
        NSUtil.validateQualifiedName(qualifiedName);
        CoreDocumentType docType = nodeFactory.createDocumentType(null, qualifiedName, publicId, systemId);
        return docType;
    }

    public Object getFeature(String feature, String version) {
        if (hasFeature(feature, version)) {
            return this;
        } else {
            return null;
        }
    }

    public boolean hasFeature(String feature, String version) {
        // The DOM specification says: "if a plus sign "+" is prepended to any feature name,
        // implementations are considered in which the specified feature may not be directly
        // castable but would require discovery through DOMImplementation.getFeature(feature,
        // version) and Node.getFeature(feature, version)"
        if (feature.startsWith("+")) {
            feature = feature.substring(1);
        }
        boolean anyVersion = version == null || version.length() == 0;
        return (feature.equalsIgnoreCase("Core") || feature.equalsIgnoreCase("XML"))
                && (anyVersion || version.equals("1.0") || version.equals("2.0")
                        || version.equals("3.0"));
    }
}
