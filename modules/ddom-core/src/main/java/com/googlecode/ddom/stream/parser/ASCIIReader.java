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

public class ASCIIReader extends ByteStreamUnicodeReader {
    public static final Factory FACTORY = new Factory() {
        public ByteStreamUnicodeReader create(InputStream in) {
            return new ASCIIReader(in);
        }

        public ByteStreamUnicodeReader create(ByteStreamUnicodeReader other) {
            return new ASCIIReader(other);
        }
    };
    
    public ASCIIReader(InputStream in) {
        super(in);
    }

    public ASCIIReader(ByteStreamUnicodeReader other) {
        super(other);
    }

    public int read() throws IOException {
        int c = readByte();
        if (c >= 128) {
            throw new IOException("Illegal character");
        }
        return c;
    }
}
