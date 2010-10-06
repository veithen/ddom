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
package com.googlecode.ddom.saaj;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.frontend.saaj.intf.SAAJDocument;
import com.google.code.ddom.frontend.saaj.support.NameImpl;

public class SOAPFactoryImpl extends SOAPFactory {
    private static final DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance(SOAPFactoryImpl.class.getClassLoader());
    
    private SAAJDocument createDocument() {
        return (SAAJDocument)documentHelper.newDocument("saaj");
    }
    
    @Override
    public Detail createDetail() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final SOAPElement createElement(Name name) throws SOAPException {
        // TODO: need to check if conventions for empty/null prefix/uri are respected
        return (SOAPElement)createDocument().coreCreateElement(name.getURI(), name.getLocalName(), name.getPrefix());
    }

    @Override
    public SOAPElement createElement(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final SOAPElement createElement(String localName, String prefix, String uri) throws SOAPException {
        // TODO: add a unit test that checks the type of returned object if the name corresponds to a SOAP envelope, body, etc.
        return (SOAPElement)createDocument().coreCreateElement(uri, localName, prefix);
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
