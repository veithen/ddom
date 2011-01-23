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
package com.google.code.ddom.frontend.axiom.soap.support;

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

import com.google.code.ddom.core.AttributeMatcher;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.util.QNameUtil;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.OMFactoryImpl;

public class SOAPFactoryImpl extends OMFactoryImpl implements SOAPFactory {
    private final SOAPVersionEx soapVersion;

    public SOAPFactoryImpl(AxiomNodeFactory nodeFactory, SOAPVersionEx soapVersionEx) {
        super(nodeFactory);
        this.soapVersion = soapVersionEx;
    }

    public final SOAPVersion getSOAPVersion() {
        return soapVersion.getSOAPVersion();
    }
    
    public final String getSoapVersionURI() {
        return soapVersion.getEnvelopeURI();
    }

    public final SOAPEnvelope getDefaultEnvelope() throws SOAPProcessingException {
        SOAPEnvelope env = createSOAPEnvelope();
        createSOAPHeader(env);
        createSOAPBody(env);
        return env;
    }

    public final SOAPEnvelope getDefaultFaultEnvelope() throws SOAPProcessingException {
        // TODO
        SOAPEnvelope defaultEnvelope = getDefaultEnvelope();
        SOAPFault fault = createSOAPFault(defaultEnvelope.getBody());
        createSOAPFaultCode(fault);
        createSOAPFaultReason(fault);
        createSOAPFaultDetail(fault);
        return defaultEnvelope;
    }

    private <T extends AxiomElement> T createElement(OMElement parent, Class<T> extensionInterface, String namespaceURI, String localName) {
        if (parent == null) {
            String prefix = namespaceURI == null ? null : SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX;
            T element = nodeFactory.createElement(null, extensionInterface, namespaceURI, localName, prefix);
            element.setOMFactory(this);
            if (prefix != null) {
                element.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
            }
            return element;
        } else {
            try {
                T element = ((AxiomElement)parent).coreAppendElement(extensionInterface, namespaceURI, localName, namespaceURI == null ? null : SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
                element.setOMFactory(this);
                return element;
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        }
    }
    
    private <T extends AxiomElement> T createElement(OMElement parent, Class<T> extensionInterface, String localName) {
        return createElement(parent, extensionInterface, soapVersion.getEnvelopeURI(), localName);
    }
    
    private <T extends AxiomElement> T createElement(OMElement parent, Class<T> extensionInterface, QName qname) {
        return createElement(parent, extensionInterface, QNameUtil.getNamespaceURI(qname), qname.getLocalPart());
    }
    
    public final SOAPEnvelope createSOAPEnvelope() throws SOAPProcessingException {
        return createElement(null, soapVersion.getSOAPEnvelopeClass(), SOAPConstants.SOAPENVELOPE_LOCAL_NAME);
    }

    public SOAPEnvelope createSOAPEnvelope(OMNamespace ns) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPHeader createSOAPHeader() throws SOAPProcessingException {
        return createSOAPHeader(null);
    }

    public final SOAPHeader createSOAPHeader(SOAPEnvelope envelope) throws SOAPProcessingException {
        return createElement(envelope, soapVersion.getSOAPHeaderClass(), SOAPConstants.HEADER_LOCAL_NAME);
    }

    public SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, SOAPHeader parent) throws SOAPProcessingException {
        return parent.addHeaderBlock(localName, ns);
    }

    public SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, OMDataSource ds)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPBody createSOAPBody() throws SOAPProcessingException {
        return createSOAPBody(null);
    }

    public final SOAPBody createSOAPBody(SOAPEnvelope envelope) throws SOAPProcessingException {
        return createElement(envelope, soapVersion.getSOAPBodyClass(), SOAPConstants.BODY_LOCAL_NAME);
    }

    public final SOAPFault createSOAPFault() throws SOAPProcessingException {
        return createSOAPFault(null);
    }

    public final SOAPFault createSOAPFault(SOAPBody body) throws SOAPProcessingException {
        return createElement(body, soapVersion.getSOAPFaultClass(), SOAPConstants.BODY_FAULT_LOCAL_NAME);
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
        return createElement(fault, soapVersion.getSOAPFaultCodeClass(), soapVersion.getFaultCodeQName());
    }

    public final SOAPFaultReason createSOAPFaultReason() throws SOAPProcessingException {
        return createSOAPFaultReason(null);
    }

    public final SOAPFaultReason createSOAPFaultReason(SOAPFault fault) throws SOAPProcessingException {
        return createElement(fault, soapVersion.getSOAPFaultReasonClass(), soapVersion.getFaultReasonQName());
    }

    public final SOAPFaultRole createSOAPFaultRole() throws SOAPProcessingException {
        return createSOAPFaultRole(null);
    }

    public final SOAPFaultRole createSOAPFaultRole(SOAPFault fault) throws SOAPProcessingException {
        return createElement(fault, soapVersion.getSOAPFaultRoleClass(), soapVersion.getFaultRoleQName());
    }

    public final SOAPFaultDetail createSOAPFaultDetail() throws SOAPProcessingException {
        return createSOAPFaultDetail(null);
    }

    public final SOAPFaultDetail createSOAPFaultDetail(SOAPFault fault) throws SOAPProcessingException {
        return createElement(fault, soapVersion.getSOAPFaultDetailClass(), soapVersion.getFaultDetailQName());
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
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultSubCode()
     */
    public SOAPFaultSubCode createSOAPFaultSubCode() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultSubCode(org.apache.axiom.soap.SOAPFaultCode)
     */
    public SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultCode parent)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultSubCode(org.apache.axiom.soap.SOAPFaultSubCode)
     */
    public SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultSubCode parent)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultText()
     */
    public SOAPFaultText createSOAPFaultText() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultText(org.apache.axiom.soap.SOAPFaultReason)
     */
    public SOAPFaultText createSOAPFaultText(SOAPFaultReason parent) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultValue()
     */
    public SOAPFaultValue createSOAPFaultValue() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultValue(org.apache.axiom.soap.SOAPFaultCode)
     */
    public SOAPFaultValue createSOAPFaultValue(SOAPFaultCode parent) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultValue(org.apache.axiom.soap.SOAPFaultSubCode)
     */
    public SOAPFaultValue createSOAPFaultValue(SOAPFaultSubCode parent)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPMessage()
     */
    public SOAPMessage createSOAPMessage() {
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

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#getNamespace()
     */
    public OMNamespace getNamespace() {
        // TODO
        throw new UnsupportedOperationException();
    }

    //
    // Methods used by StAXBuilder and StAXOMBuilder.
    //

    public final SOAPMessage createSOAPMessage(OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPEnvelope createSOAPEnvelope(OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPHeader createSOAPHeader(SOAPEnvelope envelope, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, SOAPHeader parent, OMXMLParserWrapper builder) throws SOAPProcessingException {
        throw new UnsupportedOperationException();
    }

    public final SOAPBody createSOAPBody(SOAPEnvelope envelope, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }
    
    public final SOAPFault createSOAPFault(SOAPBody parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }
    
    public final SOAPFaultCode createSOAPFaultCode(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultCode parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultSubCode parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultValue createSOAPFaultValue(SOAPFaultCode parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultValue createSOAPFaultValue(SOAPFaultSubCode parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultReason createSOAPFaultReason(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultRole createSOAPFaultRole(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultDetail createSOAPFaultDetail(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultNode createSOAPFaultNode(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultText createSOAPFaultText(SOAPFaultReason parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }
}
