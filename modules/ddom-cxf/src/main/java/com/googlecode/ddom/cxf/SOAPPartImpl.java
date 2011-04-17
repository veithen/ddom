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
package com.googlecode.ddom.cxf;

import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.frontend.saaj.impl.AbstractSOAPPartImpl;
import com.googlecode.ddom.frontend.saaj.intf.SAAJDocument;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPBody;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPEnvelope;
import com.googlecode.ddom.model.Model;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.dom.DOMInput;

public class SOAPPartImpl extends AbstractSOAPPartImpl {
    private final Model saajModel;
    private final SoapMessage message;

    public SOAPPartImpl(Model saajModel, SoapMessage message) {
        this.saajModel = saajModel;
        this.message = message;
    }

    @Override
    protected SAAJDocument createInitialDocument() {
        try {
            // This will give the SOAP message with an empty body
            Document domDocument = (Document)message.getContent(Node.class);
            
            SAAJDocument saajDocument = (SAAJDocument)saajModel.getNodeFactory().createDocument();
            saajDocument.coreSetContent(new SimpleXmlSource(new DOMInput(domDocument)));
            
            // We build the document at this point because we need to access the (empty) body anyway
            // and there is not much content after the body. Building the document allows DDOM to
            // release the builder linked to the DOMInput object.
            // TODO: this also works around an issue in the linkedlist back-end; need to investigate further
            saajDocument.coreBuild();
            
            SAAJSOAPEnvelope envelope = (SAAJSOAPEnvelope)saajDocument.getDocumentElement();
            SAAJSOAPBody body = (SAAJSOAPBody)envelope.getBody();
            
            // TODO: handle the fault case
            
            // This allows us to pass through the raw StAX events from the original reader
            // unless the SOAP body is actually accessed through SAAJ.
            // TODO: need a better name for this thing
            StreamSwitch streamSwitch = new StreamSwitch(message.getContent(XMLStreamReader.class), body);
            
            body.coreSetContent(streamSwitch);
            
            message.setContent(XMLStreamReader.class, streamSwitch);

            // This is a bit ugly. WSS4JInInterceptor#doResults resets the XMLStreamReader content to
            // a W3CDOMStreamReader constructed from the body. This is not what we want, so we add
            // an interceptor to the end of the chain to reset the XMLStreamReader.
            message.getInterceptorChain().add(new ReaderResetInterceptor(Phase.PRE_PROTOCOL, streamSwitch));
            
            return saajDocument;
        } catch (SOAPException ex) {
            throw new RuntimeException(ex);
        } catch (DeferredParsingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected SAAJDocument createDocumentFromSource(Source source) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public SOAPEnvelope getEnvelope() throws SOAPException {
        return (SOAPEnvelope)document.getDocumentElement();
    }
}
