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
import java.io.OutputStream;

public abstract class AbstractUnicodeWriter implements UnicodeWriter {
    private final OutputStream out;
    private final byte[] buffer = new byte[4096];
    private int len;
    
    public AbstractUnicodeWriter(OutputStream out) {
        this.out = out;
    }

    protected final void writeByte(byte b) throws IOException {
        if (len == buffer.length) {
            flushBuffer();
        }
        buffer[len++] = b;
    }
    
    public final void flushBuffer() throws IOException {
        out.write(buffer, 0, len);
        len = 0;
    }

    public final void write(String data) throws IOException {
        int len = data.length();
        int pos = 0;
        while (pos < len) {
            char c = data.charAt(pos);
            if (Character.isHighSurrogate(c)) {
                write(Character.toCodePoint(c, data.charAt(pos+1)));
                pos += 2;
            } else {
                write(c);
                pos++;
            }
        }
    }
}
