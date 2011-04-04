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

/**
 * Represents a document information item. It is the root of the document tree and contains a
 * sequence of child nodes that must conform to the well-formedness rules defined by the XML
 * specification. In particular, there must not be more than one child element.
 * <p>
 * An instance of this interface also stores the information from the XML declaration of the
 * document. This data can be accessed using the {@link #coreGetXmlVersion()},
 * {@link #coreGetXmlEncoding()} and {@link #coreGetStandalone()} methods. The information from the
 * XML declaration is managed in a way similar to the attributes of an element. In particular, a
 * call to {@link CoreParentNode#coreSetContent(com.googlecode.ddom.stream.XmlSource)} will reset
 * this data, and a subsequent attempt to access the data may result in a call to the underlying XML
 * source.
 * 
 * @author Andreas Veithen
 */
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
    
    /**
     * Get the charset encoding as specified in the XML declaration of the document.
     * 
     * @return the value of the <code>encoding</code> attribute in the XML declaration, or
     *         <code>null</code> if the document doesn't have an XML declaration or the
     *         <code>encoding</code> attribute is not present
     * @throws DeferredParsingException
     *             if an error occurs during deferred parsing of the document
     */
    String coreGetXmlEncoding() throws DeferredParsingException;
    
    void coreSetXmlEncoding(String xmlEncoding);
    
    /**
     * Get the value of the <code>standalone</code> attribute in the XML declaration of the
     * document.
     * 
     * @return the value of the <code>standalone</code> attribute in the XML declaration, or
     *         <code>null</code> if the document doesn't have an XML declaration or the
     *         <code>standalone</code> attribute is not present
     * @throws DeferredParsingException
     *             if an error occurs during deferred parsing of the document
     */
    Boolean coreGetStandalone() throws DeferredParsingException;
    
    void coreSetStandalone(Boolean standalone);
    
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
