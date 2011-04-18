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
package com.googlecode.ddom.stream.serializer;

import java.io.IOException;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class SerializerHandler implements XmlHandler {
    private static final int STATE_CONTENT = 0;
    private static final int STATE_EMPTY_ELEMENT = 1;
    private static final int STATE_ATTRIBUTE = 2;
    private static final int STATE_UNESCAPED = 3;
    
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
        // TODO
//        throw new UnsupportedOperationException();
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

    private void doStartAttribute(String prefix, String localName) throws StreamException {
        try {
            writer.write(' ');
            if (prefix.length() > 0) {
                writer.write(prefix);
                writer.write(':');
            }
            writer.write(localName);
            writer.write("=\"");
            state = STATE_ATTRIBUTE;
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }
    
    public void startAttribute(String name, String type) throws StreamException {
        doStartAttribute("", name);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        doStartAttribute(prefix, localName);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        if (prefix.length() == 0) {
            doStartAttribute("", "xmlns");
        } else {
            doStartAttribute("xmlns", prefix);
        }
    }

    public void endAttribute() throws StreamException {
        try {
            writer.write('"');
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void attributesCompleted() throws StreamException {
        state = STATE_EMPTY_ELEMENT;
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        try {
            finishStartElement();
            int len = data.length();
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
                if (state == STATE_ATTRIBUTE && codePoint == '"'
                        || state == STATE_CONTENT && codePoint == '<') {
                    switch (codePoint) {
                        case '"':
                            writer.write("&quot;");
                            break;
                        case '<':
                            writer.write("&lt;");
                            break;
                        default:
                            writer.write("&#");
                            // TODO: optimize; we don't need to create a String object here
                            writer.write(Integer.toString(codePoint));
                            writer.write(';');
                    }
                } else {
                    writer.write(codePoint);
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

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.SimpleXmlOutput#processEntityReference(java.lang.String)
     */
    public void processEntityReference(String name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void completed() throws StreamException {
        try {
            writer.flushBuffer();
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }
}
