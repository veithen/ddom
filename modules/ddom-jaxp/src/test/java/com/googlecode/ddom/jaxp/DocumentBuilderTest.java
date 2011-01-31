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
package com.googlecode.ddom.jaxp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.code.ddom.utils.test.InputStreamTestWrapper;
import com.google.code.ddom.utils.test.Validated;
import com.google.code.ddom.utils.test.ValidatedTestResource;
import com.google.code.ddom.utils.test.ValidatedTestRunner;

@RunWith(ValidatedTestRunner.class)
public class DocumentBuilderTest {
    @ValidatedTestResource(reference=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.class, actual=DocumentBuilderFactoryImpl.class)
    private DocumentBuilderFactory factory;
    
    @Validated @Test
    public void testDefaultFactorySettings() {
        Assert.assertFalse(factory.isCoalescing());
        Assert.assertTrue(factory.isExpandEntityReferences());
        Assert.assertFalse(factory.isIgnoringComments());
        Assert.assertFalse(factory.isIgnoringElementContentWhitespace());
        Assert.assertFalse(factory.isNamespaceAware());
        Assert.assertFalse(factory.isValidating());
        // TODO
//        Assert.assertFalse(factory.isXIncludeAware());
    }
    
    @Validated @Test
    public void testParseConsumesInputStream() throws Exception {
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStreamTestWrapper in = new InputStreamTestWrapper(new ByteArrayInputStream("<root>test</root>".getBytes("utf-8")));
        builder.parse(in);
        Assert.assertTrue(in.isEndOfStream());
        // TODO
//        Assert.assertTrue(in.isClosed());
    }
    
    private void testEncoding(String encoding, boolean setEncodingOnInputSource, boolean setXmlEncoding, String expectedInputEncoding, String expectedXmlEncoding) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer out = new OutputStreamWriter(baos, encoding);
        if (setXmlEncoding) {
            out.write("<?xml version='1.0' encoding='");
            out.write(encoding);
            out.write("'?>");
        }
        out.write("<root/>");
        out.close();
        InputSource is = new InputSource(new ByteArrayInputStream(baos.toByteArray()));
        if (setEncodingOnInputSource) {
            is.setEncoding(encoding);
        }
        Document document = factory.newDocumentBuilder().parse(is);
        Assert.assertEquals("inputEncoding", expectedInputEncoding, document.getInputEncoding());
        Assert.assertEquals("xmlEncoding", expectedXmlEncoding, document.getXmlEncoding());
    }

    @Validated @Test
    public void testEncoding() throws Exception {
        testEncoding("ISO-8859-1", true, false, "ISO-8859-1", null);
        testEncoding("ISO-8859-1", false, true, "ISO-8859-1", "ISO-8859-1");
        testEncoding("ISO-8859-1", true, true, "ISO-8859-1", "ISO-8859-1");
    }
    
    /**
     * Check that CDATA sections are not segmented, even if coalescing is disabled.
     * 
     * @throws Exception
     */
    @Validated @Test
    public void testNoCDATASegmentation() throws Exception {
        StringBuilder buffer = new StringBuilder("<root><![CDATA[");
        for (int i=0; i<100000; i++) {
            buffer.append('a');
        }
        buffer.append("]]></root>");
        factory.setCoalescing(false);
        Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(buffer.toString())));
        Assert.assertEquals(1, document.getDocumentElement().getChildNodes().getLength());
    }
    
    @Validated @Test
    public void testIgnoringComments() throws Exception {
        factory.setIgnoringComments(true);
        Document document = factory.newDocumentBuilder().parse(new InputSource(new StringReader("<root><!--comment--></root>")));
        Assert.assertNull(document.getDocumentElement().getFirstChild());
    }
}
