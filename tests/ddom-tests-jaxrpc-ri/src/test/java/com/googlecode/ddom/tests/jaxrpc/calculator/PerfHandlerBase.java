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

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

public abstract class PerfHandlerBase extends SOAPHandler {
    protected static final String PERF_NAMESPACE_URI = "urn:performance";
    protected static final String PERF_PREFIX = "perf";
    
    protected static final QName PERF_DATA_QNAME = new QName(PERF_NAMESPACE_URI, "PerformanceData", PERF_PREFIX);
    protected static final QName TIMESTAMP_QNAME = new QName(PERF_NAMESPACE_URI, "Timestamp", PERF_PREFIX);
    
    protected void addTimestamp(SOAPHeaderElement perfData) throws SOAPException {
    }
    
    protected void addPerfData(SOAPMessage message, Timestamps timestamps) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPHeader header = envelope.getHeader();
        if (header == null) {
            header = envelope.addHeader();
        }
        SOAPHeaderElement perfData = header.addHeaderElement(PERF_DATA_QNAME);
        for (long timestamp : timestamps.getTimestamps()) {
            perfData.addChildElement(TIMESTAMP_QNAME).setValue(String.valueOf(timestamp));
        }
    }
    
    protected Timestamps extractPerfData(SOAPMessage message) throws SOAPException {
        SOAPHeader header = message.getSOAPHeader();
        if (header == null) {
            return null;
        }
        Iterator it = header.getChildElements(PERF_DATA_QNAME);
        if (!it.hasNext()) {
            return null;
        }
        SOAPHeaderElement element = (SOAPHeaderElement)it.next();
        Timestamps timestamps = new Timestamps();
        for (Iterator it2 = element.getChildElements(TIMESTAMP_QNAME); it2.hasNext(); ) {
            timestamps.addTimestamp(Long.parseLong(((SOAPElement)it2.next()).getValue()));
        }
        return timestamps;
    }
}
