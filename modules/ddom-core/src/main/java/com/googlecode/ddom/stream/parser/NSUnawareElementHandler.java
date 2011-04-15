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

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.symbols.Symbols;

final class NSUnawareElementHandler extends ElementHandler {
    public NSUnawareElementHandler(Symbols symbols, XmlHandler handler) {
        super(symbols, handler);
    }

    @Override
    void handleStartElement(char[] name, int len) throws StreamException {
        handler.startElement(symbols.getSymbol(name, 0, len));
    }

    @Override
    void handleStartAttribute(char[] name, int len) throws StreamException {
        handler.startAttribute(symbols.getSymbol(name, 0, len), "CDATA");
    }
    
    @Override
    void handleCharacterData(String data) throws StreamException {
        handler.processCharacterData(data, false);
    }

    @Override
    void handleEndAttribute() throws StreamException {
        handler.endAttribute();
    }

    @Override
    void attributesCompleted() throws StreamException {
        handler.attributesCompleted();
    }

    @Override
    void handleEndElement(char[] name, int len) throws StreamException {
        // TODO: check that element name matches
        handler.endElement();
    }
}
