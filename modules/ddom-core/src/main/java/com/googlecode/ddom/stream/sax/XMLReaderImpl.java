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
package com.googlecode.ddom.stream.sax;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlOutput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.XmlSource.Hints;
import com.googlecode.ddom.util.sax.AbstractXMLReader;

final class XMLReaderImpl extends AbstractXMLReader {
    private final XmlSource source;
    
    public XMLReaderImpl(XmlSource source) {
        this.source = source;
    }

    public void parse(InputSource input) throws IOException, SAXException {
        parse();
    }

    public void parse(String systemId) throws IOException, SAXException {
        parse();
    }
    
    private void parse() throws SAXException {
        XmlInput input = source.getInput(new Hints() {
            public boolean isPreferPush() {
                return true;
            }
        });
        XmlOutput output = new SAXOutput(contentHandler, lexicalHandler);
        try {
            new Stream(input, output).flush();
        } catch (StreamException ex) {
            throw new SAXException(ex);
        }
    }
}
