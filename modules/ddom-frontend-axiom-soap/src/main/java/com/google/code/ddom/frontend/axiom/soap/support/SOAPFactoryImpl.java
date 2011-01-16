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

import org.apache.axiom.om.OMDataSource;
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

import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPBody;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPEnvelope;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFault;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultCode;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultDetail;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultReason;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultRole;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPHeader;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.OMFactoryImpl;

public class SOAPFactoryImpl extends OMFactoryImpl implements SOAPFactory {
    private final SOAPVersionEx soapVersionEx;

    public SOAPFactoryImpl(AxiomNodeFactory nodeFactory, SOAPVersionEx soapVersionEx) {
        super(nodeFactory);
        this.soapVersionEx = soapVersionEx;
    }

    public final SOAPVersion getSOAPVersion() {
        return soapVersionEx.getSOAPVersion();
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

    public final SOAPEnvelope createSOAPEnvelope() throws SOAPProcessingException {
        AxiomSOAPEnvelope element = nodeFactory.createElement(null,
                soapVersionEx.getSOAPEnvelopeClass(), soapVersionEx.getEnvelopeURI(),
                SOAPConstants.SOAPENVELOPE_LOCAL_NAME, SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
        element.setOMFactory(this);
        return element;
    }

    public SOAPEnvelope createSOAPEnvelope(OMNamespace ns) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPHeader createSOAPHeader() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPHeader createSOAPHeader(SOAPEnvelope envelope) throws SOAPProcessingException {
        try {
            AxiomSOAPHeader header = ((AxiomSOAPEnvelope)envelope).coreAppendElement(
                    soapVersionEx.getSOAPHeaderClass(), soapVersionEx.getEnvelopeURI(),
                    SOAPConstants.HEADER_LOCAL_NAME, SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            header.setOMFactory(this);
            return header;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public SOAPBody createSOAPBody() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPBody createSOAPBody(SOAPEnvelope envelope) throws SOAPProcessingException {
        try {
            AxiomSOAPBody body = ((AxiomSOAPEnvelope)envelope).coreAppendElement(
                    soapVersionEx.getSOAPBodyClass(), soapVersionEx.getEnvelopeURI(),
                    SOAPConstants.BODY_LOCAL_NAME, SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            body.setOMFactory(this);
            return body;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFault createSOAPFault() throws SOAPProcessingException {
        AxiomSOAPFault element = nodeFactory.createElement(null,
                soapVersionEx.getSOAPFaultClass(), soapVersionEx.getEnvelopeURI(),
                SOAPConstants.BODY_FAULT_LOCAL_NAME, SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
        element.setOMFactory(this);
        return element;
    }

    public final SOAPFault createSOAPFault(SOAPBody body) throws SOAPProcessingException {
        try {
            AxiomSOAPFault fault = ((AxiomSOAPBody)body).coreAppendElement(
                    soapVersionEx.getSOAPFaultClass(), soapVersionEx.getEnvelopeURI(),
                    SOAPConstants.BODY_FAULT_LOCAL_NAME, SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            fault.setOMFactory(this);
            return fault;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFault(org.apache.axiom.soap.SOAPBody, java.lang.Exception)
     */
    public SOAPFault createSOAPFault(SOAPBody parent, Exception e) throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFault(org.apache.axiom.soap.SOAPBody, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFault createSOAPFault(SOAPBody parent, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultCode()
     */
    public SOAPFaultCode createSOAPFaultCode() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultCode createSOAPFaultCode(SOAPFault fault) throws SOAPProcessingException {
        try {
            AxiomSOAPFaultCode code = ((AxiomSOAPFault)fault).coreAppendElement(
                    soapVersionEx.getSOAPFaultCodeClass(), soapVersionEx.getEnvelopeURI(),
                    soapVersionEx.getSOAPVersion().getFaultCodeQName().getLocalPart(), SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            code.setOMFactory(this);
            return code;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultCode createSOAPFaultCode(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultReason()
     */
    public SOAPFaultReason createSOAPFaultReason() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultReason createSOAPFaultReason(SOAPFault fault) throws SOAPProcessingException {
        try {
            AxiomSOAPFaultReason code = ((AxiomSOAPFault)fault).coreAppendElement(
                    soapVersionEx.getSOAPFaultReasonClass(), soapVersionEx.getEnvelopeURI(),
                    soapVersionEx.getSOAPVersion().getFaultReasonQName().getLocalPart(), SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            code.setOMFactory(this);
            return code;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultReason(org.apache.axiom.soap.SOAPFault, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFaultReason createSOAPFaultReason(SOAPFault parent, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultRole()
     */
    public SOAPFaultRole createSOAPFaultRole() throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultRole createSOAPFaultRole(SOAPFault fault) throws SOAPProcessingException {
        try {
            AxiomSOAPFaultRole role = ((AxiomSOAPFault)fault).coreAppendElement(
                    soapVersionEx.getSOAPFaultRoleClass(), soapVersionEx.getEnvelopeURI(),
                    soapVersionEx.getSOAPVersion().getFaultRoleQName().getLocalPart(), SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            role.setOMFactory(this);
            return role;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultRole createSOAPFaultRole(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final SOAPFaultDetail createSOAPFaultDetail() throws SOAPProcessingException {
        AxiomSOAPFaultDetail detail = nodeFactory.createElement(null,
                soapVersionEx.getSOAPFaultDetailClass(), soapVersionEx.getEnvelopeURI(),
                soapVersionEx.getSOAPVersion().getFaultDetailQName().getLocalPart(), SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
        detail.setOMFactory(this);
        return detail;
    }

    public final SOAPFaultDetail createSOAPFaultDetail(SOAPFault fault) throws SOAPProcessingException {
        try {
            AxiomSOAPFaultDetail detail = ((AxiomSOAPFault)fault).coreAppendElement(
                    soapVersionEx.getSOAPFaultDetailClass(), soapVersionEx.getEnvelopeURI(),
                    soapVersionEx.getSOAPVersion().getFaultDetailQName().getLocalPart(), SOAPConstants.SOAP_DEFAULT_NAMESPACE_PREFIX);
            detail.setOMFactory(this);
            return detail;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final SOAPFaultDetail createSOAPFaultDetail(SOAPFault parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
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
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultNode(org.apache.axiom.soap.SOAPFault, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFaultNode createSOAPFaultNode(SOAPFault parent, OMXMLParserWrapper builder) {
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
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultSubCode(org.apache.axiom.soap.SOAPFaultCode, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultCode parent, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultSubCode(org.apache.axiom.soap.SOAPFaultSubCode, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFaultSubCode createSOAPFaultSubCode(SOAPFaultSubCode parent,
            OMXMLParserWrapper builder) {
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
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultText(org.apache.axiom.soap.SOAPFaultReason, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFaultText createSOAPFaultText(SOAPFaultReason parent, OMXMLParserWrapper builder) {
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
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultValue(org.apache.axiom.soap.SOAPFaultCode, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFaultValue createSOAPFaultValue(SOAPFaultCode parent, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPFaultValue(org.apache.axiom.soap.SOAPFaultSubCode, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPFaultValue createSOAPFaultValue(SOAPFaultSubCode parent, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPHeaderBlock(java.lang.String, org.apache.axiom.om.OMNamespace)
     */
    public SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPHeaderBlock(java.lang.String, org.apache.axiom.om.OMNamespace, org.apache.axiom.soap.SOAPHeader)
     */
    public SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, SOAPHeader parent)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPHeaderBlock(java.lang.String, org.apache.axiom.om.OMNamespace, org.apache.axiom.om.OMDataSource)
     */
    public SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, OMDataSource ds)
            throws SOAPProcessingException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPHeaderBlock(java.lang.String, org.apache.axiom.om.OMNamespace, org.apache.axiom.soap.SOAPHeader, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns,
            SOAPHeader parent, OMXMLParserWrapper builder) throws SOAPProcessingException {
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
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPMessage(org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPMessage createSOAPMessage(OMXMLParserWrapper builder) {
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

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#getSoapVersionURI()
     */
    public String getSoapVersionURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPEnvelope(org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPEnvelope createSOAPEnvelope(OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPHeader(org.apache.axiom.soap.SOAPEnvelope, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPHeader createSOAPHeader(SOAPEnvelope envelope, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.soap.SOAPFactory#createSOAPBody(org.apache.axiom.soap.SOAPEnvelope, org.apache.axiom.om.OMXMLParserWrapper)
     */
    public SOAPBody createSOAPBody(SOAPEnvelope envelope, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

}
