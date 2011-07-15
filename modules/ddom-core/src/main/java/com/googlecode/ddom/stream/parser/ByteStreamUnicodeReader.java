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

/**
 * Base class for {@link UnicodeReader} implementations that read and decode data from an
 * {@link InputStream}.
 * 
 * @author Andreas Veithen
 */
public abstract class ByteStreamUnicodeReader implements UnicodeReader {
    public interface Factory {
        ByteStreamUnicodeReader create(InputStream in);
        ByteStreamUnicodeReader create(ByteStreamUnicodeReader other);
    }
    
    private final InputStream in;
    private final byte[] buffer;
    private int pos;
    private int len;

    public ByteStreamUnicodeReader(InputStream in) {
        this.in = in;
        buffer = new byte[4096];
    }
    
    public ByteStreamUnicodeReader(ByteStreamUnicodeReader other) {
        in = other.in;
        buffer = other.buffer;
        pos = other.pos;
        len = other.len;
    }
    
    protected int readByte() throws IOException {
        if (pos == len) {
            pos = 0;
            int count = in.read(buffer);
            if (count == -1) {
                len = 0;
                return -1;
            } else {
                len = count;
            }
        }
        return (int)buffer[pos++] & 0xFF;
    }
    
    public static Factory getFactory(String encoding) {
        if (encoding.equalsIgnoreCase("UTF-8")) {
            return UTF8Reader.FACTORY;
        } else if (encoding.equalsIgnoreCase("US-ASCII") || encoding.equalsIgnoreCase("ASCII")) {
            return ASCIIReader.FACTORY;
        } else if (encoding.equalsIgnoreCase("ISO-8859-1")) {
            return Latin1Reader.FACTORY;
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
    }
}
