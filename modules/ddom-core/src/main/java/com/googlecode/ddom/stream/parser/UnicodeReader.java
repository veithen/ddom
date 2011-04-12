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

/**
 * Interface for reading character streams. An implementation of this interface produces a sequence
 * of Unicode code points. This is different from {@link java.io.Reader}, which produces a sequence
 * of 16 bit words representing an UTF-16 encoded character stream.
 * 
 * @author Andreas Veithen
 */
public interface UnicodeReader {
    /**
     * Read a character from the stream.
     * 
     * @return the Unicode code point of the character, or <code>-1</code> if the end of the stream
     *         has been reached
     * @throws IOException
     *             if an I/O error occurs
     */
    int read() throws IOException;
}
