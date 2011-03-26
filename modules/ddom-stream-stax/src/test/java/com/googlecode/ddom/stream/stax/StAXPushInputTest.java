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
package com.googlecode.ddom.stream.stax;

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.dom.DOMResult;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.dom.DOMOutput;

public class StAXPushInputTest {
    public interface Source {
        void serialize(XMLStreamWriter writer) throws XMLStreamException;
    }
    
    private static DocumentBuilder documentBuilder;
    private static XMLOutputFactory outputFactory;
    
    @BeforeClass
    public static void init() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        documentBuilder = factory.newDocumentBuilder();
        outputFactory = XMLOutputFactory.newInstance();
    }
    
    private void test(final Source source) throws Exception {
        StAXPushInput input = new StAXPushInput() {
            @Override
            protected void serialize(XMLStreamWriter out) throws XMLStreamException {
                source.serialize(out);
            }
            
            @Override
            public void dispose() {
            }
        };
        Document actual = documentBuilder.newDocument();
        new Stream(input, new DOMOutput(actual)).flush();
        
        Document expected = documentBuilder.newDocument();
        source.serialize(outputFactory.createXMLStreamWriter(new DOMResult(expected)));
        XMLAssert.assertXMLIdentical(new Diff(expected, actual), true);
    }
    
    @Test
    public void test1() throws Exception {
        test(new Source() {
            public void serialize(XMLStreamWriter writer) throws XMLStreamException {
                writer.writeStartElement("", "root", "");
                writer.writeAttribute("p", "urn:ns1", "attr", "value");
                writer.writeNamespace("p", "urn:ns1");
                writer.writeCharacters("test");
                writer.writeEndElement();
            }
        });
    }
    
    @Test
    public void test2() throws Exception {
        test(new Source() {
            public void serialize(XMLStreamWriter writer) throws XMLStreamException {
                writer.writeStartElement("ns2", "root", "urn:ns2");
                writer.setPrefix("ns2", "urn:ns2");
                writer.writeNamespace("ns2", "urn:ns2");
                NamespaceContext namespaceContext = writer.getNamespaceContext();
                assertEquals("ns2", namespaceContext.getPrefix("urn:ns2"));
                assertEquals("urn:ns2", namespaceContext.getNamespaceURI("ns2"));
                writer.writeStartElement("urn:ns2", "child");
                writer.writeAttribute("attr", "value");
                writer.writeEndElement();
                writer.writeEndElement();
            }
        });
    }
}
