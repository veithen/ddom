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
package com.google.code.ddom.stream.spi.buffer;

import com.google.code.ddom.stream.spi.SimpleXmlOutput;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.buffer.Event.Type;

/**
 * 
 * 
 * Note: {@link Event} objects returned by {@link #peek()}, {@link #peek(int)}, etc. are only valid until
 * they are consumed!
 * 
 * @author Andreas Veithen
 */
public class EventBuffer extends SimpleXmlOutput {
    private final Event[] buffer = new Event[16];
    private int currentIndex;
    private int nextIndex;
    
    private Event newEvent() {
        Event event = buffer[nextIndex];
        if (event == null) {
            event = new Event();
            buffer[nextIndex] = event;
        }
        nextIndex++;
        int currentSize = buffer.length;
        if (nextIndex == currentSize) {
            nextIndex = 0;
        }
        if (nextIndex == currentIndex) {
            Event[] newBuffer = new Event[currentSize*2];
            System.arraycopy(buffer, 0, newBuffer, 0, currentIndex);
            System.arraycopy(buffer, currentIndex, newBuffer, currentIndex+currentSize, currentSize-currentIndex);
            currentIndex += currentSize;
        }
        return event;
    }
    
    public Event peek() {
        return peek(0);
    }
    
    public Event peek(int lookAhead) {
        int currentSize = buffer.length;
        if (lookAhead > currentSize) {
            return null;
        } else {
            int index = (currentIndex + lookAhead) % currentSize;
            if (index >= nextIndex) {
                return null;
            } else {
                return buffer[index];
            }
        }
    }
    
    public void consume(int count) {
        // TODO: call Event#reset()!
        int currentSize = buffer.length;
        int available = (nextIndex - currentIndex) % currentSize;
        if (count > available) {
            throw new IllegalStateException("Trying to consume more events than available");
        } else if (count == available) {
            // The caller consumes all events in the buffer; in this case we update
            // nextIndex instead of currentIndex. This is an optimization to avoid
            // unnecessary allocations of Event objects.
            nextIndex = currentIndex;
        } else {
            currentIndex = (currentIndex + count) % currentSize;
        }
    }

    @Override
    protected void startElement(String tagName) {
        newEvent().init(Type.START_NS_UNAWARE_ELEMENT, tagName, null, null, null, null);
    }

    @Override
    protected void startElement(String namespaceURI, String localName, String prefix) {
        newEvent().init(Type.START_NS_AWARE_ELEMENT, localName, namespaceURI, prefix, null, null);
    }

    @Override
    protected void endElement() throws StreamException {
        newEvent().init(Type.END_ELEMENT, null, null, null, null, null);
    }

    @Override
    protected void startAttribute(String name, String type) {
        newEvent().init(Type.START_NS_UNAWARE_ATTRIBUTE, name, null, null, null, type);
    }

    @Override
    protected void startAttribute(String namespaceURI, String localName, String prefix, String type) {
        newEvent().init(Type.START_NS_AWARE_ATTRIBUTE, localName, namespaceURI, prefix, null, type);
    }

    @Override
    protected void startNamespaceDeclaration(String prefix) {
        newEvent().init(Type.START_NAMESPACE_DECLARATION, null, null, prefix, null, null);
    }

    @Override
    protected void endAttribute() throws StreamException {
        newEvent().init(Type.END_ATTRIBUTE, null, null, null, null, null);
    }

    @Override
    protected void attributesCompleted() {
        newEvent().init(Type.ATTRIBUTES_COMPLETE, null, null, null, null, null);
    }

    @Override
    protected void completed() {
        newEvent().init(Type.COMPLETED, null, null, null, null, null);
    }

    @Override
    protected void processProcessingInstruction(String target, String data) {
        newEvent().init(Type.PROCESSING_INSTRUCTION, target, null, null, data, null);
    }

    @Override
    protected void processText(String data) {
        newEvent().init(Type.CHARACTERS, null, null, null, data, null);
    }

    @Override
    protected void startCDATASection() {
        newEvent().init(Type.START_CDATA_SECTION, null, null, null, null, null);
    }

    @Override
    protected void endCDATASection() {
        newEvent().init(Type.END_CDATA_SECTION, null, null, null, null, null);
    }

    @Override
    protected void processEntityReference(String name) {
        newEvent().init(Type.ENTITY_REFERENCE, name, null, null, null, null);
    }

    @Override
    protected void processComment(String data) {
        newEvent().init(Type.COMMENT, null, null, null, data, null);
    }

    @Override
    protected void processDocumentType(String rootName, String publicId, String systemId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding,
            boolean standalone) {
        // TODO Auto-generated method stub
        
    }
}
