/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.googlecode.ddom.xerces;

import java.io.IOException;

import com.google.code.ddom.stream.spi.Input;
import com.google.code.ddom.stream.spi.Output;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.Symbols;
import com.googlecode.ddom.xerces.parsers.XML11Configuration;
import com.googlecode.ddom.xerces.xni.XNIException;
import com.googlecode.ddom.xerces.xni.parser.XMLInputSource;
import com.googlecode.ddom.xerces.xni.parser.XMLPullParserConfiguration;

public class XercesInput implements Input {
    private final XMLPullParserConfiguration config;
    private final OutputHandler handler;
    
    public XercesInput(XMLInputSource is) throws IOException {
        config = new XML11Configuration();
        handler = new OutputHandler();
        config.setDocumentHandler(handler);
        config.setInputSource(is);
    }

    public Symbols getSymbols() {
        // TODO
        return null;
    }

    public boolean proceed(Output output) throws StreamException {
        handler.setOutput(output);
        try {
            return config.parse(false);
        } catch (XNIException ex) {
            throw new StreamException(ex);
        } catch (IOException ex) {
            throw new StreamException(ex);
        }
    }

    public void dispose() {
        config.cleanup();
    }
}
