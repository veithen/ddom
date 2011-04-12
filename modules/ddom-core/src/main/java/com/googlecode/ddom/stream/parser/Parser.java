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

import com.googlecode.ddom.symbols.SymbolHashTable;
import com.googlecode.ddom.symbols.Symbols;

public class Parser {
    private final int STATE_CONTENT = 1;
    
    /**
     * The last character was a '&lt;'.
     */
    private final int STATE_MARKUP_START = 2;
    
    private final Symbols symbols = new SymbolHashTable();
    private final UnicodeReader reader;
    
    private int state;
    private int nextChar = -2;
    private char[] nameBuffer = new char[32];
    private int nameLength;
    
    public Parser(UnicodeReader reader) {
        this.reader = reader;
    }

    private int peek() throws IOException {
        if (nextChar == -2) {
            nextChar = reader.read();
        }
        return nextChar;
    }
    
    private void consume() {
        nextChar = -2;
    }
    
    public void parse() throws IOException {
        outer: while (true) {
            switch (state) {
                case STATE_CONTENT:
                    int len = 0;
                    loop: while (true) {
                        int c = reader.read();
                        switch (c) {
                            case '<':
                                state = STATE_MARKUP_START;
                                break loop;
                            case '&':
                                
                        }
                    }
                    if (len > 0) {
                        // TODO: generate event
                        break outer;
                    } else {
                        break;
                    }
                case STATE_MARKUP_START:
                    int c = reader.read();
                    if (c == '!') {
                        // TODO
                    } else if (isNameStartChar(c)) {
                        do {
                            appendNameChar(c);
                            c = reader.read();
                        } while (isNameChar(c));
                        
                    }
            }
        }
    }
    
    private void parseMarkup() {
        
    }
    
    private void appendNameChar(int c) {
        
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
}
