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

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.junit.Test;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.serializer.Serializer;

public class StAXPullInputTest {
    @Test
    public void testFragment() throws Exception {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(
                new StringReader("<a><b><c/><d/></b></a>"));
        while (reader.getEventType() != XMLStreamReader.START_ELEMENT || !reader.getLocalName().equals("b")) {
            reader.next();
        }
        StringWriter out = new StringWriter();
        new Stream(new StAXPullInput(reader), new Serializer(out)).flush();
        assertXMLEqual("<b><c/><d/></b>", out.toString());
        assertEquals(XMLStreamReader.END_ELEMENT, reader.getEventType());
        assertEquals("a", reader.getLocalName());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testInvalidState() throws Exception {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(
                new StringReader("<a>text</a>"));
        while (reader.getEventType() != XMLStreamReader.CHARACTERS) {
            reader.next();
        }
        new StAXPullInput(reader);
    }
}
