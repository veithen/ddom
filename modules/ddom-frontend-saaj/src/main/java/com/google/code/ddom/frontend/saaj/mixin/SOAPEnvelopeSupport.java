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

import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.ext.SOAPEnvelopeExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPBody;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPEnvelope;
import com.google.code.ddom.frontend.saaj.support.NameImpl;

@Mixin(SOAPEnvelopeExtension.class)
public abstract class SOAPEnvelopeSupport implements SAAJSOAPEnvelope {
    // TODO: this should not be necessary; it is because the weaver doesn't support abstract model extension classes
    public Class<?> getSOAPBodyExtension() { throw new IllegalStateException(); }
    public Class<?> getSOAPHeaderExtension() { throw new IllegalStateException(); }

    public SOAPHeader getHeader() throws SOAPException {
        try {
            CoreElement firstElement = coreGetFirstChildByType(CoreElement.class);
            return firstElement instanceof SOAPHeader ? (SOAPHeader)firstElement : null;
        } catch (CoreModelException ex) {
            throw new SOAPException(ex); // TODO
        }
    }
    
    public SOAPHeader addHeader() throws SOAPException {
        if (getHeader() != null) {
            throw new SOAPException("Can't add a header when one is already present");
        } else {
            SOAPHeader header = (SOAPHeader)coreGetDocument().coreCreateElement(getSOAPHeaderExtension(), getNamespaceURI(), "Header", getPrefix());
            insertBefore(header, getFirstChild());
            return header;
        }
    }

    public SOAPBody getBody() throws SOAPException {
        try {
            return coreGetFirstChildByType(SAAJSOAPBody.class);
        } catch (CoreModelException ex) {
            throw new SOAPException(ex); // TODO
        }
    }

    public SOAPBody addBody() throws SOAPException {
        if (getBody() != null) {
            throw new SOAPException("Can't add a body when one is already present");
        } else {
            SOAPBody body = (SOAPBody)coreGetDocument().coreCreateElement(getSOAPBodyExtension(), getNamespaceURI(), "Body", getPrefix());
            appendChild(body);
            return body;
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
