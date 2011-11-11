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
package com.googlecode.ddom.stream.stax;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlReader;

/**
 * {@link XmlInput} implementation that consumes events from an {@link XMLStreamReader}.
 * <p>
 * The supplied {@link XMLStreamReader} must be positioned on a
 * {@link XMLStreamConstants#START_DOCUMENT} or {@link XMLStreamConstants#START_ELEMENT} event. If
 * the current event is {@link XMLStreamConstants#START_DOCUMENT} then the implementation will
 * consume events up to the {@link XMLStreamConstants#END_DOCUMENT} event. If the current event is
 * {@link XMLStreamConstants#START_ELEMENT}, then the builder will consume events up to the
 * corresponding {@link XMLStreamConstants#END_ELEMENT}. After the input has been flushed, the
 * {@link XMLStreamReader} will be positioned on the event immediately following this
 * {@link XMLStreamConstants#END_ELEMENT} event. This means that this object can be used in a well
 * defined way to consume a fragment (corresponding to a single element) of the document represented
 * by the stream reader.
 */
public class StAXPullInput extends XmlInput {
    private final XMLStreamReader reader;

    /**
     * Constructor.
     * 
     * @param reader
     *            the stream reader to read the events from
     * @throws IllegalStateException
     *             if the reader is in an unexpected state
     */
    public StAXPullInput(XMLStreamReader reader) {
        int eventType = reader.getEventType();
        if (eventType != XMLStreamReader.START_DOCUMENT && eventType != XMLStreamReader.START_ELEMENT) {
            throw new IllegalStateException("Stream reader is in an unexpected state");
        }
        this.reader = reader;
    }

    @Override
    protected XmlReader createReader(XmlHandler handler) {
        return new StAXPullReader(handler, reader);
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
