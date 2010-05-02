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
package com.google.code.ddom.frontend.axiom.aspects;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMOutputFormat;

import com.google.code.ddom.frontend.axiom.intf.AxiomLeafNode;

public aspect LeafNodeSupport {
    public boolean AxiomLeafNode.isComplete() {
        // A leaf node is always complete
        return true;
    }
    
    public void AxiomLeafNode.build() {
        // Do nothing: a leaf node is always complete
    }

    public void AxiomLeafNode.serialize(@SuppressWarnings("unused") OutputStream output) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public void AxiomLeafNode.serialize(@SuppressWarnings("unused") Writer writer) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public void AxiomLeafNode.serialize(@SuppressWarnings("unused") OutputStream output, @SuppressWarnings("unused") OMOutputFormat format) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public void AxiomLeafNode.serialize(@SuppressWarnings("unused") Writer writer, @SuppressWarnings("unused") OMOutputFormat format) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public void AxiomLeafNode.serializeAndConsume(@SuppressWarnings("unused") OutputStream output) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public void AxiomLeafNode.serializeAndConsume(@SuppressWarnings("unused") Writer writer) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }
    
    public void AxiomLeafNode.serializeAndConsume(@SuppressWarnings("unused") OutputStream output, @SuppressWarnings("unused") OMOutputFormat format) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }

    public void AxiomLeafNode.serializeAndConsume(@SuppressWarnings("unused") Writer writer, @SuppressWarnings("unused") OMOutputFormat format) throws XMLStreamException {
        throw new UnsupportedOperationException();
    }
}