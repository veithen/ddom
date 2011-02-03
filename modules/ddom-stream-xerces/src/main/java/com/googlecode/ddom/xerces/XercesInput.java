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
package com.googlecode.ddom.xerces;

import java.io.IOException;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.symbols.Symbols;
import com.googlecode.ddom.xerces.parsers.XML11Configuration;
import com.googlecode.ddom.xerces.xni.XNIException;
import com.googlecode.ddom.xerces.xni.parser.XMLInputSource;
import com.googlecode.ddom.xerces.xni.parser.XMLPullParserConfiguration;

public class XercesInput extends XmlInput {
    private final XMLPullParserConfiguration config;
    
    public XercesInput(XMLInputSource is) throws IOException {
        config = new XML11Configuration();
        config.setDocumentHandler(new XercesAdapter(getHandler()));
        config.setInputSource(is);
    }

    public Symbols getSymbols() {
        // TODO
        return null;
    }

    protected void proceed() throws StreamException {
        try {
            config.parse(false);
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
