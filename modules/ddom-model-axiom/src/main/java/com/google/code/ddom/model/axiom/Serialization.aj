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
package com.google.code.ddom.model.axiom;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMOutputFormat;

public aspect Serialization {
    public void AxiomNode.serialize(XMLStreamWriter xmlWriter) throws XMLStreamException {
        serialize(xmlWriter, true);
    }

    public void AxiomNode.serializeAndConsume(XMLStreamWriter xmlWriter) throws XMLStreamException {
        serialize(xmlWriter, false);
    }
    
    public void AxiomNode.serialize(XMLStreamWriter xmlWriter, boolean cache) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomContainer.serialize(OutputStream output) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomContainer.serialize(Writer writer) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomContainer.serialize(OutputStream output, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomContainer.serialize(Writer writer, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomContainer.serializeAndConsume(OutputStream output) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomContainer.serializeAndConsume(Writer writer) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomContainer.serializeAndConsume(OutputStream output, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomContainer.serializeAndConsume(Writer writer, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
