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
package com.google.code.ddom.dom.builder;

import com.google.code.ddom.spi.parser.ElementBuilder;
import com.google.code.ddom.spi.parser.Event;
import com.google.code.ddom.spi.parser.ParserListener;

public abstract class PushParserListener implements ParserListener {
    public final void newEvent(Event event) {
        switch (event.getEventType()) {
            case Event.DTD:
                newDocumentType(event.getDTDRootName(), event.getDTDPublicId(), event.getDTDSystemId());
                break;
            case Event.DOM1_ELEMENT:
                buildElement(newElement(event.getLocalName()), event);
                break;
            case Event.DOM2_ELEMENT:
                buildElement(newElement(event.getNamespaceURI(), event.getLocalName(), event.getPrefix()), event);
                break;
            case Event.NODE_COMPLETE:
                nodeCompleted();
                break;
            case Event.PROCESSING_INSTRUCTION:
                newProcessingInstruction(event.getPITarget(), event.getPIData());
                break;
            case Event.CHARACTERS:
            case Event.SPACE:
                newText(event.getText());
                break;
            case Event.CDATA:
                newCDATASection(event.getText());
                break;
            case Event.ENTITY_REFERENCE:
                newEntityReference(event.getText());
                break;
            case Event.COMMENT:
                newComment(event.getText());
                break;
            default:
                throw new RuntimeException("Unexpected event " + event.getEventType()); // TODO
        }
    }
    
    private void buildElement(ElementBuilder builder, Event event) {
        for (int i=0; i<event.getAttributeCount(); i++) {
            switch (event.getAttributeClass(i)) {
                case Event.DOM1_ATTRIBUTE:
                    builder.newAttribute(event.getAttributeLocalName(i), event.getAttributeValue(i), event.getAttributeType(i));
                    break;
                case Event.DOM2_ATTRIBUTE:
                    builder.newAttribute(event.getAttributeNamespace(i), event.getAttributeLocalName(i), event.getAttributePrefix(i), event.getAttributeValue(i), event.getAttributeType(i));
                    break;
                case Event.NS_DECL:
                    builder.newNSDecl(event.getNamespacePrefix(i), event.getNamespaceURI(i));
                    break;
                default:
                    throw new RuntimeException("Unexpected attribute type " + event.getAttributeClass(i)); // TODO
            }
        }
    }
}
