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
package com.googlecode.ddom.frontend.axiom.support;

import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMXMLParserWrapper;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.axiom.intf.AxiomDocument;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;

public class OMXMLParserWrapperImpl implements OMXMLParserWrapper {
    private final AxiomDocument document;
    
    public OMXMLParserWrapperImpl(AxiomDocument document) {
        this.document = document;
    }

    public OMDocument getDocument() {
        return document;
    }

    public OMElement getDocumentElement() {
        return getDocumentElement(false);
    }

    public OMElement getDocumentElement(boolean discardDocument) {
        try {
            AxiomElement element = (AxiomElement)document.coreGetDocumentElement();
            if (discardDocument) {
                element.coreDetach();
            }
            return element;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void discard(OMElement el) throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public short getBuilderType() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getCharacterEncoding() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Object getParser() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Object getRegisteredContentHandler() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean isCache() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean isCompleted() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public int next() throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void registerExternalContentHandler(Object obj) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setCache(boolean b) throws OMException {
        // TODO
//        throw new UnsupportedOperationException();
    }

    public void close() {
        // TODO
//        throw new UnsupportedOperationException();
    }
}
