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
package com.google.code.ddom.frontend.axiom.mixin;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.impl.MTOMXMLStreamWriter;

import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomContainer;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

@Mixin({CoreDocument.class, CoreNSAwareElement.class})
public abstract class ContainerSupport implements AxiomContainer {
    public OMNode getFirstOMChild() {
        try {
            return (OMNode)coreGetFirstChild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public Iterator getChildren() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator getChildrenWithName(QName elementQName) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator getChildrenWithLocalName(String localName) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator getChildrenWithNamespaceURI(String uri) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMElement getFirstChildWithName(QName elementQName) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void addChild(OMNode omNode) {
        try {
            coreAppendChild((CoreChildNode)omNode);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public boolean isComplete() {
        return coreIsComplete();
    }
    
    public void build() {
        try {
            coreBuild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void buildNext() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void serialize(OutputStream output) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void serialize(Writer writer) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void serialize(OutputStream output, OMOutputFormat format) throws XMLStreamException {
        MTOMXMLStreamWriter writer = new MTOMXMLStreamWriter(output, format);
        try {
            internalSerialize(writer, true);
        } finally {
            writer.close();
        }
    }
    
    public void serialize(Writer writer, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void serializeAndConsume(OutputStream output) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void serializeAndConsume(Writer writer) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void serializeAndConsume(OutputStream output, OMOutputFormat format) throws XMLStreamException {
        // TODO
        serialize(output, format);
    }

    public void serializeAndConsume(Writer writer, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
