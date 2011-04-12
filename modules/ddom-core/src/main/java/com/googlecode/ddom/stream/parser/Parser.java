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
    private final int STATE_NONE = 1;
    
    private final Symbols symbols = new SymbolHashTable();
    private final UnicodeReader reader;
    
    private char[] nameBuffer = new char[32];
    
    public Parser(UnicodeReader reader) {
        this.reader = reader;
    }

    public void parse() throws IOException {
        while (true) {
            int c = reader.read();
            
        }
    }
}
