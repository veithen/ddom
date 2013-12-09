/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
    private final boolean expandEntityReferences;
    private Locator locator;
    private boolean documentInfoProcessed;
    private String[] prefixMappings = new String[8];
    private int prefixCount;
    private int skipDepth;

    public ContentHandlerAdapter(XmlHandler handler, boolean expandEntityReferences) {
        this.handler = handler;
        this.expandEntityReferences = expandEntityReferences;
    }

    private void processDocumentInfo() throws SAXException {
        if (!documentInfoProcessed) {
            if (locator instanceof Locator2) {
                Locator2 locator2 = (Locator2)locator;
                // TODO: extract remaining info and build minimal info if locator doesn't implement Locator2
                try {
                    handler.processXmlDeclaration(locator2.getXMLVersion(), null, null);
                } catch (StreamException ex) {
                    throw new SAXException(ex);
                }
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
        try {
            handler.startDocumentTypeDeclaration(name, publicId, systemId);
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void endDTD() throws SAXException {
        try {
            handler.endDocumentTypeDeclaration();
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (skipDepth == 0) {
            if (prefixMappings.length == prefixCount*2) {
                String[] newPrefixMappings = new String[prefixMappings.length*2];
                System.arraycopy(prefixMappings, 0, newPrefixMappings, 0, prefixMappings.length);
                prefixMappings = newPrefixMappings;
            }
            prefixMappings[prefixCount*2] = prefix;
            prefixMappings[prefixCount*2+1] = uri;
            prefixCount++;
        }
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (skipDepth == 0) {
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
                    if (!attQName.startsWith("xmlns")) {
                        String attLocalName = atts.getLocalName(i);
                        if (attLocalName.length() == 0) {
                            handler.startAttribute(attQName, atts.getType(i));
                        } else {
                            handler.startAttribute(
                                    SAXStreamUtils.normalizeNamespaceURI(atts.getURI(i)),
                                    atts.getLocalName(i),
                                    SAXStreamUtils.getPrefixFromQName(attQName),
                                    atts.getType(i));
                        }
                        String value = atts.getValue(i);
                        if (value.length() > 0) {
                            handler.processCharacterData(value, false);
                        }
                        handler.endAttribute();
                    }
                }
                for (int i=0; i<prefixCount; i++) {
                    handler.startNamespaceDeclaration(prefixMappings[i*2]);
                    handler.processCharacterData(prefixMappings[i*2+1], false);
                    handler.endAttribute();
                }
                prefixCount = 0;
                handler.attributesCompleted();
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (skipDepth == 0) {
            try {
                handler.endElement();
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void startCDATA() throws SAXException {
        if (skipDepth == 0) {
            try {
                handler.startCDATASection();
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void endCDATA() throws SAXException {
        if (skipDepth == 0) {
            try {
                handler.endCDATASection();
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (skipDepth == 0) {
            try {
                handler.processCharacterData(new String(ch, start, length), false);
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (skipDepth == 0) {
            try {
                handler.processCharacterData(new String(ch, start, length), true);
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (skipDepth == 0) {
            try {
                // TODO: is this correct? or can SAX generate several calls to this method for the same comment?
                handler.startComment();
                handler.processCharacterData(new String(ch, start, length), false);
                handler.endComment();
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void processingInstruction(String piTarget, String piData) throws SAXException {
        if (skipDepth == 0) {
            try {
                handler.startProcessingInstruction(piTarget);
                handler.processCharacterData(piData, false);
                handler.endProcessingInstruction();
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void startEntity(String name) throws SAXException {
        if (skipDepth > 0) {
            skipDepth++;
        } else if (!expandEntityReferences && !name.equals("[dtd]")) {
            skipDepth = 1;
            try {
                handler.processEntityReference(name);
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }

    public void endEntity(String name) throws SAXException {
        if (skipDepth > 0) {
            skipDepth--;
        }
    }

    public void skippedEntity(String name) throws SAXException {
        if (skipDepth == 0) {
            try {
                handler.processEntityReference(name);
            } catch (StreamException ex) {
                throw new SAXException(ex);
            }
        }
    }
}
