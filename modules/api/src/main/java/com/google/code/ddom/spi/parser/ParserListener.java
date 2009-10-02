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
package com.google.code.ddom.spi.parser;

public interface ParserListener {
    void newDocumentType(String rootName, String publicId, String systemId);
    ElementBuilder newElement(String tagName);
    ElementBuilder newElement(String namespaceURI, String localName, String prefix);
    void newProcessingInstruction(String target, String data);
    void newText(CharacterDataSource data);
    void newComment(CharacterDataSource data);
    void newCDATASection(CharacterDataSource data);
    void newEntityReference(String name);
    
    /**
     * Inform the consumer that the current element or the document is complete.
     */
    void nodeCompleted();
    
    void newEvent(Event event);
}
