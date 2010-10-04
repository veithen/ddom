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
package com.google.code.ddom.frontend.saaj.mixin;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;

import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.ElementAlreadyExistsException;
import com.google.code.ddom.core.SequenceOperation;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.ext.SOAPEnvelopeExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPEnvelope;
import com.google.code.ddom.frontend.saaj.support.NameImpl;
import com.google.code.ddom.frontend.saaj.support.SAAJExceptionUtil;

@Mixin(SOAPEnvelopeExtension.class)
public abstract class SOAPEnvelopeSupport implements SAAJSOAPEnvelope {
    public SOAPHeader getHeader() throws SOAPException {
        try {
            return (SOAPHeader)coreQuerySequence(getSOAPVersion().getEnvelopeSequence(), 0, SequenceOperation.GET);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }
    
    public SOAPHeader addHeader() throws SOAPException {
        try {
            return (SOAPHeader)coreQuerySequence(getSOAPVersion().getEnvelopeSequence(), 0, SequenceOperation.CREATE);
        } catch (ElementAlreadyExistsException ex) {
            throw new SOAPException("Can't add a header when one is already present");
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public SOAPBody getBody() throws SOAPException {
        try {
            return (SOAPBody)coreQuerySequence(getSOAPVersion().getEnvelopeSequence(), 1, SequenceOperation.GET);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public SOAPBody addBody() throws SOAPException {
        try {
            return (SOAPBody)coreQuerySequence(getSOAPVersion().getEnvelopeSequence(), 1, SequenceOperation.CREATE);
        } catch (ElementAlreadyExistsException ex) {
            throw new SOAPException("Can't add a body when one is already present");
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public Name createName(String localName, String prefix, String uri) throws SOAPException {
        return new NameImpl(localName, prefix, uri);
    }

    public Name createName(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
