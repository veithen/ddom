/*
 * Copyright 2009-2010 Andreas Veithen
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
import org.xml.sax.ext.Locator2;

import com.google.code.ddom.stream.spi.Output;

public class OutputContentHandler implements ContentHandler, LexicalHandler {
    private final Output output;
    private Locator locator;
    private boolean documentInfoProcessed;

    public OutputContentHandler(Output output) {
        this.output = output;
    }

    private void processDocumentInfo() {
        if (!documentInfoProcessed) {
            if (locator instanceof Locator2) {
                Locator2 locator2 = (Locator2)locator;
                // TODO: extract remaining info and build minimal info if locator doesn't implement Locator2
                output.setDocumentInfo(locator2.getXMLVersion(), null, null, true);
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
        output.nodeCompleted();
    }

    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        processDocumentInfo();
        output.processDocumentType(name, publicId, systemId);
    }

    public void endDTD() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        processDocumentInfo();
        if (localName.length() == 0) {
            output.processElement(qName);
        } else {
            output.processElement(SAXStreamUtils.normalizeNamespaceURI(uri), localName, SAXStreamUtils.getPrefixFromQName(qName));
        }

        int length = atts.getLength();
        for (int i=0; i<length; i++) {
            String attQName = atts.getQName(i);
            String attLocalName = atts.getLocalName(i);
            if (attLocalName.length() == 0) {
                output.processAttribute(attQName, atts.getValue(i), atts.getType(i));
            } else {
                String attUri = atts.getURI(i);
                if (attUri.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
                    output.processNamespaceDeclaration(SAXStreamUtils.getDeclaredPrefixFromQName(attQName), atts.getValue(i));
                } else {
                    output.processAttribute(
                            SAXStreamUtils.normalizeNamespaceURI(attUri),
                            atts.getLocalName(i),
                            SAXStreamUtils.getPrefixFromQName(attQName),
                            atts.getValue(i),
                            atts.getType(i));
                }
            }
        }
        output.attributesCompleted();
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        output.nodeCompleted();
    }

    public void startCDATA() throws SAXException {
        output.processCDATASection();
    }

    public void endCDATA() throws SAXException {
        output.nodeCompleted();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        output.processText(new String(ch, start, length));
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        output.processText(new String(ch, start, length));
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        output.processComment(new String(ch, start, length));
    }

    public void processingInstruction(String piTarget, String piData) throws SAXException {
        output.processProcessingInstruction(piTarget, piData);
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
