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

import javax.xml.XMLConstants;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.google.code.ddom.spi.stream.AttributeData;
import com.google.code.ddom.spi.stream.AttributeMode;
import com.google.code.ddom.spi.stream.CharacterData;
import com.google.code.ddom.spi.stream.Consumer;
import com.google.code.ddom.spi.stream.StreamException;

public class ConsumerContentHandler implements ContentHandler, AttributeData {
    private static class CharacterDataImpl1 implements CharacterData {
        public char[] buffer;
        public int start;
        public int length;
        
        public Scope getScope() {
            return Scope.CONSUMER_INVOCATION;
        }
        
        public String getString() throws StreamException {
            return new String(buffer, start, length);
        }
        
        public void clear() {
            buffer = null;
        }
    }
    
    private static class CharacterDataImpl2 implements CharacterData {
        public String data;
        
        public Scope getScope() {
            return Scope.CONSUMER_INVOCATION;
        }
        
        public String getString() throws StreamException {
            return data;
        }

        public void clear() {
            data = null;
        }
    }
    
    private final Consumer consumer;
    private final CharacterDataImpl1 cdata1 = new CharacterDataImpl1();
    private final CharacterDataImpl2 cdata2 = new CharacterDataImpl2();
    private Attributes atts;

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
            
        } else {
            this.atts = atts;
            
            
            atts = null;
        }
    }

    public Scope getScope() {
        return Scope.CONSUMER_INVOCATION;
    }

    public String getDataType(int index) {
        return atts.getType(index);
    }

    public int getLength() {
        return atts.getLength();
    }

    public String getName(int index) {
        String localName = atts.getLocalName(index);
        return localName.length() == 0 ? atts.getQName(index) : localName;
    }

    public String getNamespaceURI(int index) {
        String uri = atts.getURI(index);
        return uri.length() == 0 ? null : uri;
    }

    public String getPrefix(int index) {
        String qName = atts.getQName(index);
        int i = qName.indexOf(':');
        return i == -1 ? null : qName.substring(0, i);
    }

    public Type getType(int index) {
        if (atts.getLocalName(index).length() == 0) {
            return Type.DOM1;
        } else if (atts.getURI(index).equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return Type.NS_DECL;
        } else {
            return Type.DOM2;
        }
    }

    public String getValue(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        // TODO Auto-generated method stub
        
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        cdata1.buffer = ch;
        cdata1.start = start;
        cdata1.length = length;
        consumer.processText(cdata1);
        cdata1.clear();
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        cdata1.buffer = ch;
        cdata1.start = start;
        cdata1.length = length;
        consumer.processText(cdata1);
        cdata1.clear();
    }

    public void processingInstruction(String target, String data) throws SAXException {
        cdata2.data = data;
        consumer.processProcessingInstruction(target, cdata2);
        cdata2.clear();
    }

    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub
        
    }
}
