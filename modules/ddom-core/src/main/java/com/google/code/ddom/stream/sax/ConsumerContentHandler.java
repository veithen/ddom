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
package com.google.code.ddom.stream.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.google.code.ddom.spi.stream.AttributeMode;
import com.google.code.ddom.spi.stream.Consumer;
import com.google.code.ddom.stream.util.CharArrayCharacterData;
import com.google.code.ddom.stream.util.StringCharacterData;

public class ConsumerContentHandler implements ContentHandler {
    private final Consumer consumer;
    private StringCharacterData stringData;
    private CharArrayCharacterData charArrayData;
    private SAXAttributeData attributeData;

    public ConsumerContentHandler(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
        consumer.nodeCompleted();
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (consumer.getAttributeMode() == AttributeMode.EVENT) {
            int length = atts.getLength();
            for (int i=0; i<length; i++) {
                
                // TODO
                
            }
        } else {
            SAXAttributeData data = attributeData;
            if (attributeData == null) {
                data = new SAXAttributeData();
                attributeData = data;
            }
            data.setData(atts);
            
            // TODO
            
            data.clear();
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        // TODO Auto-generated method stub
        
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        CharArrayCharacterData data = charArrayData;
        if (data == null) {
            data = new CharArrayCharacterData();
            charArrayData = data;
        }
        data.setData(ch, start, length);
        consumer.processText(data);
        data.clear();
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        CharArrayCharacterData data = charArrayData;
        if (data == null) {
            data = new CharArrayCharacterData();
            charArrayData = data;
        }
        data.setData(ch, start, length);
        consumer.processText(data);
        data.clear();
    }

    public void processingInstruction(String piTarget, String piData) throws SAXException {
        StringCharacterData data = stringData;
        if (data == null) {
            data = new StringCharacterData();
            stringData = data;
        }
        data.setData(piData);
        consumer.processProcessingInstruction(piTarget, data);
        data.clear();
    }

    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub
        
    }
}
