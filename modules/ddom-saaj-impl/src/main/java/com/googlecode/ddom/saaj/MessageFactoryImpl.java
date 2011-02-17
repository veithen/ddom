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
package com.googlecode.ddom.saaj;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;

public class MessageFactoryImpl extends MessageFactory {
    private final SOAPVersion soapVersion;
    private final NodeFactory nodeFactory;
    
    public MessageFactoryImpl(SOAPVersion soapVersion) throws SOAPException {
        this.soapVersion = soapVersion;
        ModelRegistry modelRegistry = ModelRegistry.getInstance(SOAPFactoryImpl.class.getClassLoader());
        try {
            nodeFactory = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("saaj")).getNodeFactory();
        } catch (ModelLoaderException ex) {
            throw new SOAPException(ex);
        }
    }

    @Override
    public SOAPMessage createMessage() throws SOAPException {
        return new SOAPMessageImpl(new SOAPPartImpl(nodeFactory, soapVersion));
    }
    
    @Override
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws IOException, SOAPException {
        return new SOAPMessageImpl(new SOAPPartImpl(soapVersion, in));
    }
}
