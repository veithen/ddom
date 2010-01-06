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
package com.google.code.ddom.frontend.axiom;

import java.io.StringReader;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.llom.factory.OMLinkedListImplFactory;
import org.apache.axiom.om.util.StAXUtils;
import org.junit.Assert;

public class LLOMAxiomUtil implements AxiomUtil {
    public final static LLOMAxiomUtil INSTANCE = new LLOMAxiomUtil();
    
    private final OMFactory factory = new OMLinkedListImplFactory();

    private LLOMAxiomUtil() {}
    
    public OMDocument createDocument() {
        return factory.createOMDocument();
    }

    public OMDocument parse(String xml) {
        try {
            return new StAXOMBuilder(StAXUtils.createXMLStreamReader(new StringReader(xml))).getDocument();
        } catch (XMLStreamException ex) {
            Assert.fail(ex.getMessage());
            return null; // Make compiler happy
        }
    }
}
