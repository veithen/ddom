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
package com.googlecode.ddom.frontend.axiom.mixin;

import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.xml.sax.InputSource;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomDocument;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.googlecode.ddom.frontend.axiom.support.OMFactoryImpl;
import com.googlecode.ddom.frontend.axiom.support.OMXMLParserWrapperImpl;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.dom.DOMSource;
import com.googlecode.ddom.stream.parser.ParserSource;
import com.googlecode.ddom.stream.sax.SAXInput;
import com.googlecode.ddom.stream.stax.StAXPullInput;

@Mixin(NodeFactory.class)
public abstract class NodeFactorySupport implements AxiomNodeFactory {
    private final OMFactory omFactory;
    
    public NodeFactorySupport() {
        omFactory = new OMFactoryImpl(this);
    }
    
    public final OMFactory getOMFactory() {
        return omFactory;
    }

    private OMXMLParserWrapper createBuilder(XmlSource source) {
        AxiomDocument document = (AxiomDocument)createDocument();
        document.coreSetContent(source);
        return new OMXMLParserWrapperImpl(document);
    }
    
    public final OMXMLParserWrapper createOMBuilder(OMFactory omFactory, StAXParserConfiguration configuration, InputSource is) {
        // TODO: we have currently no way to set the OMFactory!
        // TODO: translate configuration
        return createBuilder(new ParserSource(is));
    }

    public final OMXMLParserWrapper createStAXOMBuilder(OMFactory omFactory, XMLStreamReader parser) {
        // TODO: we have currently no way to set the OMFactory!
        return createBuilder(new SimpleXmlSource(new StAXPullInput(parser)));
    }

    public OMXMLParserWrapper createOMBuilder(OMFactory omFactory, Source source) {
        // TODO: we have currently no way to set the OMFactory!
        if (source instanceof SAXSource) {
            return createBuilder(new SimpleXmlSource(new SAXInput((SAXSource)source)));
        } else if (source instanceof javax.xml.transform.dom.DOMSource) {
            return createBuilder(new DOMSource(((javax.xml.transform.dom.DOMSource)source).getNode()));
        } else {
            // TODO: should support StreamSource as well
            throw new UnsupportedOperationException();
        }
    }
}
