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
package com.google.code.ddom.stream.spi;

// TODO: explain that the preferred way of delivering data to the consumer is
//       using processEvent with a scope of PARSER_INVOCATION or ETERNAL 
public interface Consumer {
    void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone);
    
    void processDTD(DTDEvent event);
    
    void processDocumentType(String rootName, String publicId, String systemId);
    
    /**
     * Process an element in non namespace aware mode.
     * 
     * @param tagName the name of the element
     */
    void processElement(String tagName);
    
    /**
     * Process an element in namespace aware mode.
     * 
     * @param namespaceURI the namespace URI of the element, or <code>null</code> if the element has no
     *                     namespace
     * @param localName the local part of the element's name
     * @param prefix the prefix of the element, or <code>null</code> if the element has no prefix
     */
    void processElement(String namespaceURI, String localName, String prefix);
    
    /**
     * Process an attribute in non namespace aware mode.
     * 
     * @param name
     * @param value
     * @param type
     */
    void processAttribute(String name, String value, String type);
    
    /**
     * Process an attribute in namespace aware mode.
     * 
     * @param namespaceURI
     * @param localName
     * @param prefix
     * @param value
     * @param type
     */
    void processAttribute(String namespaceURI, String localName, String prefix, String value, String type);
    
    /**
     * Process a namespace declaration.
     * 
     * @param prefix
     *            the prefix being declared, or <code>null</code> if the declaration sets the
     *            default namespace
     * @param namespaceURI
     *            the namespace URI; this value must not be <code>null</code>
     */
    void processNSDecl(String prefix, String namespaceURI);
    
    void attributesCompleted();
    
    void processProcessingInstruction(String target, CharacterData data);
    void processText(CharacterData data);
    void processComment(CharacterData data);
    void processCDATASection(CharacterData data);
    void processEntityReference(String name);
    
    /**
     * Inform the consumer that the current element or the document is complete.
     */
    void nodeCompleted();
    
    void processEvent(Event event);
}
