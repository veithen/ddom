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

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlOutput;

public class Serializer extends XmlOutput {
    private final UnicodeWriter writer;
    
    public Serializer(Writer writer) {
        this.writer = new WriterAdapter(writer);
    }

    public Serializer(OutputStream out, String encoding) throws UnsupportedEncodingException {
        if (encoding.equalsIgnoreCase("UTF-8")) {
            writer = new UTF8Writer(out);
        } else if (encoding.equalsIgnoreCase("US-ASCII")) {
            writer = new ASCIIWriter(out);
        } else if (encoding.equalsIgnoreCase("ISO-8859-1")) {
            writer = new Latin1Writer(out);
        } else {
            // TODO: support other encodings as well (via ICU4J)
            throw new UnsupportedEncodingException("Encoding " + encoding + " is unsupported");
        }
    }

    @Override
    protected XmlHandler createXmlHandler() {
        return new SerializerHandler(writer);
    }
}
