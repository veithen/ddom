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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlReader;
import com.googlecode.ddom.stream.serializer.Serializer;

public class Parser extends XmlInput {
    private final UnicodeReader reader;
    private final String inputEncoding;
    private final boolean namespaceAware;
    
    public Parser(Reader reader, boolean namespaceAware) {
        this.reader = new ReaderAdapter(reader);
        this.inputEncoding = null;
        this.namespaceAware = namespaceAware;
    }
    
    public Parser(InputStream in, String inputEncoding, boolean namespaceAware) {
        this.inputEncoding = inputEncoding;
        // TODO: extract the encoding detection logic because it also appears in parseXmlDeclaration
        if (inputEncoding == null || inputEncoding.equalsIgnoreCase("utf-8")) {
            reader = new UTF8Reader(in);
        } else if (inputEncoding.equalsIgnoreCase("ascii")) {
            reader = new ASCIIReader(in);
        } else if (inputEncoding.equalsIgnoreCase("iso-8859-1")) {
            reader = new Latin1Reader(in);
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
        this.namespaceAware = namespaceAware;
    }

    @Override
    protected XmlReader createReader(XmlHandler handler) {
        return new ParserImpl(handler, reader, inputEncoding, namespaceAware);
    }

    @Override
    public void dispose() {
        // TODO
    }

    public static void main(String[] args) throws Exception {
        new Stream(new Parser(new ByteArrayInputStream("<?xml version='1.0' encoding='utf-8'?><root attr=\"value\">text</root>".getBytes("utf-8")), null, true), new Serializer(System.out, "UTF-8")).flush();
    }
}
