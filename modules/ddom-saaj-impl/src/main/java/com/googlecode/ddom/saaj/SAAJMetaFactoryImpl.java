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

import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SAAJMetaFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import com.googlecode.ddom.frontend.saaj.support.SOAPVersion;
import com.googlecode.ddom.model.spi.ModelLoaderException;
import com.googlecode.ddom.saaj.compat.CompatibilityPolicy;
import com.googlecode.ddom.saaj.compat.DefaultCompatibilityPolicy;
import com.googlecode.ddom.spi.Finder;

public class SAAJMetaFactoryImpl extends SAAJMetaFactory {
    private final CompatibilityPolicy compatibilityPolicy;
    
    public SAAJMetaFactoryImpl() throws SOAPException {
        Map<String,CompatibilityPolicy> policies = Finder.findNamedProviders(MessageFactoryImpl.class.getClassLoader(), CompatibilityPolicy.class);
        if (policies.isEmpty()) {
            compatibilityPolicy = DefaultCompatibilityPolicy.INSTANCE;
        } else if (policies.size() == 1) {
            compatibilityPolicy = policies.values().iterator().next();
        } else {
            throw new SOAPException("Multiple compatibility policies found in classpath");
        }
    }
    
    @Override
    protected MessageFactory newMessageFactory(String protocol) throws SOAPException {
        SOAPVersion version;
        if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
            version = SOAPVersion.SOAP11;
        } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
            version = SOAPVersion.SOAP12;
        } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
            version = null; 
        } else {
            throw new SOAPException("Unknown Protocol: " + protocol + " specified for creating MessageFactory");
        }
        return compatibilityPolicy.wrapMessageFactory(new MessageFactoryImpl(version, compatibilityPolicy));
    }

    @Override
    protected SOAPFactory newSOAPFactory(String protocol) throws SOAPException {
        try {
            // TODO: distinguish SOAP versions
            if (SOAPConstants.SOAP_1_1_PROTOCOL.equals(protocol)) {
                return new SOAPFactoryImpl();
            } else if (SOAPConstants.SOAP_1_2_PROTOCOL.equals(protocol)) {
                return new SOAPFactoryImpl();
            } else if (SOAPConstants.DYNAMIC_SOAP_PROTOCOL.equals(protocol)) {
                return new SOAPFactoryImpl();
            } else {
                throw new SOAPException("Unknown Protocol: " + protocol + " specified for creating SOAPFactory");
            }
        } catch (ModelLoaderException ex) {
            throw new SOAPException(ex);
        }
    }
}
