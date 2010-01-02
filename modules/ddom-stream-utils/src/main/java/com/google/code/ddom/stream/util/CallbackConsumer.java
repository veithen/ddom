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
package com.google.code.ddom.stream.util;

import com.google.code.ddom.stream.spi.AttributeMode;
import com.google.code.ddom.stream.spi.Consumer;
import com.google.code.ddom.stream.spi.Event;

/**
 * Abstract base class for callback style consumers. This class implements the
 * {@link Consumer#processEvent(Event)} method so that events are delivered to individual methods.
 * 
 * @author Andreas Veithen
 */
public abstract class CallbackConsumer implements Consumer {
    private final AttributeMode attributeMode;
    
    public CallbackConsumer(AttributeMode attributeMode) {
        this.attributeMode = attributeMode;
    }

    public final AttributeMode getAttributeMode() {
        return attributeMode;
    }

    public final void processEvent(Event event) {
        switch (event.getEventType()) {
            case DTD:
                processDocumentType(event.getDTDRootName(), event.getDTDPublicId(), event.getDTDSystemId());
                break;
            case NS_UNAWARE_ELEMENT:
                processElement(event.getName(), attributeMode == AttributeMode.ELEMENT ? event.getAttributes() : null);
                break;
            case NS_AWARE_ELEMENT:
                processElement(event.getNamespaceURI(), event.getName(), event.getPrefix(), attributeMode == AttributeMode.ELEMENT ? event.getAttributes() : null);
                break;
            case NS_UNAWARE_ATTRIBUTE:
                processAttribute(event.getName(), event.getValue(), event.getDataType());
                break;
            case NS_AWARE_ATTRIBUTE:
                processAttribute(event.getNamespaceURI(), event.getName(), event.getPrefix(), event.getValue(), event.getDataType());
                break;
            case NAMESPACE_DECLARATION:
                processNSDecl(event.getPrefix(), event.getNamespaceURI());
                break;
            case ATTRIBUTES_COMPLETE:
                attributesCompleted();
                break;
            case NODE_COMPLETE:
                nodeCompleted();
                break;
            case PROCESSING_INSTRUCTION:
                processProcessingInstruction(event.getName(), event.getData());
                break;
            case CHARACTERS:
            case SPACE:
                processText(event.getData());
                break;
            case CDATA:
                processCDATASection(event.getData());
                break;
            case ENTITY_REFERENCE:
                processEntityReference(event.getName());
                break;
            case COMMENT:
                processComment(event.getData());
                break;
            default:
                throw new RuntimeException("Unexpected event " + event.getEventType()); // TODO
        }
    }
}
