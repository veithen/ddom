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

public class SOAPFactoryImpl extends SOAPFactory {

    @Override
    public Detail createDetail() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public SOAPElement createElement(Name arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public SOAPElement createElement(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public SOAPElement createElement(String arg0, String arg1, String arg2)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
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
    public Name createName(String arg0, String arg1, String arg2)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
