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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.serializer.Serializer;
import com.googlecode.ddom.symbols.SymbolHashTable;
import com.googlecode.ddom.symbols.Symbols;

public class Parser extends XmlInput {
    private static final int STATE_START_DOCUMENT = 0;
    
    private static final int STATE_DOCUMENT_CONTENT = 1;
    
    /**
     * The last character was a '&lt;'.
     */
    private static final int STATE_MARKUP = 2;
    
    /**
     * Parsing the content of an element.
     */
    private static final int STATE_ELEMENT_CONTENT = 3;
    
    /**
     * Parsing the attributes of a start tag.
     */
    private static final int STATE_START_ELEMENT = 4;
    
    /**
     * An empty element was encountered, but {@link XmlHandler#endElement()} has not been invoked
     * yet.
     */
    private static final int STATE_EMPTY_ELEMENT = 5;
    
    /**
     * Parsing the content of an attribute.
     */
    private static final int STATE_ATTRIBUTE_CONTENT = 6;
    
    /**
     * Parsing the content of a comment.
     */
    private static final int STATE_COMMENT_CONTENT = 7;
    
    /**
     * Parsing the content of a processing instruction.
     */
    private static final int STATE_PI_CONTENT = 8;
    
    /**
     * Parsing the content of a CDATA section.
     */
    private static final int STATE_CDATA_SECTION_CONTENT = 9;
    
    private final Symbols symbols = new SymbolHashTable();
    private UnicodeReader reader;
    private String inputEncoding;
    private final boolean namespaceAware;
    private ElementHandler elementHandler;
    
    private int state = STATE_START_DOCUMENT;
    private int nextChar = -2;
    private char[] nameBuffer = new char[32];
    private int nameLength;
    private int quoteChar;
    
    // TODO: replace by something more sophisticated
    private char[] textBuffer = new char[4096];
    private int textLength;
    
    public Parser(Reader reader, boolean namespaceAware) {
        this.reader = new ReaderAdapter(reader);
        this.namespaceAware = namespaceAware;
    }
    
    public Parser(InputStream in, String inputEncoding, boolean namespaceAware) {
        this.inputEncoding = inputEncoding;
        // TODO: extract the encoding detection logic because it also appears in parseXmlDeclaration
        if (inputEncoding == null || inputEncoding.equalsIgnoreCase("utf-8")) {
            reader = new UTF8Reader(in);
        } else if (inputEncoding.equalsIgnoreCase("ascii")) {
            reader = new ASCIIReader(in);
        } else if (inputEncoding.equalsIgnoreCase("iso-8859-1")) {
            reader = new Latin1Reader(in);
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
        this.namespaceAware = namespaceAware;
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
    
    @Override
    protected void proceed(boolean flush) throws StreamException {
        switch (state) {
            case STATE_START_DOCUMENT:
                // TODO: need to implement encoding detection here
                XmlHandler handler = getHandler();
                elementHandler = namespaceAware ? new NSAwareElementHandler(symbols, handler) : new NSUnawareElementHandler(symbols, handler);
                parseStartDocument();
                break;
            case STATE_MARKUP:
                parseMarkup();
                break;
            case STATE_DOCUMENT_CONTENT: // TODO
            case STATE_ELEMENT_CONTENT:
                parseElementContent();
                break;
            case STATE_START_ELEMENT:
                parseAttribute();
                break;
            case STATE_EMPTY_ELEMENT:
                elementHandler.handleEndElement(null, 0); // TODO
                state = STATE_ELEMENT_CONTENT; // TODO: not always correct
                break;
            case STATE_ATTRIBUTE_CONTENT:
                parseAttributeContent();
                break;
            case STATE_COMMENT_CONTENT:
                parseDelimitedContent("-->", 2);
                break;
            case STATE_PI_CONTENT:
                parseDelimitedContent("?>", -1);
                break;
            case STATE_CDATA_SECTION_CONTENT:
                parseDelimitedContent("]]>", -1);
                break;
        }
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
                getHandler().startEntity(false, inputEncoding);
                state = STATE_MARKUP;
            }
        } else {
            getHandler().startEntity(false, inputEncoding);
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
                    } else if (encoding.equalsIgnoreCase("ascii")) {
                        reader = new ASCIIReader((ByteStreamUnicodeReader)reader);
                    } else if (encoding.equalsIgnoreCase("iso-8859-1")) {
                        reader = new Latin1Reader((ByteStreamUnicodeReader)reader);
                    } else {
                        // TODO
                        throw new UnsupportedOperationException();
                    }
                }
            }
            getHandler().startEntity(false, inputEncoding);
            getHandler().processXmlDeclaration(version, encoding, standalone == null ? null : Boolean.valueOf(standalone.equals("yes")));
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
    
    private void parseElementContent() throws StreamException {
        while (true) {
            int c = peek();
            switch (c) {
                case '<':
                    if (flushText(false)) {
                        return;
                    } else {
                        consume();
                        parseMarkup();
                        return;
                    }
                case -1:
                    // TODO: check depth!
                    if (flushText(false)) {
                        return;
                    } else {
                        getHandler().completed();
                        return;
                    }
                case 0xD:
                    // TODO: also implement this case for other types of content
                    consume();
                    if (peek() == 0xA) {
                        consume();
                    }
                    processCharacterData(0xA); // TODO: actually code unit;
                    break;
                default:
                    consume();
                    // TODO: The character sequence "]]>" must not appear in content unless used to mark the end of a CDATA section.
                    // CharData ::= [^<&]* - ([^<&]* ']]>' [^<&]*)
                    processCharacterData(c);
            }
        }
    }
    
    private void parseAttributeContent() throws StreamException {
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
                default:
                    if (c == quoteChar) {
                        if (flushText(true)) {
                            return;
                        } else {
                            consume();
                            elementHandler.handleEndAttribute();
                            state = STATE_START_ELEMENT;
                            return;
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
    
    private boolean flushText(boolean attributeContent) throws StreamException {
        if (textLength > 0) {
            if (attributeContent) {
                elementHandler.handleCharacterData(new String(textBuffer, 0, textLength));
            } else {
                getHandler().processCharacterData(new String(textBuffer, 0, textLength), false);
            }
            textLength = 0;
            return true;
        } else {
            return false;
        }
    }
    
    private void parseMarkup() throws StreamException {
        int c = peek();
        switch (c) {
            case '!':
                consume();
                parseCommentOrCDATASection();
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
                parseStartElement();
        }
    }
    
    private void parseStartElement() throws StreamException {
        parseName();
        elementHandler.handleStartElement(nameBuffer, nameLength);
        state = STATE_START_ELEMENT;
    }
    
    private void parseAttribute() throws StreamException {
        skipWhitespace();
        int c = peek();
        if (c == '>') {
            consume();
            elementHandler.attributesCompleted();
            state = STATE_ELEMENT_CONTENT;
        } else if (c == '/') {
            consume();
            if (read() != '>') {
                throw new StreamException("Expected '>'");
            }
            elementHandler.attributesCompleted();
            state = STATE_EMPTY_ELEMENT;
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
            elementHandler.handleStartAttribute(nameBuffer, nameLength);
            state = STATE_ATTRIBUTE_CONTENT;
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
    
    private void parseCommentOrCDATASection() throws StreamException {
        if (peek() == '-') {
            parseComment();
        } else {
            parseCDATASection();
        }
    }
    
    private void parseComment() throws StreamException {
        for (int i=0; i<2; i++) {
            if (read() != '-') {
                throw new StreamException("Expected '-'");
            }
        }
        getHandler().startComment();
        state = STATE_COMMENT_CONTENT;
    }
    
    private void parseCDATASection() throws StreamException {
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
        getHandler().startCDATASection();
        state = STATE_CDATA_SECTION_CONTENT;
    }
    
    private void parsePI() throws StreamException {
        parseName();
        if (!isWhitespace(read())) {
            throw new StreamException("Illegal start of processing instruction");
        }
        // TODO: should we preserve additional whitespace? (StAX does not)
        skipWhitespace();
        // TODO: check for invalid PI target (xml)
        getHandler().startProcessingInstruction(symbols.getSymbol(nameBuffer, 0, nameLength));
        state = STATE_PI_CONTENT;
    }
    
    private void parseDelimitedContent(String delimiter, int matchThreshold) throws StreamException {
        int matchLength = 0;
        while (true) {
            int c = read();
            if (c == delimiter.charAt(matchLength)) {
                matchLength++;
                if (matchThreshold > 0) {
                    if (matchLength == matchThreshold) {
                        for (; matchLength < delimiter.length(); matchLength++) {
                            if (read() != delimiter.charAt(matchLength)) {
                                throw new StreamException("Illegal character sequence \"" + delimiter.substring(0, matchThreshold) + " found in content");
                            }
                        }
                        // TODO: this may produce two events...
                        flushText(false);
                        // TODO: this should be handled by the caller
                        getHandler().endComment();
                        // TODO: may also be STATE_DOCUMENT_CONTENT
                        state = STATE_ELEMENT_CONTENT;
                        return;
                    }
                } else if (matchLength == delimiter.length()) {
                    // TODO: this may produce two events...
                    flushText(false);
                    // TODO: this should be handled by the caller
                    switch (state) {
                        case STATE_PI_CONTENT:
                            getHandler().endProcessingInstruction();
                            break;
                        case STATE_CDATA_SECTION_CONTENT:
                            getHandler().endCDATASection();
                            break;
                    }
                    // TODO: may also be STATE_DOCUMENT_CONTENT
                    state = STATE_ELEMENT_CONTENT;
                    return;
                }
            } else {
                // TODO: we need to exit the loop here if processCharacterData produces an event
                if (matchLength > 0) {
                    for (int i=0; i<matchLength; i++) {
                        // TODO: optimize: don't need to handle supplementary code points here
                        processCharacterData(delimiter.charAt(matchLength));
                    }
                    matchLength = 0;
                }
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
    
    @Override
    public void dispose() {
        // TODO
    }

    public static void main(String[] args) throws Exception {
        new Stream(new Parser(new ByteArrayInputStream("<?xml version='1.0' encoding='utf-8'?><root attr=\"value\">text</root>".getBytes("utf-8")), null, true), new Serializer(System.out, "UTF-8")).flush();
    }
}
