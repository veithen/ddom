/*
 * Copyright 2009-2010 Andreas Veithen
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

import java.io.InputStream;
import java.io.Reader;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.soap.SOAPFactory;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.Options;
import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;
import com.google.code.ddom.frontend.axiom.soap.AxiomSOAPFactories;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;

public final class OMMetaFactoryImpl implements OMMetaFactory {
    private static final DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance(OMMetaFactoryImpl.class.getClassLoader());
    private static final ModelDefinition modelDefinition = ModelDefinitionBuilder.buildModelDefinition("axiom-soap");
    
    private final OMFactory omFactory;
    private final SOAPFactory soap11Factory;
    private final SOAPFactory soap12Factory;
    
    public OMMetaFactoryImpl() {
        omFactory = documentHelper.getAPIObject(modelDefinition, OMFactory.class);
        AxiomSOAPFactories soapFactories = documentHelper.getAPIObject(modelDefinition, AxiomSOAPFactories.class);
        soap11Factory = soapFactories.getSOAP11Factory();
        soap12Factory = soapFactories.getSOAP12Factory();
    }
    
    public OMFactory getOMFactory() {
        return omFactory;
    }

    public SOAPFactory getSOAP11Factory() {
        return soap11Factory;
    }

    public SOAPFactory getSOAP12Factory() {
        return soap12Factory;
    }
    
    private OMXMLParserWrapper createBuilder(Object source) {
        return new OMXMLParserWrapperImpl((AxiomDocument)documentHelper.parse(modelDefinition, source, new Options()));
    }
    
    public OMXMLParserWrapper createOMBuilder(InputStream in) {
        return createBuilder(in);
    }

    public OMXMLParserWrapper createOMBuilder(Reader in) {
        return createBuilder(in);
    }

    public OMXMLParserWrapper createStAXOMBuilder(XMLStreamReader parser) {
        return createBuilder(parser);
    }
}
