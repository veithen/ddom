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
package com.google.code.ddom.stream.dom;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import com.google.code.ddom.Options;
import com.googlecode.ddom.stream.StreamFactory;
import com.googlecode.ddom.stream.XmlSource;

public class DOMStreamProviderTest {
    private StreamFactory streamFactory;
    
    @Before
    public void setUp() {
        streamFactory = StreamFactory.getInstance(DOMStreamProviderTest.class.getClassLoader());
    }
    
    @Test
    public void testDocument() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        XmlSource source = streamFactory.getSource(document, new Options(), true);
        Assert.assertNotNull(source);
    }
    
    // TODO: we should test other types
}
