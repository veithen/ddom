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
package com.googlecode.ddom.frontend.axiom.support;

import java.io.IOException;
import java.io.Reader;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;

public class TextFromElementReader extends Reader {
    private final PullTextExtractor extractor;
    
    public TextFromElementReader(XmlInput input) {
        extractor = new PullTextExtractor();
        new Stream(input, extractor);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        try {
            return extractor.read(cbuf, off, len);
        } catch (StreamException ex) {
            IOException ioException = new IOException();
            ioException.initCause(ex);
            throw ioException;
        }
    }

    @Override
    public void close() throws IOException {
        // TODO
    }
}
