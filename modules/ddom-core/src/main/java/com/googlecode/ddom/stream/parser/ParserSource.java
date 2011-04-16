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

import java.io.InputStream;
import java.io.Reader;

import org.xml.sax.InputSource;

import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;

// TODO: allow specifying namespace awareness
public class ParserSource implements XmlSource {
    private final InputStream byteStream;
    private final String encoding;
    private final Reader characterStream;
    
    public ParserSource(InputSource is) {
        byteStream = is.getByteStream();
        encoding = is.getEncoding();
        characterStream = is.getCharacterStream();
        // TODO: handle InputSource objects specified by systemId
    }

    public XmlInput getInput(Hints hints) {
        if (byteStream != null) {
            return new Parser(byteStream, encoding, true);
        } else {
            return new Parser(characterStream, true);
        }
    }

    public boolean isDestructive() {
        // TODO
        return true;
    }
}
