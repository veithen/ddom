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
import org.apache.axiom.om.util.StAXParserConfiguration;
import org.apache.axiom.soap.SOAPFactory;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;

public final class OMMetaFactoryImpl implements OMMetaFactory {
    private static final ModelDefinition modelDefinition = ModelDefinitionBuilder.buildModelDefinition("axiom-soap");
    
    private final OMMetaFactory metaFactory;
    
    public OMMetaFactoryImpl() {
        DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance(OMMetaFactoryImpl.class.getClassLoader());
        metaFactory = documentHelper.getAPIObject(modelDefinition, OMMetaFactory.class);
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

    public OMXMLParserWrapper createOMBuilder(OMFactory omFactory, StAXParserConfiguration configuration, InputStream in) {
        return metaFactory.createOMBuilder(omFactory, configuration, in);
    }

    public OMXMLParserWrapper createOMBuilder(OMFactory omFactory, StAXParserConfiguration configuration, Reader in) {
        return metaFactory.createOMBuilder(omFactory, configuration, in);
    }

    public OMXMLParserWrapper createStAXOMBuilder(OMFactory omFactory, XMLStreamReader parser) {
        return metaFactory.createStAXOMBuilder(omFactory, parser);
    }
}
