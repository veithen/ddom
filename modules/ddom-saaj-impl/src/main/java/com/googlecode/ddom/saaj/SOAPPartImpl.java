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

import java.io.InputStream;

import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Source;

import com.google.code.ddom.frontend.saaj.impl.AbstractSOAPPartImpl;
import com.google.code.ddom.frontend.saaj.intf.SAAJDocument;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.stream.Options;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.StreamFactory;

public class SOAPPartImpl extends AbstractSOAPPartImpl {
    private static final StreamFactory streamFactory = StreamFactory.getInstance(SOAPPartImpl.class.getClassLoader());
    
    private final NodeFactory nodeFactory;
    private final SOAPVersion soapVersion;
    
    public SOAPPartImpl(NodeFactory nodeFactory, SOAPVersion soapVersion) {
        this.nodeFactory = nodeFactory;
        this.soapVersion = soapVersion;
    }
    
    public SOAPPartImpl(SOAPVersion soapVersion, InputStream in) {
        throw new UnsupportedOperationException();
//        super((SAAJDocument)documentHelper.parse("saaj", in));
//        this.soapVersion = soapVersion;
    }
    
    @Override
    protected SAAJDocument createInitialDocument() {
        return (SAAJDocument)nodeFactory.createDocument();
    }

    @Override
    protected SAAJDocument createDocumentFromSource(Source source) {
        SAAJDocument document = createInitialDocument();
        try {
            document.coreSetContent(streamFactory.getSource(source, new Options(), false));
        } catch (StreamException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
        return document;
    }

    @Override
    public SOAPEnvelope getEnvelope() throws SOAPException {
        initDocument();
        SOAPEnvelope envelope = (SOAPEnvelope)document.getDocumentElement();
        if (envelope == null) {
            envelope = soapVersion.createEnvelope(document);
            document.appendChild(envelope);
            envelope.addHeader();
            envelope.addBody();
        }
        return envelope;
    }
}
