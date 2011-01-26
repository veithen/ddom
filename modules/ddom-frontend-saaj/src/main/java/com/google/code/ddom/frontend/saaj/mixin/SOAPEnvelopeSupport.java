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
package com.google.code.ddom.frontend.saaj.mixin;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;

import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPEnvelope;
import com.google.code.ddom.frontend.saaj.support.NameImpl;
import com.google.code.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.ElementAlreadyExistsException;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(SAAJSOAPEnvelope.class)
public abstract class SOAPEnvelopeSupport implements SAAJSOAPEnvelope {
    public final SOAPHeader getHeader() throws SOAPException {
        try {
            return (SOAPHeader)coreGetElementFromSequence(getSOAPVersion().getEnvelopeSequence(), 0, false);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }
    
    public final SOAPHeader addHeader() throws SOAPException {
        try {
            return (SOAPHeader)coreCreateElementInSequence(getSOAPVersion().getEnvelopeSequence(), 0);
        } catch (ElementAlreadyExistsException ex) {
            throw new SOAPException("Can't add a header when one is already present");
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final SOAPBody getBody() throws SOAPException {
        try {
            return (SOAPBody)coreGetElementFromSequence(getSOAPVersion().getEnvelopeSequence(), 1, false);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final SOAPBody addBody() throws SOAPException {
        try {
            return (SOAPBody)coreCreateElementInSequence(getSOAPVersion().getEnvelopeSequence(), 1);
        } catch (ElementAlreadyExistsException ex) {
            throw new SOAPException("Can't add a body when one is already present");
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final Name createName(String localName, String prefix, String uri) throws SOAPException {
        return new NameImpl(localName, prefix, uri);
    }

    public final Name createName(String localName) throws SOAPException {
        return new NameImpl(localName, null, null);
    }
}
