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
package com.google.code.ddom.stream.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.stream.spi.AttributeMode;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;

public class StAXParser implements Producer {
    private final XMLStreamReader reader;
    private final XMLStreamReaderEvent event; // TODO: maybe this should be an inner class

    public StAXParser(XMLStreamReader reader) {
        this.reader = reader;
        event = new XMLStreamReaderEvent(reader);
    }

    public void proceed(Consumer consumer) throws StreamException {
        XMLStreamReaderEvent.Mode mode = event.getMode();
        if (consumer.getAttributeMode() == AttributeMode.EVENT) {
            int index = event.getIndex();
            switch (mode) {
                case ATTRIBUTES_COMPLETE:
                    mode = XMLStreamReaderEvent.Mode.NODE;
                    break;
                case NODE:
                    if (!reader.isStartElement()) {
                        break;
                    } else {
                        mode = XMLStreamReaderEvent.Mode.ATTRIBUTE;
                        index = -1;
                        // Fall through
                    }
                case ATTRIBUTE:
                    if (++index < reader.getAttributeCount()) {
                        break;
                    } else {
                        mode = XMLStreamReaderEvent.Mode.NS_DECL;
                        index = -1;
                        // Fall through
                    }
                case NS_DECL:
                    if (++index < reader.getNamespaceCount()) {
                        break;
                    } else {
                        mode = XMLStreamReaderEvent.Mode.ATTRIBUTES_COMPLETE;
                    }
            }
            event.updateState(mode, index);
        }
        if (mode == XMLStreamReaderEvent.Mode.NODE) {
            try {
                reader.next();
            } catch (XMLStreamException ex) {
                throw new StreamException(ex);
            }
        }
        consumer.processEvent(event);
    }

    public void dispose() {
        // TODO: this doesn't close the stream
        try {
            reader.close();
        } catch (XMLStreamException ex) {
            // Ignore this; we can't do more.
        }
    }
}
