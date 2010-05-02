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
package com.google.code.ddom.frontend.axiom.aspects;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMOutputFormat;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.axiom.intf.AxiomContainer;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

public aspect ContainerSupport {
    public OMNode AxiomContainer.getFirstOMChild() {
        try {
            return (OMNode)coreGetFirstChild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public Iterator AxiomContainer.getChildren() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator AxiomContainer.getChildrenWithName(QName elementQName) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator AxiomContainer.getChildrenWithLocalName(String localName) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator AxiomContainer.getChildrenWithNamespaceURI(String uri) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMElement AxiomContainer.getFirstChildWithName(QName elementQName) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomContainer.addChild(OMNode omNode) {
        try {
            coreAppendChild((CoreChildNode)omNode);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public boolean AxiomContainer.isComplete() {
        return coreIsComplete();
    }
    
    public void AxiomContainer.build() {
        try {
            coreBuild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void AxiomContainer.buildNext() {
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
