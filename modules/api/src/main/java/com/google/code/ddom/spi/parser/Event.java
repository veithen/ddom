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

public interface Event {
    int DTD = 1;
    int DOM1_ELEMENT = 2;
    int DOM2_ELEMENT = 3;
    int NODE_COMPLETE = 4;
    int PROCESSING_INSTRUCTION = 5;
    int CHARACTERS = 6;
    int SPACE = 7;
    int CDATA = 8;
    int ENTITY_REFERENCE = 9;
    int COMMENT = 10;
    
    int DOM1_ATTRIBUTE = 1;
    int DOM2_ATTRIBUTE = 2;
    int NS_DECL = 3;
    
    int getEventType();

    int getAttributeCount();

    int getAttributeClass(int index);
    
    String getAttributeLocalName(int index);

    String getAttributeNamespace(int index);

    String getAttributePrefix(int index);

    String getAttributeValue(int index);
    
    String getAttributeType(int index);

    String getNamespacePrefix(int index);

    String getNamespaceURI(int index);

    String getLocalName();

    String getNamespaceURI();
    
    String getPrefix();

    String getPIData();

    String getPITarget();

    String getText();

    String getDTDPublicId();

    String getDTDRootName();

    String getDTDSystemId();
}
