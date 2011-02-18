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
package com.googlecode.ddom.tests.jaxrpc.calculator;

import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;

public abstract class SOAPHandler extends GenericHandler {
    @Override
    public final boolean handleRequest(MessageContext context) {
        try {
            return handleRequest((SOAPMessageContext)context);
        } catch (SOAPException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
    }

    @Override
    public final boolean handleResponse(MessageContext context) {
        return handleResponse((SOAPMessageContext)context);
    }

    @Override
    public final boolean handleFault(MessageContext context) {
        return handleFault((SOAPMessageContext)context);
    }

    public boolean handleRequest(SOAPMessageContext context) throws SOAPException {
        return true;
    }

    public boolean handleResponse(SOAPMessageContext context) {
        return true;
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }
}
