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
import java.io.StringReader;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.serializer.Serializer;
import com.googlecode.ddom.symbols.SymbolHashTable;
import com.googlecode.ddom.symbols.Symbols;

public class Parser extends XmlInput {
    private final int STATE_START_DOCUMENT = 0;
    
    private final int STATE_CONTENT = 1;
    
    /**
     * The last character was a '&lt;'.
     */
    private final int STATE_MARKUP_START = 2;
    
    private final Symbols symbols = new SymbolHashTable();
    private final UnicodeReader reader;
    private final boolean namespaceAware;
    private ElementHandler elementHandler;
    
    private int state = STATE_START_DOCUMENT;
    private int nextChar = -2;
    private char[] nameBuffer = new char[32];
    private int nameLength;
    
    public Parser(UnicodeReader reader, boolean namespaceAware) {
        this.reader = reader;
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
    
    @Override
    protected void proceed(boolean flush) throws StreamException {
        switch (state) {
            case STATE_START_DOCUMENT:
                XmlHandler handler = getHandler();
                elementHandler = namespaceAware ? new NSAwareElementHandler(symbols, handler) : new NSUnawareElementHandler(symbols, handler);
                handler.startEntity(false, null); // TODO
                state = STATE_CONTENT;
                break;
            case STATE_CONTENT:
                parseContent();
                
            case STATE_MARKUP_START:
        }
    }
    
    private void parseContent() throws StreamException {
        while (true) {
            int c = peek();
            switch (c) {
                case '<':
                    consume();
                    parseMarkup();
            }
        }
    }
    
    private void parseMarkup() throws StreamException {
        int c = peek();
        switch (c) {
            case '!':
                consume();
                parseCDATASection();
                break;
            case '?':
                consume();
                parsePI();
                break;
            default:
                parseStartElement();
        }
    }
    
    private void parseStartElement() throws StreamException {
        parseName();
        // TODO: consume spaces
        elementHandler.handleStartElement(nameBuffer, nameLength);
    }
    
    private void parseCDATASection() {
        
    }
    
    private void parsePI() {
        
    }
    
    private void parseName() throws StreamException {
        int c = peek();
        if (isNameStartChar(c)) {
            do {
                consume();
                appendNameChar(c);
                c = peek();
            } while (isNameChar(c));
        } else {
            throw new StreamException("Expected NameStartChar");
        }
    }
    
    private void appendNameChar(int c) {
        // TODO: incorrect for supplemental characters
        if (nameLength == nameBuffer.length) {
            char[] newCharBuffer = new char[nameBuffer.length*2];
            System.arraycopy(nameBuffer, 0, newCharBuffer, 0, nameBuffer.length);
            nameBuffer = newCharBuffer;
        }
        // TODO: handle supplemental characters here
        nameBuffer[nameLength++] = (char)c;
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
    
    @Override
    public void dispose() {
        // TODO
    }

    public static void main(String[] args) throws Exception {
        new Stream(new Parser(new ReaderAdapter(new StringReader("<root>text</root>")), false), new Serializer(System.out, "UTF-8")).flush();
    }
}
