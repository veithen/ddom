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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.DeferredParsingException;
import com.google.code.ddom.spi.parser.ParserListener;
import com.google.code.ddom.spi.parser.ParserWrapper;

public class StAXParserWrapper implements ParserWrapper {
    private final XMLStreamReader reader;
    private final XMLStreamReaderEvent event;

    public StAXParserWrapper(XMLStreamReader reader) {
        this.reader = reader;
        event = new XMLStreamReaderEvent(reader);
    }

    public void proceed(ParserListener listener) throws DeferredParsingException {
        try {
            reader.next();
        } catch (XMLStreamException ex) {
            throw new DeferredParsingException("Parse error", ex);
        }
        listener.newEvent(event);
    }
}
