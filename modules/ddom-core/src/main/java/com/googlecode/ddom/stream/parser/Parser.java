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
    private char[] nameBuffer = new char[32];
    private int nameLength;
    
    public Parser(UnicodeReader reader) {
        this.reader = reader;
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
    
    private void appendNameChar(int c) {
        
    }
    
    private static boolean isNameStartChar(int c) {
        // TODO
        return true;
    }
    
    private static boolean isNameChar(int c) {
        // TODO
        return true;
    }
}
