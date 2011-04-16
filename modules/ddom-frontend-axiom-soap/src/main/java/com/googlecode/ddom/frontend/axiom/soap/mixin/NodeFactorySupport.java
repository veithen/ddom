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
package com.googlecode.ddom.frontend.axiom.soap.mixin;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPModelBuilder;
import org.xml.sax.InputSource;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomDocument;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPNodeFactory;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPFactoryImpl;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPModelBuilderImpl;
import com.googlecode.ddom.frontend.axiom.soap.support.SOAPVersionEx;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.XmlSource;
import com.googlecode.ddom.stream.parser.ParserSource;
import com.googlecode.ddom.stream.stax.StAXInput;

@Mixin(NodeFactory.class)
public abstract class NodeFactorySupport implements AxiomSOAPNodeFactory {
    private final SOAPFactory soap11Factory = new SOAPFactoryImpl(this, SOAPVersionEx.SOAP11);
    private final SOAPFactory soap12Factory = new SOAPFactoryImpl(this, SOAPVersionEx.SOAP12);
    
    public final SOAPFactory getSOAP11Factory() {
        return soap11Factory;
    }

    public final SOAPFactory getSOAP12Factory() {
        return soap12Factory;
    }

    private SOAPModelBuilder createBuilder(XmlSource source) {
        AxiomDocument document = (AxiomDocument)createDocument();
        document.coreSetContent(source);
        return new SOAPModelBuilderImpl(document);
    }
    
    public final SOAPModelBuilder createSOAPModelBuilder(StAXParserConfiguration configuration, InputSource is) {
        // TODO: we have currently no way to set the SOAPFactory!
        // TODO: translate configuration
        return createBuilder(new ParserSource(is));
    }

    public final SOAPModelBuilder createStAXSOAPModelBuilder(XMLStreamReader reader) {
        // TODO: we have currently no way to set the SOAPFactory!
        return createBuilder(new SimpleXmlSource(new StAXInput(reader, null)));
    }
}
