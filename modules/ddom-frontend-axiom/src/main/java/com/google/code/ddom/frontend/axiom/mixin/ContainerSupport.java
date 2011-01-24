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
package com.google.code.ddom.frontend.axiom.mixin;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.impl.MTOMXMLStreamWriter;

import com.google.code.ddom.Options;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomChildNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomContainer;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.Policies;
import com.google.code.ddom.stream.spi.NamespaceRepairingFilter;
import com.google.code.ddom.stream.stax.StAXPivot;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.util.QNameUtil;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlOutput;

@Mixin({CoreDocument.class, CoreNSAwareElement.class})
public abstract class ContainerSupport implements AxiomContainer {
    public final OMNode getFirstOMChild() {
        try {
            return (OMNode)coreGetFirstChild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public final Iterator getChildren() {
        return coreGetChildrenByType(Axis.CHILDREN, AxiomChildNode.class);
    }
    
    public final Iterator getChildrenWithName(QName qname) {
        return coreGetElementsByName(Axis.CHILDREN, QNameUtil.getNamespaceURI(qname), qname.getLocalPart());
    }
    
    public final Iterator getChildrenWithLocalName(String localName) {
        return coreGetElementsByLocalName(Axis.CHILDREN, localName);
    }
    
    public Iterator getChildrenWithNamespaceURI(String uri) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final OMElement getFirstChildWithName(QName qname) {
        // TODO: we should avoid usage of an iterator here; this would also improve error reporting (because the iterator can only throw unchecked exceptions)
        Iterator<CoreNSAwareElement> it = coreGetElementsByName(Axis.CHILDREN, QNameUtil.getNamespaceURI(qname), qname.getLocalPart());
        return it.hasNext() ? (AxiomElement)it.next() : null;
    }
    
    public void addChild(OMNode omNode) {
        try {
            coreAppendChild((CoreChildNode)omNode, Policies.NODE_MIGRATION_POLICY);
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
    
    public final void internalSerialize(Object out, boolean preserve) throws StreamException {
        XmlInput input = coreGetInput(preserve);
        XmlOutput output = ((AxiomNodeFactory)coreGetNodeFactory()).getStreamFactory().getOutput(out, new Options());
        new Stream(input, output, new NamespaceRepairingFilter()).flush();
    }
    
    public final void serialize(OutputStream output) throws XMLStreamException {
        // TODO: close output?
        try {
            internalSerialize(output, true);
        } catch (StreamException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void serialize(Writer writer) throws XMLStreamException {
        // TODO: close output?
        try {
            internalSerialize(writer, true);
        } catch (StreamException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void serializeAndConsume(OutputStream output) throws XMLStreamException {
        // TODO: close output?
        try {
            internalSerialize(output, false);
        } catch (StreamException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void serializeAndConsume(Writer writer) throws XMLStreamException {
        // TODO: close output?
        try {
            internalSerialize(writer, false);
        } catch (StreamException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
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

    public void serializeAndConsume(OutputStream output, OMOutputFormat format) throws XMLStreamException {
        // TODO
        serialize(output, format);
    }

    public void serializeAndConsume(Writer writer, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final XMLStreamReader getXMLStreamReader() {
        return getXMLStreamReader(true);
    }
    
    public final XMLStreamReader getXMLStreamReaderWithoutCaching() {
        return getXMLStreamReader(false);
    }
    
    public final XMLStreamReader getXMLStreamReader(boolean cache) {
        StAXPivot pivot = new StAXPivot();
        new Stream(coreGetInput(cache), pivot);
        return pivot;
    }
}
