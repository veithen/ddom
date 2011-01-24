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
package com.googlecode.ddom.core;

import com.googlecode.ddom.symbols.Symbols;

public interface CoreDocument extends CoreParentNode {
    /**
     * Get the symbol table used by this document. The symbol table may be used to avoid creation of
     * new {@link String} objects when doing substring operations.
     * 
     * @return the symbol table; may not be <code>null</code>
     */
    Symbols getSymbols();
    
    String coreGetInputEncoding() throws DeferredParsingException;
    void coreSetInputEncoding(String inputEncoding);
    String coreGetXmlVersion() throws DeferredParsingException;
    void coreSetXmlVersion(String xmlVersion);
    String coreGetXmlEncoding() throws DeferredParsingException;
    void coreSetXmlEncoding(String xmlEncoding);
    boolean coreGetStandalone() throws DeferredParsingException;
    void coreSetStandalone(boolean standalone);
    String coreGetDocumentURI() throws DeferredParsingException;
    void coreSetDocumentURI(String documentURI) throws DeferredParsingException;
    CoreDocumentTypeDeclaration coreGetDocumentTypeDeclaration() throws DeferredParsingException;
    
    /**
     * Get the document element.
     * 
     * @return the document element, or <code>null</code> if the document doesn't have a child
     *         element
     * @throws DeferredParsingException 
     */
    CoreElement coreGetDocumentElement() throws DeferredParsingException;
}
