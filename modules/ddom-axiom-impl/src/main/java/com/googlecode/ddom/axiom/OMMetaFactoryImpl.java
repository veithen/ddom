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
package com.googlecode.ddom.axiom;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axiom.soap.SOAPFactory;
import org.xml.sax.InputSource;

import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.google.code.ddom.spi.model.Model;
import com.google.code.ddom.spi.model.ModelLoaderException;
import com.google.code.ddom.spi.model.ModelRegistry;

public final class OMMetaFactoryImpl implements OMMetaFactory {
    private static final ModelDefinition modelDefinition = ModelDefinitionBuilder.buildModelDefinition("axiom-soap");
    
    private final OMMetaFactory metaFactory;
    
    public OMMetaFactoryImpl() throws ModelLoaderException {
        ModelRegistry modelRegistry = ModelRegistry.getInstance(OMMetaFactoryImpl.class.getClassLoader());
        Model model = modelRegistry.getModel(modelDefinition);
        metaFactory = (OMMetaFactory)model.getNodeFactory();
    }

    public OMFactory getOMFactory() {
        return metaFactory.getOMFactory();
    }

    public SOAPFactory getSOAP11Factory() {
        return metaFactory.getSOAP11Factory();
    }

    public SOAPFactory getSOAP12Factory() {
        return metaFactory.getSOAP12Factory();
    }

    public OMXMLParserWrapper createOMBuilder(OMFactory omFactory, StAXParserConfiguration configuration, InputSource is) {
        return metaFactory.createOMBuilder(omFactory, configuration, is);
    }

    public OMXMLParserWrapper createStAXOMBuilder(OMFactory omFactory, XMLStreamReader parser) {
        return metaFactory.createStAXOMBuilder(omFactory, parser);
    }

    public OMXMLParserWrapper createSOAPModelBuilder(StAXParserConfiguration configuration, InputSource is) {
        return metaFactory.createSOAPModelBuilder(configuration, is);
    }

    public OMXMLParserWrapper createStAXSOAPModelBuilder(XMLStreamReader reader) {
        return metaFactory.createStAXSOAPModelBuilder(reader);
    }
}
