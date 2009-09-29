package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class DOMImplementationImpl implements DOMImplementation {
    public static final DOMImplementationImpl INSTANCE = new DOMImplementationImpl();
    
    private DOMImplementationImpl() {}
    
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException {
        // TODO: factory here!!!!
        DocumentImpl document = new DocumentImpl(null);
        if (doctype != null) {
            if (doctype.getOwnerDocument() == null) {
                ((DocumentTypeImpl)doctype).internalSetDocument(document);
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
        // TODO: factory here!!!!
        DocumentTypeImpl docType = new DocumentTypeImpl(null);
        docType.setName(qualifiedName);
        docType.setPublicId(publicId);
        docType.setSystemId(systemId);
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
