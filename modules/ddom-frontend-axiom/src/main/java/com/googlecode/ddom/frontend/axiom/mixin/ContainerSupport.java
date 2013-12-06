/*
 * Copyright 2009-2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.mixin;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXSource;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.OMSerializable;
import org.apache.axiom.om.OMXMLStreamReaderConfiguration;

import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreModelStreamException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.ElementMatcher;
import com.googlecode.ddom.core.Selector;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomContainer;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionTranslator;
import com.googlecode.ddom.frontend.axiom.support.Policies;
import com.googlecode.ddom.frontend.axiom.support.XmlSourceImpl;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlOutput;
import com.googlecode.ddom.stream.filter.NamespaceContextFilter;
import com.googlecode.ddom.stream.filter.NamespaceRepairingFilter;
import com.googlecode.ddom.stream.filter.NamespaceURIInterningFilter;
import com.googlecode.ddom.stream.sax.SAXSourceAdapter;
import com.googlecode.ddom.stream.serializer.Serializer;
import com.googlecode.ddom.stream.stax.StAXOutput;
import com.googlecode.ddom.stream.stax.StAXPivot;

@Mixin({CoreDocument.class, CoreNSAwareElement.class})
public abstract class ContainerSupport implements AxiomContainer {
    public final OMNode getFirstOMChild() {
        try {
            return (OMNode)coreGetFirstChild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public final Iterator<OMNode> getChildren() {
        return coreGetNodes(Axis.CHILDREN, Selector.ANY, OMNode.class, AxiomExceptionTranslator.INSTANCE);
    }
    
    public final Iterator getChildrenWithName(QName qname) {
        return coreGetElements(Axis.CHILDREN, CoreNSAwareElement.class, ElementMatcher.BY_QNAME, qname.getNamespaceURI(), qname.getLocalPart(), AxiomExceptionTranslator.INSTANCE);
    }
    
    public final Iterator getChildrenWithLocalName(String localName) {
        return coreGetElements(Axis.CHILDREN, CoreNSAwareElement.class, ElementMatcher.BY_LOCAL_NAME, null, localName, AxiomExceptionTranslator.INSTANCE);
    }
    
    public final Iterator getChildrenWithNamespaceURI(String uri) {
        return coreGetElements(Axis.CHILDREN, CoreNSAwareElement.class, ElementMatcher.BY_NAMESPACE_URI, uri, null, AxiomExceptionTranslator.INSTANCE);
    }
    
    public final Iterator<OMSerializable> getDescendants(boolean includeSelf) {
        return coreGetNodes(includeSelf ? Axis.DESCENDANTS_OR_SELF : Axis.DESCENDANTS, Selector.ANY, OMSerializable.class, AxiomExceptionTranslator.INSTANCE);
    }
    
    public final OMElement getFirstChildWithName(QName qname) {
        // TODO: we should avoid usage of an iterator here; this would also improve error reporting (because the iterator can only throw unchecked exceptions)
        Iterator<CoreNSAwareElement> it = coreGetElements(Axis.CHILDREN, CoreNSAwareElement.class, ElementMatcher.BY_QNAME, qname.getNamespaceURI(), qname.getLocalPart(), AxiomExceptionTranslator.INSTANCE);
        return it.hasNext() ? (AxiomElement)it.next() : null;
    }
    
    public void addChild(OMNode omNode) {
        try {
            coreAppendChild((CoreChildNode)omNode, Policies.NODE_MIGRATION_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public boolean isComplete() {
        return coreIsComplete();
    }
    
    public void build() {
        try {
            coreBuild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public void buildNext() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    private final void serialize(XmlOutput output, boolean preserve) throws XMLStreamException {
        XmlInput input = coreGetInput(preserve);
        input.addFilter(new NamespaceRepairingFilter());
        try {
            new Stream(input, output).flush();
        } catch (CoreModelStreamException ex) {
            throw AxiomExceptionTranslator.translate(ex.getCoreModelException());
        } catch (StreamException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public void serialize(XMLStreamWriter writer, boolean cache) throws XMLStreamException {
        serialize(new StAXOutput(writer), cache);
    }

    public String toString(boolean preserve) throws XMLStreamException {
        StringWriter sw = new StringWriter();
        serialize(new Serializer(sw), preserve);
        return sw.toString();
    }
    
    private void serialize(OutputStream output, boolean preserve) throws XMLStreamException {
        // TODO: close output?
        try {
            // TODO: correct choice of encoding?
            serialize(new Serializer(output, "UTF-8"), preserve);
        } catch (UnsupportedEncodingException ex) {
            throw new XMLStreamException(ex);
        }
    }

    private void serialize(Writer writer, boolean preserve) throws XMLStreamException {
        // TODO: close output?
        serialize(new Serializer(writer), preserve);
    }
    
    private void serialize(OutputStream output, OMOutputFormat format, boolean preserve) throws XMLStreamException {
        // TODO: close output?
        // TODO: MTOM
        // TODO: XML declaration
        try {
            String encoding = format.getCharSetEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
            serialize(new Serializer(output, encoding), preserve);
        } catch (UnsupportedEncodingException ex) {
            throw new XMLStreamException(ex);
        }
    }

    public final void serialize(OutputStream output) throws XMLStreamException {
        serialize(output, true);
    }
    
    public final void serializeAndConsume(OutputStream output) throws XMLStreamException {
        serialize(output, false);
    }

    public final void serialize(Writer writer) throws XMLStreamException {
        serialize(writer, true);
    }
    
    public final void serializeAndConsume(Writer writer) throws XMLStreamException {
        serialize(writer, false);
    }

    public final void serialize(OutputStream output, OMOutputFormat format) throws XMLStreamException {
        serialize(output, format, true);
    }
    
    public final void serializeAndConsume(OutputStream output, OMOutputFormat format) throws XMLStreamException {
        serialize(output, format, false);
    }

    public void serialize(Writer writer, OMOutputFormat format) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
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

    public final XMLStreamReader getXMLStreamReader(boolean cache, OMXMLStreamReaderConfiguration configuration) {
        StAXPivot pivot = new StAXPivot();
        XmlInput input = coreGetInput(cache);
        if (configuration.isPreserveNamespaceContext() && this instanceof CoreChildNode) {
            CoreParentNode parent = ((CoreChildNode)this).coreGetParent();
            if (parent instanceof AxiomElement) {
                input.addFilter(new NamespaceContextFilter(((AxiomElement)parent).getNamespaceContextMap()));
            }
        }
        if (configuration.isNamespaceURIInterning()) {
            input.addFilter(new NamespaceURIInterningFilter());
        }
        new Stream(input, pivot);
        return pivot;
    }

    public final SAXSource getSAXSource(boolean cache) {
        return new SAXSourceAdapter(new XmlSourceImpl(this, cache));
    }
}
