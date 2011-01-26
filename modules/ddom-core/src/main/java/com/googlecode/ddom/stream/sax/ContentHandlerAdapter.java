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
package com.googlecode.ddom.stream.sax;

import javax.xml.XMLConstants;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

public class ContentHandlerAdapter implements ContentHandler, LexicalHandler {
    private final XmlHandler handler;
    private Locator locator;
    private boolean documentInfoProcessed;

    public ContentHandlerAdapter(XmlHandler handler) {
        this.handler = handler;
    }

    private void processDocumentInfo() {
        if (!documentInfoProcessed) {
            if (locator instanceof Locator2) {
                Locator2 locator2 = (Locator2)locator;
                // TODO: extract remaining info and build minimal info if locator doesn't implement Locator2
                handler.setDocumentInfo(locator2.getXMLVersion(), null, null, true);
            }
            documentInfoProcessed = true;
        }
    }
    
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startDocument() throws SAXException {
        // The document info provided by the Locator2 interface is not ready at this stage and needs
        // to be processed later. Do nothing here.
    }

    public void endDocument() throws SAXException {
        try {
            handler.completed();
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        processDocumentInfo();
        handler.processDocumentType(name, publicId, systemId, null);
    }

    public void endDTD() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        try {
            processDocumentInfo();
            if (localName.length() == 0) {
                handler.startElement(qName);
            } else {
                handler.startElement(SAXStreamUtils.normalizeNamespaceURI(uri), localName, SAXStreamUtils.getPrefixFromQName(qName));
            }
    
            int length = atts.getLength();
            for (int i=0; i<length; i++) {
                String attQName = atts.getQName(i);
                String attLocalName = atts.getLocalName(i);
                if (attLocalName.length() == 0) {
                    handler.startAttribute(attQName, atts.getType(i));
                } else {
                    String attUri = atts.getURI(i);
                    if (attUri.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
                        handler.startNamespaceDeclaration(SAXStreamUtils.getDeclaredPrefixFromQName(attQName));
                    } else {
                        handler.startAttribute(
                                SAXStreamUtils.normalizeNamespaceURI(attUri),
                                atts.getLocalName(i),
                                SAXStreamUtils.getPrefixFromQName(attQName),
                                atts.getType(i));
                    }
                }
                String value = atts.getValue(i);
                if (value.length() > 0) {
                    handler.processText(value, false);
                }
                handler.endAttribute();
            }
            handler.attributesCompleted();
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            handler.endElement();
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void startCDATA() throws SAXException {
        try {
            handler.startCDATASection();
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void endCDATA() throws SAXException {
        try {
            handler.endCDATASection();
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            handler.processText(new String(ch, start, length), false);
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            handler.processText(new String(ch, start, length), true);
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        try {
            handler.processComment(new String(ch, start, length));
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void processingInstruction(String piTarget, String piData) throws SAXException {
        try {
            handler.processProcessingInstruction(piTarget, piData);
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
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
