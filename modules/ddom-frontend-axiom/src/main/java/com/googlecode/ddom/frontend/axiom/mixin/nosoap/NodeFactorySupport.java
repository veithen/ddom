/*
 * Copyright 2009-2010,2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.mixin.nosoap;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPModelBuilder;
import org.apache.axiom.util.stax.xop.MimePartProvider;
import org.xml.sax.InputSource;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNodeFactory;

@Mixin(NodeFactory.class)
public abstract class NodeFactorySupport implements AxiomNodeFactory {
    public final SOAPFactory getSOAP11Factory() {
        throw new UnsupportedOperationException();
    }

    public final SOAPFactory getSOAP12Factory() {
        throw new UnsupportedOperationException();
    }

    public final SOAPModelBuilder createStAXSOAPModelBuilder(XMLStreamReader parser) {
        throw new UnsupportedOperationException();
    }

    public final SOAPModelBuilder createSOAPModelBuilder(StAXParserConfiguration configuration, InputSource is) {
        throw new UnsupportedOperationException();
    }

    public final SOAPModelBuilder createSOAPModelBuilder(StAXParserConfiguration configuration,
            SOAPFactory soapFactory, InputSource rootPart, MimePartProvider mimePartProvider) {
        throw new UnsupportedOperationException();
    }
}
