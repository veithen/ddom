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

import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.symbols.Symbols;

class NSAwareElementHandler extends ElementHandler {
    NSAwareElementHandler(Symbols symbols, XmlHandler handler) {
        super(symbols, handler);
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.parser.ElementHandler#handleEndAttribute()
     */
    void handleEndAttribute() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.parser.ElementHandler#handleEndElement(char[], int)
     */
    void handleEndElement(char[] name, int len) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.parser.ElementHandler#handleStartAttribute(char[], int)
     */
    void handleStartAttribute(char[] name, int len) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.parser.ElementHandler#handleStartElement(char[], int)
     */
    void handleStartElement(char[] name, int len) {
        // TODO
        throw new UnsupportedOperationException();
    }

}
