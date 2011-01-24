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

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;

import com.google.code.ddom.frontend.saaj.support.NameImpl;
import com.google.code.ddom.model.ModelDefinition;
import com.google.code.ddom.model.ModelDefinitionBuilder;
import com.google.code.ddom.spi.model.ModelLoaderException;
import com.google.code.ddom.spi.model.ModelRegistry;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.core.util.QNameUtil;

public class SOAPFactoryImpl extends SOAPFactory {
    private static final ModelRegistry modelRegistry = ModelRegistry.getInstance(SOAPFactoryImpl.class.getClassLoader());
    private static final ModelDefinition modelDefinition = ModelDefinitionBuilder.buildModelDefinition("saaj");
    
    private final NodeFactory nodeFactory;
    
    public SOAPFactoryImpl() throws ModelLoaderException {
        nodeFactory = modelRegistry.getModel(modelDefinition).getNodeFactory();
    }
    
    @Override
    public Detail createDetail() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final SOAPElement createElement(Name name) throws SOAPException {
        // TODO: need to check if conventions for empty/null prefix/uri are respected
        return (SOAPElement)nodeFactory.createElement(null, name.getURI(), name.getLocalName(), name.getPrefix());
    }

    @Override
    public SOAPElement createElement(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final SOAPElement createElement(String localName, String prefix, String uri) throws SOAPException {
        // TODO: add a unit test that checks the type of returned object if the name corresponds to a SOAP envelope, body, etc.
        return (SOAPElement)nodeFactory.createElement(null, uri, localName, prefix);
    }

    @Override
    public final SOAPElement createElement(QName qname) throws SOAPException {
        return (SOAPElement)nodeFactory.createElement(null, QNameUtil.getNamespaceURI(qname), qname.getLocalPart(), QNameUtil.getPrefix(qname));
    }

    @Override
    public SOAPFault createFault() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public SOAPFault createFault(String arg0, QName arg1) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Name createName(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final Name createName(String localName, String prefix, String uri) throws SOAPException {
        return new NameImpl(localName, prefix, uri);
    }
}
