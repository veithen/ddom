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
package com.googlecode.ddom.frontend.dom.support;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.dom.intf.AbortNormalizationException;
import com.googlecode.ddom.frontend.dom.intf.DOMDocument;
import com.googlecode.ddom.frontend.dom.intf.DOMDocumentType;
import com.googlecode.ddom.frontend.dom.intf.DOMDocumentTypeDeclaration;
import com.googlecode.ddom.frontend.dom.intf.NormalizationConfig;

public class DocumentTypeImpl implements DOMDocumentType {
    private DOMDocumentTypeDeclaration declaration;
    private Map<String,UserData> userData;
    private DOMImplementation domImplementation;
    private String name;
    private String publicId;
    private String systemId;

    public DocumentTypeImpl(DOMImplementation domImplementation, String name, String publicId, String systemId) {
        this.domImplementation = domImplementation;
        this.name = name;
        this.publicId = publicId;
        this.systemId = systemId;
    }

    public DocumentTypeImpl(DOMDocumentTypeDeclaration declaration) {
        this.declaration = declaration;
    }
    
    public final DOMDocumentTypeDeclaration getDeclaration() {
        return declaration;
    }

    public final DOMDocumentTypeDeclaration attach(DOMDocument document) {
        if (declaration != null) {
            throw new IllegalStateException();
        }
        declaration = (DOMDocumentTypeDeclaration)document.coreGetNodeFactory().createDocumentTypeDeclaration(document, name, publicId, systemId);
        name = null;
        publicId = null;
        systemId = null;
        return declaration;
    }

    public final Map<String,UserData> getUserDataMap(boolean create) {
        if (create && userData == null) {
            userData = new HashMap<String,UserData>();
        }
        return userData;
    }

    public final DOMImplementation getDOMImplementation() {
        return declaration == null ? domImplementation : ((DOMDocument)declaration.coreGetOwnerDocument(true)).getImplementation();
    }

    public final String getName() {
        return declaration == null ? name : declaration.coreGetRootName();
    }

    public final String getPublicId() {
        return declaration == null ? publicId : declaration.coreGetPublicId();
    }

    public final String getSystemId() {
        return declaration == null ? systemId : declaration.coreGetSystemId();
    }

    public final NamedNodeMap getEntities() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getInternalSubset() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final NamedNodeMap getNotations() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
//        CoreDocument document = coreGetDocument();
//        return (Node)document.getNodeFactory().createDocumentTypeDeclaration(document, coreGetRootName(), coreGetPublicId(), coreGetSystemId());
    }

    public final Document getOwnerDocument() {
        DOMDocumentTypeDeclaration declaration = getDeclaration();
        return declaration == null ? null : (Document)declaration.coreGetOwnerDocument(true);
    }

    public final Node getParentNode() {
        DOMDocumentTypeDeclaration declaration = getDeclaration();
        return declaration == null ? null : (Node)declaration.coreGetParent();
    }

    public final String getTextContent() {
        return null;
    }

    public final void setTextContent(@SuppressWarnings("unused") String textContent) {
        // Setting textContent on a DocumentType has no effect.
    }
    
    public final Node getNextSibling() {
        try {
            DOMDocumentTypeDeclaration declaration = getDeclaration();
            return declaration == null ? null : NodeUtil.toDOM(declaration.coreGetNextSibling());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final Node getPreviousSibling() {
        try {
            DOMDocumentTypeDeclaration declaration = getDeclaration();
            return declaration == null ? null : NodeUtil.toDOM(declaration.coreGetPreviousSibling());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final short getNodeType() {
        return Node.DOCUMENT_TYPE_NODE;
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String getNodeName() {
        return getName();
    }
    
    public final CoreElement getNamespaceContext() {
        return null;
    }

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final Node getFirstChild() {
        return null;
    }
    
    public final Node getLastChild() {
        return null;
    }

    public final boolean hasChildNodes() {
        return false;
    }
    
    public final NodeList getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    public final Node appendChild(@SuppressWarnings("unused") Node newChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
    }

    public final Node insertBefore(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node refChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node removeChild(@SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node replaceChild(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String getLocalName() {
        return null;
    }
    
    public final void normalize(NormalizationConfig config) throws AbortNormalizationException {
        
    }

    public final boolean isSupported(String feature, String version) {
        return getDOMImplementation().hasFeature(feature, version);
    }

    public final Object getFeature(String feature, String version) {
        return this;
    }

    public final boolean isSameNode(Node other) {
        return other == this;
    }

    public final boolean isEqualNode(Node other) {
        // We know that for any DOCUMENT_TYPE_NODE, localName, namespaceURI, prefix and nodeValue
        // are null, so no need to compare them
        return other.getNodeType() == Node.DOCUMENT_TYPE_NODE
                && ObjectUtils.equals(other.getNodeName(), getNodeName());
    }

    public final String getBaseURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final short compareDocumentPosition(Node other) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Object getUserData(String key) {
        Map<String,UserData> userDataMap = getUserDataMap(false);
        if (userDataMap == null) {
            return null;
        } else {
            UserData userData = userDataMap.get(key);
            return userData == null ? null : userData.getData();
        }
    }

    public final Object setUserData(String key, Object data, UserDataHandler handler) {
        UserData userData;
        if (data == null) {
            Map<String,UserData> userDataMap = getUserDataMap(false);
            if (userDataMap != null) {
                userData = userDataMap.remove(key);
            } else {
                userData = null;
            }
        } else {
            Map<String,UserData> userDataMap = getUserDataMap(true);
            userData = userDataMap.put(key, new UserData(data, handler));
        }
        return userData == null ? null : userData.getData();
    }
    
    public final String lookupNamespaceURI(String prefix) {
        return null;
    }

    public final String lookupPrefix(String namespaceURI) {
        return null;
    }

    public final boolean isDefaultNamespace(String namespaceURI) {
        return false;
    }

    public final void normalize() {
        // Nothing to do
    }
}
