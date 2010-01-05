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
package com.google.code.ddom.backend;

import com.google.code.ddom.DeferredDocument;
import com.google.code.ddom.DeferredParsingException;
import com.google.code.ddom.stream.spi.Symbols;

public interface CoreDocument extends DeferredDocument, BuilderTarget {
    NodeFactory getNodeFactory();
    
    /**
     * Get the symbol table used by this document. Various methods defined by the core model are
     * required to return canonicalized strings. This method gives access to the symbol table used
     * for all canonicalizations occurring for nodes linked to this document.
     * <p>
     * If the document has been created from a {@link com.google.code.ddom.stream.spi.Producer}, the
     * the symbol table MUST be the same as the one returned by
     * {@link com.google.code.ddom.stream.spi.Producer#getSymbols()}.
     * 
     * @return the symbol table; may not be <code>null</code>
     */
    Symbols getSymbols();
    
    void next() throws DeferredParsingException;
    String coreGetInputEncoding();
    void coreSetInputEncoding(String inputEncoding);
    String coreGetXmlVersion();
    void coreSetXmlVersion(String xmlVersion);
    String coreGetXmlEncoding();
    void coreSetXmlEncoding(String xmlEncoding);
    boolean coreGetStandalone();
    void coreSetStandalone(boolean standalone);
    String coreGetDocumentURI();
    void coreSetDocumentURI(String documentURI);
    CoreDocumentType coreGetDocumentType();
    
    /**
     * Get the document element.
     * 
     * @return the document element, or <code>null</code> if the document doesn't have a child
     *         element
     */
    CoreElement coreGetDocumentElement();
}
