/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
    private String[] nameStack = new String[16];
    private int depth;
    
    public NSUnawareElementHandler(Symbols symbols, XmlHandler handler) {
        super(symbols, handler);
    }

    @Override
    boolean pushPendingEvent() throws StreamException {
        return false;
    }

    @Override
    boolean handleStartElement(char[] name, int len) throws StreamException {
        String n = symbols.getSymbol(name, 0, len);
        if (depth == nameStack.length) {
            String[] newNameStack = new String[nameStack.length*2];
            System.arraycopy(nameStack, 0, newNameStack, 0, nameStack.length);
            nameStack = newNameStack;
        }
        nameStack[depth++] = n;
        handler.startElement(n);
        return true;
    }

    @Override
    boolean handleStartAttribute(char[] name, int len) throws StreamException {
        handler.startAttribute(symbols.getSymbol(name, 0, len), "CDATA");
        return true;
    }
    
    @Override
    boolean handleCharacterData(String data) throws StreamException {
        handler.processCharacterData(data, false);
        return true;
    }

    @Override
    boolean handleEndAttribute() throws StreamException {
        handler.endAttribute();
        return true;
    }

    @Override
    void attributesCompleted() throws StreamException {
        handler.attributesCompleted();
    }

    @Override
    void handleEndElement(char[] name, int len) throws StreamException {
        depth--;
        if (name != null) {
            String n = nameStack[depth];
            if (len != n.length() || !Utils.equals(n, name, 0)) {
                throw new XmlSyntaxException("The element type \"" + n + "\" must be terminated by the matching end-tag \"</" + n + ">\"");
            }
        }
        handler.endElement();
    }
}
