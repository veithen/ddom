/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.stax;

import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.spi.parser.ParserWrapper;
import com.google.code.ddom.spi.parser.Source;

public class StAXSource implements Source {
    private final XMLStreamReader reader;
    private boolean consumed;

    public StAXSource(XMLStreamReader reader) {
        this.reader = reader;
    }

    public ParserWrapper getParser() {
        if (consumed) {
            throw new IllegalStateException("This source has already been consumed");
        }
        return new StAXParserWrapper(reader);
    }
}
