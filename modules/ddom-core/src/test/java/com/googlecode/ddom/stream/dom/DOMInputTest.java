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
package com.googlecode.ddom.stream.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.xml.parsers.DocumentBuilder;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.googlecode.ddom.stream.Stream;

public class DOMInputTest {
    @Test
    public void testSubtree() throws Exception {
        DocumentBuilder documentBuilder = new DocumentBuilderFactoryImpl().newDocumentBuilder();
        Document doc = documentBuilder.newDocument();
        Element a = doc.createElementNS(null, "a");
        Element b = doc.createElementNS(null, "b");
        b.appendChild(doc.createComment("test"));
        a.appendChild(b);
        Element c = doc.createElementNS(null, "c");
        a.appendChild(c);
        Document doc2 = documentBuilder.newDocument();
        new Stream(new DOMInput(b, false), new DOMOutput(doc2)).flush();
        Element b2 = doc2.getDocumentElement();
        assertEquals("b", b2.getLocalName());
        assertNull(b2.getNextSibling());
        Comment comment = (Comment)b.getFirstChild();
        assertEquals("test", comment.getData());
    }
}
