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

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPException;

public class ServerPerfHandler extends PerfHandlerBase {
    private static final String TIMESTAMPS_PROPERTY = ServerPerfHandler.class.getName() + ".Timestamps";
    
    @Override
    public QName[] getHeaders() {
        return null;
    }

    @Override
    public boolean handleRequest(SOAPMessageContext context) throws SOAPException {
        Timestamps timestamps = extractPerfData(context.getMessage());
        if (timestamps != null) {
            timestamps.addTimestamp();
            context.setProperty(TIMESTAMPS_PROPERTY, timestamps);
        }
        return true;
    }

    @Override
    public boolean handleResponse(SOAPMessageContext context) throws SOAPException {
        Timestamps timestamps = (Timestamps)context.getProperty(TIMESTAMPS_PROPERTY);
        if (timestamps != null) {
            timestamps.addTimestamp();
            addPerfData(context.getMessage(), timestamps);
        }
        return true;
    }
}
