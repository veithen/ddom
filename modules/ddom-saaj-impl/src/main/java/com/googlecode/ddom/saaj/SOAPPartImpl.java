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

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.frontend.saaj.impl.AbstractSOAPPartImpl;
import com.google.code.ddom.frontend.saaj.intf.SAAJDocument;

public class SOAPPartImpl extends AbstractSOAPPartImpl {
    private static final DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance(SOAPPartImpl.class.getClassLoader());
    
    private final SOAPVersion soapVersion;
    
    public SOAPPartImpl(SOAPVersion soapVersion) {
        this.soapVersion = soapVersion;
    }
    
    public SOAPPartImpl(SOAPVersion soapVersion, InputStream in) {
        super((SAAJDocument)documentHelper.parse("saaj", in));
        this.soapVersion = soapVersion;
    }
    
    @Override
    protected SAAJDocument createInitialDocument() {
        return (SAAJDocument)documentHelper.newDocument("saaj");
    }

    @Override
    protected SAAJDocument createDocumentFromSource(Source source) {
        return (SAAJDocument)documentHelper.parse("saaj", source);
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
