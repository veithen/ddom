/*
 * Copyright 2013 Andreas Veithen
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
package com.googlecode.ddom.backend.testsuite;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class EventTrackerHandler implements XmlHandler {
    private final XmlHandler target;
    private final EventTracker tracker;

    EventTrackerHandler(XmlHandler target, EventTracker tracker) {
        super();
        this.target = target;
        this.tracker = tracker;
    }

    public void startEntity(boolean fragment, String inputEncoding) throws StreamException {
        tracker.lastEvent = fragment ? Event.START_FRAGMENT : Event.START_DOCUMENT;
        target.startEntity(fragment, inputEncoding);
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) throws StreamException {
        tracker.lastEvent = Event.XML_DECLARATION;
        target.processXmlDeclaration(version, encoding, standalone);
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        tracker.lastEvent = Event.START_DOCUMENT_TYPE_DECLARATION;
        target.startDocumentTypeDeclaration(rootName, publicId, systemId);
    }

    public void endDocumentTypeDeclaration() throws StreamException {
        tracker.lastEvent = Event.END_DOCUMENT_TYPE_DECLARATION;
        target.endDocumentTypeDeclaration();
    }

    public void startElement(String tagName) throws StreamException {
        tracker.lastEvent = Event.START_NS_UNAWARE_ELEMENT;
        target.startElement(tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        tracker.lastEvent = Event.START_NS_AWARE_ELEMENT;
        target.startElement(namespaceURI, localName, prefix);
    }

    public void endElement() throws StreamException {
        tracker.lastEvent = Event.END_ELEMENT;
        target.endElement();
    }

    public void startAttribute(String name, String type) throws StreamException {
        tracker.lastEvent = Event.START_NS_UNAWARE_ATTRIBUTE;
        target.startAttribute(name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        tracker.lastEvent = Event.START_NS_AWARE_ATTRIBUTE;
        target.startAttribute(namespaceURI, localName, prefix, type);
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        tracker.lastEvent = Event.START_NAMESPACE_DECLARATION;
        target.startNamespaceDeclaration(prefix);
    }

    public void endAttribute() throws StreamException {
        tracker.lastEvent = Event.END_ATTRIBUTE;
        target.endAttribute();
    }

    public void resolveElementNamespace(String namespaceURI) throws StreamException {
        // TODO: should we track this event?
        target.resolveElementNamespace(namespaceURI);
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        // TODO: should we track this event?
        target.resolveAttributeNamespace(index, namespaceURI);
    }

    public void attributesCompleted() throws StreamException {
        tracker.lastEvent = Event.ATTRIBUTES_COMPLETED;
        target.attributesCompleted();
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        tracker.lastEvent = Event.CHARACTER_DATA;
        target.processCharacterData(data, ignorable);
    }

    public void startProcessingInstruction(String piTarget) throws StreamException {
        tracker.lastEvent = Event.START_PROCESSING_INSTRUCTION;
        target.startProcessingInstruction(piTarget);
    }

    public void endProcessingInstruction() throws StreamException {
        tracker.lastEvent = Event.END_PROCESSING_INSTRUCTION;
        target.endProcessingInstruction();
    }

    public void startComment() throws StreamException {
        tracker.lastEvent = Event.START_COMMENT;
        target.startComment();
    }

    public void endComment() throws StreamException {
        tracker.lastEvent = Event.END_COMMENT;
        target.endComment();
    }

    public void startCDATASection() throws StreamException {
        tracker.lastEvent = Event.START_CDATA_SECTION;
        target.startCDATASection();
    }

    public void endCDATASection() throws StreamException {
        tracker.lastEvent = Event.END_CDATA_SECTION;
        target.endCDATASection();
    }

    public void processEntityReference(String name) throws StreamException {
        tracker.lastEvent = Event.ENTITY_REFERENCE;
        target.processEntityReference(name);
    }

    public void completed() throws StreamException {
        tracker.lastEvent = Event.COMPLETED;
        target.completed();
    }
}
