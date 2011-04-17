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
import java.io.InputStream;

// TODO: we don't cover all cases of invalid byte sequences, but the specs require us to do so
public class UTF8Reader extends ByteStreamUnicodeReader {
    public static final Factory FACTORY = new Factory() {
        public ByteStreamUnicodeReader create(InputStream in) {
            return new UTF8Reader(in);
        }

        public ByteStreamUnicodeReader create(ByteStreamUnicodeReader other) {
            return new UTF8Reader(other);
        }
    };
    
    public UTF8Reader(InputStream in) {
        super(in);
    }
    
    public UTF8Reader(ByteStreamUnicodeReader other) {
        super(other);
    }

    private int readContinuationByte() throws IOException {
        int b = readByte();
        if (b == -1) {
            throw new IOException("Unexpected end of stream in multibyte sequence");
        }
        if ((b & 0xC0) != 0x80) {
            throw new IOException("Invalid byte in multibyte sequence");
        }
        return b;
    }
    
    public int read() throws IOException {
        int b = readByte();
        if (b < 0x80) {
            // UTF-8:      0xxxxxxx
            // Code point: 0xxxxxxx
            // This also covers the end of stream case
            return b;
        } else if ((b & 0xE0) == 0xC0) {
            // UTF-8:      110yyyyy 10xxxxxx
            // Code point: 00000yyy yyxxxxxx
            return ((b & 0x1F) << 6) | (readContinuationByte() & 0x3F);
        } else if ((b & 0xF0) == 0xE0) {
            // UTF-8:      1110zzzz 10yyyyyy 10xxxxxx
            // Code point: zzzzyyyy yyxxxxxx
            return ((b & 0x0F) << 12) | ((readContinuationByte() & 0x3F) << 6) | (readContinuationByte() & 0x3F);
        } else if ((b & 0xF8) == 0xF0) {
            // UTF-8:      11110www 10zzzzzz 10yyyyyy 10xxxxxx
            // Code point: 000wwwzz zzzzyyyy yyxxxxxx
            return ((b & 0x07) << 16) | ((readContinuationByte() & 0x3F) << 12) | ((readContinuationByte() & 0x3F) << 6) | (readContinuationByte() & 0x3F);
        } else {
            throw new IOException("Invalid UTF-8 byte");
        }
    }
}
