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

import org.w3c.dom.DOMException;

import com.google.code.ddom.backend.CoreModelException;

import com.google.code.ddom.frontend.dom.intf.*;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;

public aspect TextContent {
    public final String DOMElement.getTextContent() {
        try {
            return coreGetTextContent();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void DOMElement.setTextContent(String textContent) {
        coreSetValue(textContent);
    }

    public final String DOMDocumentFragment.getTextContent() {
        try {
            return coreGetTextContent();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void DOMDocumentFragment.setTextContent(String textContent) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String DOMAttribute.getTextContent() {
        try {
            return coreGetTextContent();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void DOMAttribute.setTextContent(String textContent) {
        coreSetValue(textContent);
    }

    public final String DOMCharacterData.getTextContent() {
        return coreGetData();
    }

    public final void DOMCharacterData.setTextContent(String textContent) {
        coreSetData(textContent);
    }

    public final String DOMEntityReference.getTextContent() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void DOMEntityReference.setTextContent(@SuppressWarnings("unused") String textContent) {
        throw DOMExceptionUtil.newDOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR);
    }

    public final String DOMProcessingInstruction.getTextContent() {
        return coreGetData();
    }

    public final void DOMProcessingInstruction.setTextContent(String textContent) {
        coreSetData(textContent);
    }

    public final String DOMDocument.getTextContent() {
        return null;
    }

    public final void DOMDocument.setTextContent(@SuppressWarnings("unused") String textContent) {
        // Setting textContent on a Document has no effect.
    }

    public final String DOMDocumentTypeDeclaration.getTextContent() {
        return null;
    }

    public final void DOMDocumentTypeDeclaration.setTextContent(@SuppressWarnings("unused") String textContent) {
        // Setting textContent on a DocumentType has no effect.
    }
}
