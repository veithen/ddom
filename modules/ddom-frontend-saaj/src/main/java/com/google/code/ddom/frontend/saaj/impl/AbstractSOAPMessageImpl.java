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
package com.google.code.ddom.frontend.saaj.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

public abstract class AbstractSOAPMessageImpl extends SOAPMessage {
    private final Map<String,Object> properties = new HashMap<String,Object>();
    
    @Override
    public final SOAPHeader getSOAPHeader() throws SOAPException {
        return getSOAPPart().getEnvelope().getHeader();
    }

    @Override
    public final SOAPBody getSOAPBody() throws SOAPException {
        return getSOAPPart().getEnvelope().getBody();
    }

    @Override
    public final Object getProperty(String property) throws SOAPException {
        return properties.get(property);
    }

    @Override
    public final void setProperty(String property, Object value) throws SOAPException {
        properties.put(property, value);
    }

    @Override
    public final void writeTo(OutputStream out) throws SOAPException, IOException {
        // TODO: obviously only correct for messages without attachments
        ((AbstractSOAPPartImpl)getSOAPPart()).writeTo(out);
    }
}
