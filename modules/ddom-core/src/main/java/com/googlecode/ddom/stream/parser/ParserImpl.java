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
package com.googlecode.ddom.stream.parser;

import java.io.IOException;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlReader;
import com.googlecode.ddom.symbols.SymbolHashTable;
import com.googlecode.ddom.symbols.Symbols;

final class ParserImpl implements XmlReader {
    private static final int STATE_START_DOCUMENT = 0;
    
    private static final int STATE_DOCUMENT_CONTENT = 1;
    
    /**
     * The last character was a '&lt;'.
     */
    private static final int STATE_MARKUP = 2;
    
    private static final int STATE_INTERNAL_SUBSET_CONTENT = 3;
    
    /**
     * Parsing the content of an element.
     */
    private static final int STATE_ELEMENT_CONTENT = 4;
    
    /**
     * Parsing the attributes of a start tag.
     */
    private static final int STATE_START_ELEMENT = 5;
    
    /**
     * An empty element was encountered, but {@link XmlHandler#endElement()} has not been invoked
     * yet.
     */
    private static final int STATE_EMPTY_ELEMENT = 6;
    
    /**
     * Parsing the content of an attribute.
     */
    private static final int STATE_ATTRIBUTE_CONTENT = 7;
    
    /**
     * Parsing the content of a comment.
     */
    private static final int STATE_COMMENT_CONTENT = 8;
    
    private static final int STATE_END_COMMENT = 9;
    
    /**
     * Parsing the content of a processing instruction.
     */
    private static final int STATE_PI_CONTENT = 10;
    
    private static final int STATE_END_PI = 11;
    
    /**
     * Parsing the content of a CDATA section.
     */
    private static final int STATE_CDATA_SECTION_CONTENT = 12;
    
    private static final int STATE_END_CDATA_SECTION = 13;
    
    private final XmlHandler handler;
    private final Symbols symbols = new SymbolHashTable();
    private UnicodeReader reader;
    private String inputEncoding;
    private final ElementHandler elementHandler;
    
    private int state = STATE_START_DOCUMENT;
    private int nextChar = -2;
    private char[] nameBuffer = new char[32];
    private int nameLength;
    private int quoteChar;
    
    // TODO: replace by something more sophisticated
    private char[] textBuffer = new char[4096];
    private int textLength;
    
    public ParserImpl(XmlHandler handler, UnicodeReader reader, String inputEncoding, boolean namespaceAware) {
        this.handler = handler;
        this.reader = reader;
        this.inputEncoding = inputEncoding;
        elementHandler = namespaceAware ? new NSAwareElementHandler(symbols, handler) : new NSUnawareElementHandler(symbols, handler);
    }

    private int peek() throws StreamException {
        if (nextChar == -2) {
            try {
                nextChar = reader.read();
            } catch (IOException ex) {
                throw new StreamException(ex);
            }
        }
        return nextChar;
    }
    
    private void consume() {
        nextChar = -2;
    }
    
    private int read() throws StreamException {
        if (nextChar != -2) {
            int c = nextChar;
            nextChar = -2;
            return c;
        } else {
            try {
                return reader.read();
            } catch (IOException ex) {
                throw new StreamException(ex);
            }
        }
    }
    
    private void skipWhitespace() throws StreamException {
        while (isWhitespace(peek())) {
            consume();
        }
    }
    
    public void proceed(boolean flush) throws StreamException {
        if (elementHandler.pushPendingEvent()) {
            return;
        }
        boolean eventProduced;
        do {
            eventProduced = true; // TODO
            switch (state) {
                case STATE_START_DOCUMENT:
                    // TODO: need to implement encoding detection here
                    parseStartDocument();
                    break;
                case STATE_MARKUP:
                    parseMarkup();
                    break;
                case STATE_INTERNAL_SUBSET_CONTENT:
                    parseInternalSubset();
                    break;
                case STATE_DOCUMENT_CONTENT: // TODO
                case STATE_ELEMENT_CONTENT:
                    eventProduced = parseElementContent();
                    break;
                case STATE_START_ELEMENT:
                    eventProduced = parseAttribute();
                    break;
                case STATE_EMPTY_ELEMENT:
                    elementHandler.handleEndElement(null, 0); // TODO
                    state = STATE_ELEMENT_CONTENT; // TODO: not always correct
                    break;
                case STATE_ATTRIBUTE_CONTENT:
                    eventProduced = parseAttributeContent();
                    break;
                case STATE_COMMENT_CONTENT: {
                    int result = parseDelimitedContent("-->", 2, 0);
                    eventProduced = (result & 1) != 0;
                    if ((result & 2) != 0) {
                        state = STATE_END_COMMENT;
                    }
                    break;
                }
                case STATE_END_COMMENT:
                    handler.endComment();
                    state = STATE_ELEMENT_CONTENT;
                    break;
                case STATE_PI_CONTENT: {
                    int result = parseDelimitedContent("?>", -1, 0);
                    eventProduced = (result & 1) != 0;
                    if ((result & 2) != 0) {
                        state = STATE_END_PI;
                    }
                    break;
                }
                case STATE_END_PI:
                    handler.endProcessingInstruction();
                    state = STATE_ELEMENT_CONTENT;
                    break;
                case STATE_CDATA_SECTION_CONTENT: {
                    int result = parseDelimitedContent("]]>", -1, 2);
                    eventProduced = (result & 1) != 0;
                    if ((result & 2) != 0) {
                        state = STATE_END_CDATA_SECTION;
                    }
                    break;
                }
                case STATE_END_CDATA_SECTION:
                    handler.endCDATASection();
                    state = STATE_ELEMENT_CONTENT;
                    break;
            }
        } while (!eventProduced);
    }
    
    private void parseStartDocument() throws StreamException {
        int c = peek();
        if (c == '<') {
            consume();
            c = peek();
            if (c == '?') {
                consume();
                // TODO: the document may also start with a processing instruction that is not an XML declaration
                parseXmlDeclaration();
                state = STATE_DOCUMENT_CONTENT;
            } else {
                handler.startEntity(false, inputEncoding);
                state = STATE_MARKUP;
            }
        } else {
            handler.startEntity(false, inputEncoding);
            state = STATE_DOCUMENT_CONTENT;
        }
    }
    
    private void parseXmlDeclaration() throws StreamException {
        // XMLDecl      ::= '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
        // VersionInfo  ::= S 'version' Eq ("'" VersionNum "'" | '"' VersionNum '"')
        // Eq           ::= S? '=' S?
        // VersionNum   ::= '1.' [0-9]+
        // EncodingDecl ::= S 'encoding' Eq ('"' EncName '"' | "'" EncName "'" )
        // SDDecl       ::= S 'standalone' Eq (("'" ('yes' | 'no') "'") | ('"' ('yes' | 'no') '"'))
        parseName();
        if (checkName("xml")) {
            skipWhitespace();
            parseName();
            if (!checkName("version")) {
                throw new StreamException("Expected 'version' pseudo-attribute");
            }
            String version = parsePseudoAttribute();
            String encoding;
            String standalone;
            skipWhitespace();
            if (peek() == '?') {
                consume();
                encoding = null;
                standalone = null;
            } else {
                parseName();
                if (checkName("encoding")) {
                    encoding = parsePseudoAttribute();
                    skipWhitespace();
                    if (peek() == '?') {
                        standalone = null;
                    } else {
                        parseName();
                        if (!checkName("standalone")) {
                            throw new StreamException("Unexpected pseudo attribute");
                        }
                        standalone = parsePseudoAttribute();
                    }
                } else if (checkName("standalone")) {
                    encoding = null;
                    standalone = parsePseudoAttribute();
                } else {
                    throw new StreamException("Unexpected pseudo attribute");
                }
                skipWhitespace();
                if (read() != '?') {
                    throw new StreamException("Expected '?'");
                }
            }
            if (read() != '>') {
                throw new StreamException("Expected '>'");
            }
            if (inputEncoding == null && reader instanceof ByteStreamUnicodeReader) {
                if (encoding == null) {
                    inputEncoding = "UTF-8";
                } else {
                    inputEncoding = encoding.toUpperCase(); // TODO: quick & dirty hack for compatibility with Woodstox
                    if (encoding.equalsIgnoreCase("utf-8")) {
                        // Do nothing: we use an UTF8Reader by default
                    } else {
                        reader = ByteStreamUnicodeReader.getFactory(encoding).create((ByteStreamUnicodeReader)reader);
                    }
                }
            }
            handler.startEntity(false, inputEncoding);
            handler.processXmlDeclaration(version, encoding, standalone == null ? null : Boolean.valueOf(standalone.equals("yes")));
        } else {
            throw new StreamException("Expected XML declaration");
        }
    }
    
    private String parsePseudoAttribute() throws StreamException {
        StringBuilder buffer = new StringBuilder();
        skipWhitespace();
        if (read() != '=') {
            throw new StreamException("Expected '='");
        }
        skipWhitespace();
        int quoteChar = read();
        if (quoteChar != '\'' && quoteChar != '"') {
            throw new StreamException("Expected quote");
        }
        int c;
        while ((c = read()) != quoteChar) {
            if (c < 32 || c >= 128) {
                throw new StreamException("Illegal character in XML declaration pseudo attribute");
            }
            buffer.append((char)c);
        }
        return buffer.toString();
    }
    
    private void parseDocumentContent() throws StreamException {
        
    }
    
    private boolean parseElementContent() throws StreamException {
        while (true) {
            int c = peek();
            switch (c) {
                case '<':
                    if (flushText(false)) {
                        return true;
                    } else {
                        consume();
                        return parseMarkup();
                    }
                case -1:
                    // TODO: check depth!
                    if (flushText(false)) {
                        return true;
                    } else {
                        handler.completed();
                        return true;
                    }
                case 0xD:
                    // TODO: also implement this case for other types of content
                    consume();
                    if (peek() == 0xA) {
                        consume();
                    }
                    processCharacterData(0xA); // TODO: actually code unit;
                    break;
                case '&':
                    consume();
                    parseEntityRef();
                    break;
                default:
                    consume();
                    // TODO: The character sequence "]]>" must not appear in content unless used to mark the end of a CDATA section.
                    // CharData ::= [^<&]* - ([^<&]* ']]>' [^<&]*)
                    processCharacterData(c);
            }
        }
    }
    
    private boolean parseAttributeContent() throws StreamException {
        while (true) {
            int c = peek();
            switch (c) {
                case '<':
                    throw new StreamException("'<' not allowed in attribute value");
                case -1:
                    throw new StreamException("Unexpected end of stream in attribute value");
                case 0xD:
                case 0xA:
                case 0x9:
                    consume();
                    processCharacterData(0x20);
                    break;
                case '&':
                    consume();
                    parseEntityRef();
                    break;
                default:
                    if (c == quoteChar) {
                        if (flushText(true)) {
                            return true;
                        } else {
                            consume();
                            state = STATE_START_ELEMENT;
                            return elementHandler.handleEndAttribute();
                        }
                    } else {
                        consume();
                        processCharacterData(c);
                    }
            }
        }
    }
    
    private void processCharacterData(int c) {
        // TODO: check buffer overflow
        // TODO: handle supplemental characters
        textBuffer[textLength++] = (char)c;
    }
    
    private void parseEntityRef() throws StreamException {
        if (peek() == '#') {
            consume();
            if (peek() == 'x') {
                consume();
                int ref = 0;
                int c;
                while ((c = read()) != ';') {
                    if ('0' <= c && c <= '9') {
                        ref = (ref << 4) + c - '0';
                    } else if ('a' <= c && c <= 'f') {
                        ref = (ref << 4) + c - 'a' + 10;
                    } else if ('A' <= c && c <= 'F') {
                        ref = (ref << 4) + c - 'A' + 10;
                    }
                }
                processCharacterData(ref);
            } else {
                int ref = 0;
                int c;
                while ((c = read()) != ';') {
                    if (c < '0' || c > '9') {
                        throw new StreamException("Illegal character in character reference");
                    }
                    ref = ref*10 + c - '0';
                }
                processCharacterData(ref);
            }
        } else {
            parseName();
            if (read() != ';') {
                throw new StreamException("Expected ';'");
            }
            if (checkName("lt")) {
                processCharacterData('<');
            } else if (checkName("gt")) {
                processCharacterData('>');
            } else if (checkName("amp")) {
                processCharacterData('&');
            } else if (checkName("apos")) {
                processCharacterData('\'');
            } else if (checkName("quot")) {
                processCharacterData('"');
            } else {
                throw new UnsupportedOperationException(); // TODO
            }
        }
    }
    
    private boolean flushText(boolean attributeContent) throws StreamException {
        if (textLength > 0) {
            boolean eventProduced;
            if (attributeContent) {
                eventProduced = elementHandler.handleCharacterData(new String(textBuffer, 0, textLength));
            } else {
                handler.processCharacterData(new String(textBuffer, 0, textLength), false);
                eventProduced = true;
            }
            textLength = 0;
            return eventProduced;
        } else {
            return false;
        }
    }
    
    private boolean parseMarkup() throws StreamException {
        int c = peek();
        switch (c) {
            case '!':
                consume();
                c = peek();
                switch (c) {
                    case '-':
                        parseComment();
                        break;
                    case '[':
                        parseCDATASection();
                        break;
                    default:
                        parseDeclaration();
                }
                break;
            case '?':
                consume();
                parsePI();
                break;
            case '/':
                consume();
                parseEndElement();
                break;
            default:
                return parseStartElement();
        }
        return true; // TODO
    }
    
    private boolean parseStartElement() throws StreamException {
        parseName();
        state = STATE_START_ELEMENT;
        return elementHandler.handleStartElement(nameBuffer, nameLength);
    }
    
    private boolean parseAttribute() throws StreamException {
        skipWhitespace();
        int c = peek();
        if (c == '>') {
            consume();
            elementHandler.attributesCompleted();
            state = STATE_ELEMENT_CONTENT;
            return true;
        } else if (c == '/') {
            consume();
            if (read() != '>') {
                throw new StreamException("Expected '>'");
            }
            elementHandler.attributesCompleted();
            state = STATE_EMPTY_ELEMENT;
            return true;
        } else if (isNameStartChar(c)) {
            parseName();
            // Whitespace is allowed here:
            //  Attribute ::= Name Eq AttValue
            //  Eq ::= S? '=' S?
            skipWhitespace();
            if (read() != '=') {
                throw new StreamException("Expected '='");
            }
            skipWhitespace();
            quoteChar = read();
            if (quoteChar != '\'' && quoteChar != '\"') {
                throw new StreamException("Expected quote");
            }
            state = STATE_ATTRIBUTE_CONTENT;
            return elementHandler.handleStartAttribute(nameBuffer, nameLength);
        } else {
            throw new StreamException("Expected start of attribute, but found '" + (char)c + "'");
        }
    }
    
    private void parseEndElement() throws StreamException {
        parseName();
        // There may be whitespace after the name:  ETag ::= '</' Name S? '>
        skipWhitespace();
        if (read() != '>') {
            throw new StreamException("Expected '>'");
        }
        elementHandler.handleEndElement(nameBuffer, nameLength);
        state = STATE_ELEMENT_CONTENT;
    }
    
    private void parseComment() throws StreamException {
        for (int i=0; i<2; i++) {
            if (read() != '-') {
                throw new StreamException("Expected '-'");
            }
        }
        handler.startComment();
        state = STATE_COMMENT_CONTENT;
    }
    
    private void parseCDATASection() throws StreamException {
        // TODO: test already done in parseMarkup
        if (read() != '[') {
            throw new StreamException("Invalid start of CDATA section");
        }
        parseName();
        if (!checkName("CDATA")) {
            throw new StreamException("Expected CDATA, but got '" + new String(nameBuffer, 0, nameLength) + "'");
        }
        if (read() != '[') {
            throw new StreamException("Invalid start of CDATA section");
        }
        handler.startCDATASection();
        state = STATE_CDATA_SECTION_CONTENT;
    }
    
    private void parsePI() throws StreamException {
        // PI ::= '<?' PITarget (S (Char* - (Char* '?>' Char*)))? '?>'
        parseName();
        // A processing instruction may be empty
        if (peek() != '?') {
            if (!isWhitespace(read())) {
                throw new StreamException("Illegal start of processing instruction");
            }
            // TODO: should we preserve additional whitespace? (StAX does not)
            skipWhitespace();
        }
        // TODO: check for invalid PI target (xml)
        handler.startProcessingInstruction(symbols.getSymbol(nameBuffer, 0, nameLength));
        state = STATE_PI_CONTENT;
    }
    
    private void parseDeclaration() throws StreamException {
        parseName();
        if (!checkName("DOCTYPE")) {
            throw new StreamException("Expected DOCTYPE");
        }
        skipWhitespace();
        parseName();
        String rootName = symbols.getSymbol(nameBuffer, 0, nameLength);
        skipWhitespace();
        // TODO: handle ExternalID here
        handler.startDocumentTypeDeclaration(rootName, null, null); // TODO
        if (peek() == '[') {
            consume();
            state = STATE_INTERNAL_SUBSET_CONTENT;
        }
    }
    
    private void parseInternalSubset() throws StreamException {
        // TODO
        int c;
        while ((c = read()) != ']') {
            // Loop
        }
        skipWhitespace();
        if (read() != '>') {
            throw new StreamException("Expected '>' after internal subset");
        }
        handler.endDocumentTypeDeclaration();
        state = STATE_DOCUMENT_CONTENT;
    }
    
    // 1 = An even has been produced, but the end has not been reached
    // 2 = The end has been reached without producing any event
    // 3 = The end has been reached and an event has been produced
    private int parseDelimitedContent(String delimiter, int matchThreshold, int fuzziness) throws StreamException {
        int matchLength = 0;
        while (true) {
            int c = peek();
            if (c == -1) {
                throw new StreamException("Unexpected end of stream");
            } else if (c == delimiter.charAt(matchLength)) {
                consume();
                matchLength++;
                if (matchThreshold > 0) {
                    if (matchLength == matchThreshold) {
                        for (; matchLength < delimiter.length(); matchLength++) {
                            if (read() != delimiter.charAt(matchLength)) {
                                throw new StreamException("Illegal character sequence \"" + delimiter.substring(0, matchThreshold) + " found in content");
                            }
                        }
                        return flushText(false) ? 3 : 2;
                    }
                } else if (matchLength == delimiter.length()) {
                    return flushText(false) ? 3 : 2;
                }
            } else if (matchLength > 0) {
                // TODO: we need to exit the loop here if processCharacterData produces an event
                if (matchLength == fuzziness) {
                    // This takes care of CDATA sections terminated by "]]]>" or "]]]]>"?
                    processCharacterData(delimiter.charAt(0));
                    matchLength--;
                } else {
                    for (int i=0; i<matchLength; i++) {
                        // TODO: optimize: don't need to handle supplementary code points here
                        processCharacterData(delimiter.charAt(i));
                    }
                    matchLength = 0;
                    // The current character may be part of the delimiter, so loop again. This occurs
                    // e.g. when a PI is terminated by "??>".
                }
            } else {
                consume();
                processCharacterData(c);
            }
        }
    }
    
    private void parseName() throws StreamException {
        nameLength = 0;
        int c = peek();
        if (isNameStartChar(c)) {
            do {
                consume();
                // TODO: incorrect for supplemental characters
                if (nameLength == nameBuffer.length) {
                    char[] newCharBuffer = new char[nameBuffer.length*2];
                    System.arraycopy(nameBuffer, 0, newCharBuffer, 0, nameBuffer.length);
                    nameBuffer = newCharBuffer;
                }
                // TODO: handle supplemental characters here
                nameBuffer[nameLength++] = (char)c;
                c = peek();
            } while (isNameChar(c));
        } else {
            throw new StreamException("Expected NameStartChar");
        }
    }
    
    private boolean checkName(String name) {
        if (name.length() == nameLength) {
            for (int i=0; i<nameLength; i++) {
                if (name.charAt(i) != nameBuffer[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check whether the given character matches the <tt>NameStartChar</tt> production in the XML
     * spec. Note that in the fifth edition of XML 1.0, this production has been drastically
     * simplified, so that it is no longer necessary to use a lookup table (as e.g. Xerces does) to
     * do this check efficiently.
     * 
     * @param c
     *            the character to check
     * @return <code>true</code> if the character matches the <tt>NameStartChar</tt> production,
     *         <code>false</code> otherwise
     */
    private static boolean isNameStartChar(int c) {
        // From XML 1.0 fifth edition:
        //
        // NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] |
        //                   [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] |
        //                   [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] |
        //                   [#x10000-#xEFFFF]
        //
        if (c < 'A') {
            return c == ':';
        } else if (c <= 'Z') {
            return true;
        } else if (c < 'a') {
            return c == '_';
        } else if (c <= 'z') {
            return true;
        } else if (c < 0xC0) {
            return false;
        } else if (c <= 0x2FF) {
            return c != 0xD7 && c != 0xF7;
        } else if (c < 0x370) {
            return false;
        } else if (c <= 0x1FFFF) {
            return c != 0x37E;
        } else if (c < 0x2070) {
            return c == 0x200C || c == 0x200D;
        } else if (c <= 0x218F) {
            return true;
        } else if (c < 0x2C00) {
            return false;
        } else if (c <=0x2FEF) {
            return true;
        } else if (c < 0x3001) {
            return false;
        } else if (c <= 0xD7FF) {
            return true;
        } else if (c < 0xF900) {
            return false;
        } else if (c <= 0xFDCF) {
            return true;
        } else if (c < 0xFDF0) {
            return false;
        } else if (c <= 0xFFFD) {
            return true;
        } else if (c < 0x10000) {
            return false;
        } else if (c <= 0xEFFFF) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check whether the given character matches the <tt>NameChar</tt> production in the XML spec.
     * 
     * @param c
     *            the character to check
     * @return <code>true</code> if the character matches the <tt>NameChar</tt> production,
     *         <code>false</code> otherwise
     */
    private static boolean isNameChar(int c) {
        // From XML 1.0 fifth edition:
        //
        // NameChar ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
        //
        // That means:
        //
        // NameChar ::= "-" | "." | [0-9] | ":" | [A-Z] | "_" | [a-z] | #xB7 | [#xC0-#xD6] |
        //              [#xD8-#xF6] | [#xF8-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x203F-#x2040]
        //              [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] |
        //              [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
        if (c < '0') {
            return c == '-' || c == '.';
        } else if (c <= ':') { // '9' and ':' are adjacent
            return true;
        } else if (c < 'A') {
            return false;
        } else if (c <= 'Z') {
            return true;
        } else if (c < 'a') {
            return c == '_';
        } else if (c <= 'z') {
            return true;
        } else if (c < 0xC0) {
            return c == 0xB7;
        } else if (c <= 0x1FFF) {
            return c != 0xD7 && c != 0xF7 && c != 0x37E;
        } else if (c < 0x2070) {
            return c == 0x200C || c == 0x200D || c == 0x203F || c == 0x2040;
        } else if (c <= 0x218F) {
            return true;
        } else if (c < 0x2C00) {
            return false;
        } else if (c <=0x2FEF) {
            return true;
        } else if (c < 0x3001) {
            return false;
        } else if (c <= 0xD7FF) {
            return true;
        } else if (c < 0xF900) {
            return false;
        } else if (c <= 0xFDCF) {
            return true;
        } else if (c < 0xFDF0) {
            return false;
        } else if (c <= 0xFFFD) {
            return true;
        } else if (c < 0x10000) {
            return false;
        } else if (c <= 0xEFFFF) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check whether the given character is whitespace as defined by the <tt>S</tt> production in
     * the XML spec.
     * 
     * @param c
     *            the character to check
     * @return <code>true</code> if the character is whitespace, <code>false</code> otherwise
     */
    private boolean isWhitespace(int c) {
        return c == 0x20 || c == 0x9 || c == 0xD || c == 0xA;
    }
}
