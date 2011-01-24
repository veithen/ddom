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
package com.google.code.ddom.frontend.axiom.mixin;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.xml.sax.InputSource;

import com.google.code.ddom.Options;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.google.code.ddom.frontend.axiom.support.OMFactoryImpl;
import com.google.code.ddom.frontend.axiom.support.OMXMLParserWrapperImpl;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.StreamFactory;
import com.googlecode.ddom.core.NodeFactory;

@Mixin(NodeFactory.class)
public abstract class NodeFactorySupport implements AxiomNodeFactory {
    private final OMFactory omFactory;
    private final StreamFactory streamFactory;
    
    public NodeFactorySupport() {
        omFactory = new OMFactoryImpl(this);
        streamFactory = StreamFactory.getInstance(getClass().getClassLoader());
    }
    
    public final StreamFactory getStreamFactory() {
        return streamFactory;
    }

    public final OMFactory getOMFactory() {
        return omFactory;
    }

    private OMXMLParserWrapper createBuilder(Object source) {
        try {
            AxiomDocument document = (AxiomDocument)createDocument();
            document.coreSetContent(streamFactory.getSource(source, new Options(), false));
            return new OMXMLParserWrapperImpl(document);
        } catch (StreamException ex) {
            throw new OMException(ex);
        }
    }
    
    public final OMXMLParserWrapper createOMBuilder(OMFactory omFactory, StAXParserConfiguration configuration, InputSource is) {
        // TODO: we have currently no way to set the OMFactory!
        // TODO: translate configuration
        return createBuilder(is);
    }

    public final OMXMLParserWrapper createStAXOMBuilder(OMFactory omFactory, XMLStreamReader parser) {
        // TODO: we have currently no way to set the OMFactory!
        return createBuilder(parser);
    }
}
