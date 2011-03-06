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
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.frontend.saaj.impl.AttachmentSet;
import com.googlecode.ddom.frontend.saaj.impl.MessageDeserializer;
import com.googlecode.ddom.frontend.saaj.impl.MessageProfile;
import com.googlecode.ddom.frontend.saaj.impl.SOAPProfile;
import com.googlecode.ddom.frontend.saaj.impl.SimpleAttachmentSet;
import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;
import com.googlecode.ddom.model.ModelDefinitionBuilder;
import com.googlecode.ddom.model.ModelRegistry;
import com.googlecode.ddom.model.spi.ModelLoaderException;
import com.googlecode.ddom.spi.ProviderFinder;

public abstract class MessageFactoryImpl extends MessageFactory {
    private final SOAPVersion soapVersion;
    private final NodeFactory nodeFactory;
    private final CompatibilityPolicy compatibilityPolicy;
    
    public MessageFactoryImpl(SOAPVersion soapVersion) throws SOAPException {
        this.soapVersion = soapVersion;
        ModelRegistry modelRegistry = ModelRegistry.getInstance(SOAPFactoryImpl.class.getClassLoader());
        try {
            nodeFactory = modelRegistry.getModel(ModelDefinitionBuilder.buildModelDefinition("saaj")).getNodeFactory();
        } catch (ModelLoaderException ex) {
            throw new SOAPException(ex);
        }
        Map<String,CompatibilityPolicy> policies = ProviderFinder.find(MessageFactoryImpl.class.getClassLoader(), CompatibilityPolicy.class);
        if (policies.isEmpty()) {
            compatibilityPolicy = DefaultCompatibilityPolicy.INSTANCE;
        } else if (policies.size() == 1) {
            compatibilityPolicy = policies.values().iterator().next();
        } else {
            throw new SOAPException("Multiple compatibility policies found in classpath");
        }
    }

    @Override
    public SOAPMessage createMessage() throws SOAPException {
        // TODO: throw exception if soapVersion == null
        return compatibilityPolicy.wrapMessage(new SOAPMessageImpl(new SOAPProfile(soapVersion), new MimeHeaders(), new SOAPPartImpl(nodeFactory, soapVersion), new SimpleAttachmentSet()));
    }
    
    @Override
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws IOException, SOAPException {
        String contentType = null;
        if (headers == null) {
            headers = new MimeHeaders();
        } else {
            String[] values = headers.getHeader("Content-Type");
            if (values != null) {
                contentType = values[0];
            }
        }
        MessageProfile profile;
        if (contentType == null) {
            if (soapVersion == null) {
                throw new SOAPException("No content type specified");
            }
            headers.addHeader("Content-Type", soapVersion.getContentType());
            profile = new SOAPProfile(soapVersion);
        } else {
            profile = MessageProfile.fromContentType(contentType);
        }
        SOAPVersion soapVersionFromContentType = profile.getSOAPVersion();
        MessageDeserializer deserializer = profile.createDeserializer(in);
        MimeHeaders soapPartHeaders = deserializer.getSOAPPartHeaders();
        InputStream soapPartInputStream = deserializer.getSOAPPartInputStream();
        AttachmentSet attachments = deserializer.getAttachments();
        SOAPVersion actualSOAPVersion;
        if (soapVersion == null) {
            actualSOAPVersion = soapVersionFromContentType;
        } else if (soapVersion == soapVersionFromContentType) {
            actualSOAPVersion = soapVersion;
        } else {
            throw new SOAPException("The SOAP version specified by the content type " + soapVersionFromContentType.getContentType() + " is not supported by this factory");
        }
        return compatibilityPolicy.wrapMessage(new SOAPMessageImpl(profile, headers, new SOAPPartImpl(nodeFactory, actualSOAPVersion, soapPartInputStream), attachments));
    }
}
