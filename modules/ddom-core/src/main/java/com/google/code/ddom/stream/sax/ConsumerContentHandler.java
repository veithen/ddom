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
import org.xml.sax.ext.LexicalHandler;

import com.google.code.ddom.stream.spi.AttributeMode;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.util.CharArrayCharacterData;
import com.google.code.ddom.stream.util.StringCharacterData;

public class ConsumerContentHandler implements ContentHandler, LexicalHandler {
    private final Consumer consumer;
    private boolean inCDATA;
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

    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        consumer.processDocumentType(name, publicId, systemId);
    }

    public void endDTD() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        SAXAttributeData data;
        if (consumer.getAttributeMode() == AttributeMode.ELEMENT) {
            data = attributeData;
            if (attributeData == null) {
                data = new SAXAttributeData();
                attributeData = data;
            }
            data.setData(atts);
        } else {
            data = null;
        }
        
        if (localName.length() == 0) {
            consumer.processElement(qName, data);
        } else {
            consumer.processElement(SAXStreamUtils.normalizeNamespaceURI(uri), localName, SAXStreamUtils.getPrefixFromQName(qName), data);
        }

        if (data == null) {
            int length = atts.getLength();
            for (int i=0; i<length; i++) {
                String attLocalName = atts.getLocalName(i);
                if (attLocalName.length() == 0) {
                    consumer.processAttribute(qName, atts.getValue(i), atts.getType(i));
                } else {
                    String attUri = atts.getURI(i);
                    if (attUri.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
                        consumer.processNSDecl(SAXStreamUtils.getDeclaredPrefixFromQName(qName), atts.getValue(i));
                    } else {
                        consumer.processAttribute(
                                SAXStreamUtils.normalizeNamespaceURI(attUri),
                                atts.getLocalName(i),
                                SAXStreamUtils.getPrefixFromQName(qName),
                                atts.getValue(i),
                                atts.getType(i));
                    }
                }
            }
            consumer.attributesCompleted();
        }
        
        if (data != null) {
            data.clear();
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        consumer.nodeCompleted();
    }

    public void startCDATA() throws SAXException {
        inCDATA = true;
    }

    public void endCDATA() throws SAXException {
        inCDATA = false;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        CharArrayCharacterData data = charArrayData;
        if (data == null) {
            data = new CharArrayCharacterData();
            charArrayData = data;
        }
        data.setData(ch, start, length);
        if (inCDATA) {
            consumer.processCDATASection(data);
        } else {
            consumer.processText(data);
        }
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

    public void comment(char[] ch, int start, int length) throws SAXException {
        CharArrayCharacterData data = charArrayData;
        if (data == null) {
            data = new CharArrayCharacterData();
            charArrayData = data;
        }
        data.setData(ch, start, length);
        consumer.processComment(data);
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

    public void startEntity(String name) throws SAXException {
        // TODO Auto-generated method stub
        
    }

    public void endEntity(String name) throws SAXException {
        // TODO Auto-generated method stub
        
    }

    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub
        
    }
}
