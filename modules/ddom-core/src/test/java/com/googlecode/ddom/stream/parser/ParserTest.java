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
package com.googlecode.ddom.stream.parser;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.dom.DOMOutput;

public class ParserTest {
    private static DocumentBuilder documentBuilder;
    
    @BeforeClass
    public static void setUp() throws ParserConfigurationException {
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
    
    private static Element toDOM(String xml) throws StreamException {
        Document document = documentBuilder.newDocument();
        new Stream(new Parser(new StringReader(xml), true), new DOMOutput(document)).flush();
        return document.getDocumentElement();
    }
    
    @Test
    public void testEOLNormalizationInElementContent() throws StreamException {
        assertEquals("a\nb", toDOM("<root>a\r\nb</root>").getTextContent());
    }
    
    @Test
    public void testEOLNormalizationInProcessingInstruction() throws StreamException {
        assertEquals("a\nb", toDOM("<root><?pi a\r\nb?></root>").getFirstChild().getTextContent());
    }
    
    @Test
    public void testEOLNormalizationInComment() throws StreamException {
        assertEquals("a\nb", toDOM("<root><!--a\r\nb--></root>").getFirstChild().getTextContent());
    }

    @Test
    public void testEOLNormalizationInCDATASection() throws StreamException {
        assertEquals("a\nb", toDOM("<root><![CDATA[a\r\nb]]></root>").getTextContent());
    }
}
