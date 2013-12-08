/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.soap.support;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPConstants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPFault;
import org.apache.axiom.soap.SOAPFaultClassifier;
import org.apache.axiom.soap.SOAPFaultCode;
import org.apache.axiom.soap.SOAPFaultDetail;
import org.apache.axiom.soap.SOAPFaultNode;
import org.apache.axiom.soap.SOAPFaultReason;
import org.apache.axiom.soap.SOAPFaultRole;
import org.apache.axiom.soap.SOAPFaultSubCode;
import org.apache.axiom.soap.SOAPFaultText;
import org.apache.axiom.soap.SOAPFaultValue;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPMessage;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axiom.soap.SOAPVersion;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPEnvelope;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPHeaderBlock;
import com.googlecode.ddom.frontend.axiom.soap.intf.AxiomSOAPMessage;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionTranslator;
import com.googlecode.ddom.frontend.axiom.support.NSUtil;
import com.googlecode.ddom.frontend.axiom.support.OMFactoryImpl;

public class SOAPFactoryImpl extends OMFactoryImpl implements SOAPFactory {
    private final SOAPVersionEx soapVersion;
    private final OMNamespace namespace;

    public SOAPFactoryImpl(AxiomNodeFactory nodeFactory, SOAPVersionEx soapVersionEx) {
        super(nodeFactory);
        this.soapVersion = soapVersionEx;
        this.namespace = createOMNamespace(soapVersionEx.getEnvelopeURI(), SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
    }

    public final SOAPVersion getSOAPVersion() {
        return soapVersion.getSOAPVersion();
    }
    
    public final String getSoapVersionURI() {
        return soapVersion.getEnvelopeURI();
    }

    public final OMNamespace getNamespace() {
        return namespace;
    }

    public final SOAPEnvelope getDefaultEnvelope() throws SOAPProcessingException {
        SOAPEnvelope env = createSOAPEnvelope();
        createSOAPHeader(env);
        createSOAPBody(env);
        return env;
    }

    public final SOAPEnvelope getDefaultFaultEnvelope() throws SOAPProcessingException {
        SOAPEnvelope defaultEnvelope = getDefaultEnvelope();
        SOAPFault fault = createSOAPFault(defaultEnvelope.getBody());
        SOAPFaultCode code = createSOAPFaultCode(fault);
        if (soapVersion.getFaultClassifierSequence() != null) {
            createSOAPFaultValue(code);
        }
        SOAPFaultReason reason = createSOAPFaultReason(fault);
        if (soapVersion.getSOAPFaultTextClass() != null) {
            createSOAPFaultText(reason);
        }
        createSOAPFaultDetail(fault);
        return defaultEnvelope;
    }

    public final SOAPMessage createSOAPMessage() {
        return nodeFactory.createDocument(AxiomSOAPMessage.class);
    }

    public final SOAPMessage createSOAPMessage(OMXMLParserWrapper builder) {
        // This method is deprecated and we don't support it
        throw new UnsupportedOperationException();
    }
    
    private <T extends AxiomElement> T createSOAPElement(OMElement parent, Class<T> extensionInterface, String namespaceURI, String localName) {
        try {
            String prefix = namespaceURI.length() == 0 ? "" : SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX;
            if (parent == null) {
                T element = nodeFactory.createElement(null, extensionInterface, namespaceURI, localName, prefix);
                element.setOMFactory(this);
                if (prefix.length() != 0) {
                    element.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
                }
                return element;
            } else {
                T element = ((AxiomElement)parent).coreAppendElement(extensionInterface, namespaceURI, localName, prefix);
                element.setOMFactory(this);
                return element;
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    private <T extends AxiomElement> T createSOAPElement(OMElement parent, Class<T> extensionInterface, String localName) {
        return createSOAPElement(parent, extensionInterface, soapVersion.getEnvelopeURI(), localName);
    }
    
    private <T extends AxiomElement> T createSOAPElement(OMElement parent, Class<T> extensionInterface, QName qname) {
        return createSOAPElement(parent, extensionInterface, qname.getNamespaceURI(), qname.getLocalPart());
    }
    
    public final SOAPEnvelope createSOAPEnvelope() throws SOAPProcessingException {
        return createSOAPElement(null, soapVersion.getSOAPEnvelopeClass(), SOAPConstants.SOAPENVELOPE_LOCAL_NAME);
    }

    public final SOAPEnvelope createSOAPEnvelope(OMNamespace ns) {
        try {
            String prefix = ns.getPrefix();
            if (prefix == null) {
                prefix = SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX;
            }
            AxiomSOAPEnvelope envelope = nodeFactory.createElement(null, soapVersion.getSOAPEnvelopeClass(), soapVersion.getEnvelopeURI(), SOAPConstants.SOAPENVELOPE_LOCAL_NAME, prefix);
            envelope.setOMFactory(this);
            envelope.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, soapVersion.getEnvelopeURI());
            return envelope;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final SOAPHeader createSOAPHeader() throws SOAPProcessingException {
        return createSOAPHeader(null);
    }

    public final SOAPHeader createSOAPHeader(SOAPEnvelope envelope) throws SOAPProcessingException {
        return createSOAPElement(envelope, soapVersion.getSOAPHeaderClass(), SOAPConstants.HEADER_LOCAL_NAME);
    }

    public final SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns) throws SOAPProcessingException {
        AxiomSOAPHeaderBlock element = nodeFactory.createElement(null, soapVersion.getSOAPHeaderBlockClass(), NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns));
        element.setOMFactory(this);
        return element;
    }

    public final SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, SOAPHeader parent) throws SOAPProcessingException {
        return parent.addHeaderBlock(localName, ns);
    }

    public final SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, OMDataSource ds) throws SOAPProcessingException {
        SOAPHeaderBlock element = createSOAPHeaderBlock(localName, ns);
        element.setDataSource(ds);
        return element;
    }

    public final SOAPBody createSOAPBody() throws SOAPProcessingException {
        return createSOAPBody(null);
    }

    public final SOAPBody createSOAPBody(SOAPEnvelope envelope) throws SOAPProcessingException {
        return createSOAPElement(envelope, soapVersion.getSOAPBodyClass(), SOAPConstants.BODY_LOCAL_NAME);
    }

    public final SOAPFault createSOAPFault() throws SOAPProcessingException {
        return createSOAPFault(null);
    }

    public final SOAPFault createSOAPFault(SOAPBody body) throws SOAPProcessingException {
        return createSOAPElement(body, soapVersion.getSOAPFaultClass(), SOAPConstants.BODY_FAULT_LOCAL_NAME);
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFault(org.apache.axiom.soap.SOAPBody, java.lang.Exception)
     */
    public SOAPFault createSOAPFault(SOAPBody parent, Exception e) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultCode createSOAPFaultCode() throws SOAPProcessingException {
        return createSOAPFaultCode(null);
    }

    public final SOAPFaultCode createSOAPFaultCode(SOAPFault fault) throws SOAPProcessingException {
        return createSOAPElement(fault, soapVersion.getSOAPFaultCodeClass(), soapVersion.getFaultCodeQName());
    }

    public final SOAPFaultReason createSOAPFaultReason() throws SOAPProcessingException {
        return createSOAPFaultReason(null);
    }

    public final SOAPFaultReason createSOAPFaultReason(SOAPFault fault) throws SOAPProcessingException {
        return createSOAPElement(fault, soapVersion.getSOAPFaultReasonClass(), soapVersion.getFaultReasonQName());
    }

    public final SOAPFaultRole createSOAPFaultRole() throws SOAPProcessingException {
        return createSOAPFaultRole(null);
    }

    public final SOAPFaultRole createSOAPFaultRole(SOAPFault fault) throws SOAPProcessingException {
        return createSOAPElement(fault, soapVersion.getSOAPFaultRoleClass(), soapVersion.getFaultRoleQName());
    }

    public final SOAPFaultDetail createSOAPFaultDetail() throws SOAPProcessingException {
        return createSOAPFaultDetail(null);
    }

    public final SOAPFaultDetail createSOAPFaultDetail(SOAPFault fault) throws SOAPProcessingException {
        return createSOAPElement(fault, soapVersion.getSOAPFaultDetailClass(), soapVersion.getFaultDetailQName());
    }

    private SOAPFaultValue internalCreateSOAPFaultValue(SOAPFaultClassifier parent) {
        return createSOAPElement(parent, soapVersion.getSOAPFaultValueClass(), soapVersion.getFaultValueQName());
    }
    
    public final SOAPFaultValue createSOAPFaultValue() throws SOAPProcessingException {
        return internalCreateSOAPFaultValue(null);
    }

    // TODO: Axiom 1.3 roadmap: there should be a single method taking a SOAPFaultClassifier argument
    public final SOAPFaultValue createSOAPFaultValue(SOAPFaultCode parent) throws SOAPProcessingException {
        return internalCreateSOAPFaultValue(parent);
    }

    public final SOAPFaultValue createSOAPFaultValue(SOAPFaultSubCode parent) throws SOAPProcessingException {
        return internalCreateSOAPFaultValue(parent);
    }

    private SOAPFaultSubCode internalCreateSOAPFaultSubCode(SOAPFaultClassifier parent) {
        return createSOAPElement(parent, soapVersion.getSOAPFaultSubCodeClass(), soapVersion.getFaultSubCodeQName());
    }
    
    public SOAPFaultSubCode createSOAPFaultSubCode() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    // TODO: Axiom 1.3 roadmap: there should be a single method taking a SOAPFaultClassifier argument
    public final SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultCode parent) throws SOAPProcessingException {
        return internalCreateSOAPFaultSubCode(parent);
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultSubCode(org.apache.axiom.soap.SOAPFaultSubCode)
     */
    public SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultSubCode parent)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultText createSOAPFaultText() throws SOAPProcessingException {
        return createSOAPFaultText(null);
    }

    public final SOAPFaultText createSOAPFaultText(SOAPFaultReason parent) throws SOAPProcessingException {
        QName qname = soapVersion.getFaultTextQName();
        if (qname == null) {
            throw new UnsupportedOperationException("SOAPFaultText is not supported by this SOAP version");
        }
        return createSOAPElement(parent, soapVersion.getSOAPFaultTextClass(), qname);
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultNode()
     */
    public SOAPFaultNode createSOAPFaultNode() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultNode(org.apache.axiom.soap.SOAPFault)
     */
    public SOAPFaultNode createSOAPFaultNode(SOAPFault parent) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPMessage(org.apache.axiom.soap.SOAPEnvelope, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPMessage createSOAPMessage(SOAPEnvelope envelope, OMXMLParserWrapper parserWrapper) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPHeaderBlock createSOAPHeaderBlock(OMDataSource source) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
