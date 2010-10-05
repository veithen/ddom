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
package com.google.code.ddom.stream.spi.buffer;

import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.buffer.Event.Type;

/**
 * 
 * 
 * Note: {@link Event} objects returned by {@link #peek()}, {@link #peek(int)}, etc. are only valid until
 * they are consumed!
 * 
 * @author Andreas Veithen
 */
public class EventBuffer implements Consumer {
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

    public void processElement(String tagName) {
        newEvent().init(Type.NS_UNAWARE_ELEMENT, tagName, null, null, null, null);
    }

    public void processElement(String namespaceURI, String localName, String prefix) {
        newEvent().init(Type.NS_AWARE_ELEMENT, localName, namespaceURI, prefix, null, null);
    }

    public void processAttribute(String name, String value, String type) {
        newEvent().init(Type.NS_UNAWARE_ATTRIBUTE, name, null, null, value, type);
    }

    public void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        newEvent().init(Type.NS_AWARE_ATTRIBUTE, localName, namespaceURI, prefix, value, type);
    }

    public void processNamespaceDeclaration(String prefix, String namespaceURI) {
        newEvent().init(Type.NAMESPACE_DECLARATION, null, namespaceURI, prefix, null, null);
    }

    public void attributesCompleted() {
        newEvent().init(Type.ATTRIBUTES_COMPLETE, null, null, null, null, null);
    }

    public void nodeCompleted() {
        newEvent().init(Type.NODE_COMPLETE, null, null, null, null, null);
    }

    public void processProcessingInstruction(String target, String data) {
        newEvent().init(Type.PROCESSING_INSTRUCTION, target, null, null, data, null);
    }

    public void processText(String data) {
        newEvent().init(Type.CHARACTERS, null, null, null, data, null);
    }

    public void processCDATASection(String data) {
        newEvent().init(Type.CDATA, null, null, null, data, null);
    }

    public void processEntityReference(String name) {
        newEvent().init(Type.ENTITY_REFERENCE, name, null, null, null, null);
    }

    public void processComment(String data) {
        newEvent().init(Type.COMMENT, null, null, null, data, null);
    }

    public void processDocumentType(String rootName, String publicId, String systemId) {
        // TODO Auto-generated method stub
        
    }

    public void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding,
            boolean standalone) {
        // TODO Auto-generated method stub
        
    }
}
