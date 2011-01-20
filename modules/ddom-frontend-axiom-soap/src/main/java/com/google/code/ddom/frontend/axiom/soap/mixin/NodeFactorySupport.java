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
package com.google.code.ddom.frontend.axiom.soap.mixin;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axiom.soap.SOAPFactory;
import org.xml.sax.InputSource;

import com.google.code.ddom.core.NodeFactory;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPNodeFactory;
import com.google.code.ddom.frontend.axiom.soap.support.SOAPFactoryImpl;
import com.google.code.ddom.frontend.axiom.soap.support.SOAPVersionEx;

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

    public final OMXMLParserWrapper createSOAPModelBuilder(StAXParserConfiguration configuration, InputSource is) {
        // TODO: this only works because createOMBuilder may actually create a SOAP model
        return createOMBuilder(null, configuration, is);
    }

    public final OMXMLParserWrapper createStAXSOAPModelBuilder(XMLStreamReader reader) {
        // TODO: this only works because createStAXOMBuilder may actually create a SOAP model
        return createStAXOMBuilder(null, reader);
    }
}
