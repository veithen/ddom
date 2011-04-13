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
import java.io.Reader;

public class ReaderAdapter implements UnicodeReader {
    private final Reader reader;
    private final char[] buffer = new char[4096];
    private int pos;
    private int len;

    public ReaderAdapter(Reader reader) {
        this.reader = reader;
    }
    
    private int readChar() throws IOException {
        if (pos == len) {
            pos = 0;
            int count = reader.read(buffer);
            if (count == -1) {
                len = 0;
                return -1;
            }
        }
        return buffer[pos++];
    }
    
    public int read() throws IOException {
        int c = readChar();
        if (c == -1) {
            return -1;
        } else {
            if (Character.isHighSurrogate((char)c)) {
                int c2 = readChar();
                if (c2 == -1) {
                    throw new IOException("Unexpected end of stream after high surrogate character");
                } else if (Character.isLowSurrogate((char)c2)) {
                    return Character.toCodePoint((char)c, (char)c2);
                } else {
                    throw new IOException("Invalid surrogate pair");
                }
            } else {
                return c;
            }
        }
    }
}
