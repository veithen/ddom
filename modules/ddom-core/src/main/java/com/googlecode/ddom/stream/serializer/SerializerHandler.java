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
package com.googlecode.ddom.stream.serializer;

import java.io.IOException;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class SerializerHandler implements XmlHandler {
    private static final int STATE_CONTENT = 0;
    private static final int STATE_EMPTY_ELEMENT = 1;
    private static final int STATE_ATTRIBUTE = 2;
    
    /**
     * The serializer is writing the content of a CDATA section, comment or processing instruction.
     */
    private static final int STATE_UNESCAPED = 3;
    
    /**
     * The serializer is writing an namespace declaration with a prefix and for which no content has
     * been written yet.
     */
    private static final int STATE_EMPTY_PREFIXED_NS_DECL = 4;
    
    private final UnicodeWriter writer;
    private int depth;
    private String[] elementNameStack = new String[16];
    private int state = STATE_CONTENT;
    
    SerializerHandler(UnicodeWriter writer) {
        this.writer = writer;
    }
    
    public void startEntity(boolean fragment, String inputEncoding) {
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) throws StreamException {
        try {
            writer.write("<?xml version=\"");
            // TODO: clarify if version is mandatory or not
            writer.write(version == null ? "1.0" : version);
            writer.write("\"");
            if (encoding != null) {
                writer.write(" encoding=\"");
                writer.write(encoding);
                writer.write('\"');
            }
            if (standalone != null) {
                writer.write(standalone ? " standalone=\"yes\"" : " standalone=\"no\"");
            }
            writer.write("?>");
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        try {
            writer.write("<!DOCTYPE ");
            writer.write(rootName);
            if (publicId != null) {
                writer.write(" PUBLIC \"");
                writer.write(publicId);
                writer.write('"');
            }
            if (systemId != null) {
                writer.write(" SYSTEM \"");
                writer.write(systemId);
                writer.write('"');
            }
            writer.write(">");
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void endDocumentTypeDeclaration() throws StreamException {
        // TODO
//        throw new UnsupportedOperationException();
    }

    private void finishStartElement() throws IOException {
        if (state == STATE_EMPTY_ELEMENT) {
            writer.write('>');
            state = STATE_CONTENT;
        }
    }
    
    private void doStartElement(String prefix, String localName) throws StreamException {
        try {
            finishStartElement();
            if (elementNameStack.length == 2*depth) {
                String[] newElementNameStack = new String[elementNameStack.length*2];
                System.arraycopy(elementNameStack, 0, newElementNameStack, 0, elementNameStack.length);
                elementNameStack = newElementNameStack;
            }
            elementNameStack[depth*2] = prefix;
            elementNameStack[depth*2+1] = localName;
            depth++;
            writer.write('<');
            if (prefix.length() > 0) {
                writer.write(prefix);
                writer.write(':');
            }
            writer.write(localName);
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }
    
    public void startElement(String tagName) throws StreamException {
        doStartElement("", tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        doStartElement(prefix, localName);
    }

    public void endElement() throws StreamException {
        try {
            depth--;
            if (state == STATE_EMPTY_ELEMENT) {
                writer.write("/>");
                state = STATE_CONTENT;
            } else {
                writer.write("</");
                String prefix = elementNameStack[depth*2];
                if (prefix.length() > 0) {
                    writer.write(prefix);
                    writer.write(':');
                }
                writer.write(elementNameStack[depth*2+1]);
                writer.write('>');
            }
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    private void writeAttribute(String prefix, String localName) throws StreamException {
        try {
            writer.write(' ');
            if (prefix.length() > 0) {
                writer.write(prefix);
                writer.write(':');
            }
            writer.write(localName);
            writer.write("=\"");
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }
    
    public void startAttribute(String name, String type) throws StreamException {
        writeAttribute("", name);
        state = STATE_ATTRIBUTE;
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        writeAttribute(prefix, localName);
        state = STATE_ATTRIBUTE;
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        if (prefix.length() == 0) {
            writeAttribute("", "xmlns");
            state = STATE_ATTRIBUTE;
        } else {
            writeAttribute("xmlns", prefix);
            // TODO: actually this only applies to XML 1.0
            state = STATE_EMPTY_PREFIXED_NS_DECL;
        }
    }

    public void endAttribute() throws StreamException {
        if (state == STATE_EMPTY_PREFIXED_NS_DECL) {
            throw new StreamException("Invalid namespace declaration: Prefixed namespace bindings may not be empty.");
        }
        try {
            writer.write('"');
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void resolveElementNamespace(String namespaceURI) throws StreamException {
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
    }

    public void attributesCompleted() throws StreamException {
        state = STATE_EMPTY_ELEMENT;
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        try {
            finishStartElement();
            int len = data.length();
            if (len > 0) {
                if (state == STATE_EMPTY_PREFIXED_NS_DECL) {
                    state = STATE_ATTRIBUTE;
                }
                int pos = 0;
                while (pos < len) {
                    char c = data.charAt(pos);
                    int codePoint;
                    if (Character.isHighSurrogate(c)) {
                        codePoint = Character.toCodePoint(c, data.charAt(pos+1));
                        pos += 2;
                    } else {
                        codePoint = c;
                        pos++;
                    }
                    switch (codePoint) {
                        case '"':
                            if (state == STATE_ATTRIBUTE) {
                                writer.write("&quot;");
                            } else {
                                writer.write(codePoint);
                            }
                            break;
                        case '<':
                            if (state == STATE_CONTENT) {
                                writer.write("&lt;");
                            } else {
                                writer.write(codePoint);
                            }
                            break;
                        case '&':
                            writer.write("&amp;");
                            break;
                        default:
                            if (writer.canEncode(codePoint)) {
                                writer.write(codePoint);
                            } else {
                                writer.write("&#");
                                // TODO: optimize; we don't need to create a String object here
                                writer.write(Integer.toString(codePoint));
                                writer.write(';');
                            }
                    }
                }
            }
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void startProcessingInstruction(String target) throws StreamException {
        try {
            finishStartElement();
            writer.write("<?");
            writer.write(target);
            writer.write(' ');
            state = STATE_UNESCAPED;
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void endProcessingInstruction() throws StreamException {
        try {
            writer.write("?>");
            state = STATE_CONTENT;
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void startComment() throws StreamException {
        try {
            finishStartElement();
            writer.write("<!--");
            state = STATE_UNESCAPED;
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void endComment() throws StreamException {
        try {
            writer.write("-->");
            state = STATE_CONTENT;
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void startCDATASection() throws StreamException {
        try {
            finishStartElement();
            writer.write("<![CDATA[");
            state = STATE_UNESCAPED;
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void endCDATASection() throws StreamException {
        try {
            writer.write("]]>");
            state = STATE_CONTENT;
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void processEntityReference(String name) throws StreamException {
        try {
            writer.write('&');
            writer.write(name);
            writer.write(';');
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void completed() throws StreamException {
        try {
            writer.flushBuffer();
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }
}
