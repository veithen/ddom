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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class UTF8ReaderTest {
    @Test
    public void testASCIIChars() throws Exception {
        UTF8Reader reader = new UTF8Reader(new ByteArrayInputStream("test".getBytes("ASCII")));
        assertEquals((int)'t', reader.read());
        assertEquals((int)'e', reader.read());
        assertEquals((int)'s', reader.read());
        assertEquals((int)'t', reader.read());
        assertEquals(-1, reader.read());
    }
    
    @Test
    public void testLatin1Char() throws Exception {
        UTF8Reader reader = new UTF8Reader(new ByteArrayInputStream("a\u00A2b".getBytes("UTF-8")));
        assertEquals((int)'a', reader.read());
        assertEquals(0x00A2, reader.read());
        assertEquals((int)'b', reader.read());
        assertEquals(-1, reader.read());
    }
    
    @Test
    public void testBasicMultilingualPlane() throws Exception {
        UTF8Reader reader = new UTF8Reader(new ByteArrayInputStream("a\u20ACb".getBytes("UTF-8")));
        assertEquals((int)'a', reader.read());
        assertEquals(0x20AC, reader.read());
        assertEquals((int)'b', reader.read());
        assertEquals(-1, reader.read());
    }
    
    @Test
    public void testSupplementaryCodePoint() throws Exception {
        UTF8Reader reader = new UTF8Reader(new ByteArrayInputStream("a\uD834\uDD1Eb".getBytes("UTF-8")));
        assertEquals((int)'a', reader.read());
        assertEquals(0x1D11E, reader.read());
        assertEquals((int)'b', reader.read());
        assertEquals(-1, reader.read());
    }
}
