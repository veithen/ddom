/*
 * Copyright 2009-2011,2013 Andreas Veithen
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

import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axiom.util.stax.XMLEventUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomDocument;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.googlecode.ddom.frontend.axiom.support.AxiomParserSource;
import com.googlecode.ddom.frontend.axiom.support.OMFactoryImpl;
import com.googlecode.ddom.frontend.axiom.support.OMXMLParserWrapperImpl;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.dom.DOMSource;
import com.googlecode.ddom.stream.filter.NamespaceRepairingFilter;
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
        return createBuilder(new AxiomParserSource(is, configuration));
    }

    public final OMXMLParserWrapper createStAXOMBuilder(OMFactory omFactory, XMLStreamReader parser) {
        // TODO: need to do the same check for SOAP!
        int eventType = parser.getEventType();
        if (eventType != XMLStreamReader.START_DOCUMENT && eventType != XMLStreamReader.START_ELEMENT) {
            throw new OMException("The supplied XMLStreamReader is in an unexpected state ("
                    + XMLEventUtils.getEventTypeString(eventType) + ")");
        }
        // TODO: we have currently no way to set the OMFactory!
        XmlInput input = new StAXPullInput(parser);
        input.addFilter(new NamespaceRepairingFilter());
        return createBuilder(new SimpleXmlSource(input));
    }

    public final OMXMLParserWrapper createOMBuilder(OMFactory omFactory, Source source) {
        if (source instanceof SAXSource) {
            // TODO: document the expandEntityReferences default value in the Axiom Javadoc
            return createOMBuilder(omFactory, (SAXSource)source, true);
        } else if (source instanceof javax.xml.transform.dom.DOMSource) {
            // TODO: document the expandEntityReferences default value in the Axiom Javadoc
            return createOMBuilder(omFactory, ((javax.xml.transform.dom.DOMSource)source).getNode(), true);
        } else {
            // TODO: should support StreamSource as well
            throw new UnsupportedOperationException();
        }
    }

    public final OMXMLParserWrapper createOMBuilder(OMFactory omFactory, Node node, boolean expandEntityReferences) {
        // TODO: we have currently no way to set the OMFactory!
        return createBuilder(new DOMSource(node, expandEntityReferences));
    }

    public final OMXMLParserWrapper createOMBuilder(OMFactory omFactory, SAXSource source, boolean expandEntityReferences) {
        // TODO: we have currently no way to set the OMFactory!
        return createBuilder(new SimpleXmlSource(new SAXInput((SAXSource)source)));
    }
}
