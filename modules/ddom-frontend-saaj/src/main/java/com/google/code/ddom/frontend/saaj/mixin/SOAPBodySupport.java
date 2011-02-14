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

import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

import org.w3c.dom.Document;

import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPBody;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPBodyElement;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPElement;
import com.google.code.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(SAAJSOAPBody.class)
public abstract class SOAPBodySupport implements SAAJSOAPBody {
    public final Class<? extends SAAJSOAPElement> getChildType() {
        return SAAJSOAPBodyElement.class;
    }

    public final SOAPBodyElement addBodyElement(Name name) throws SOAPException {
        // TODO: need unit test with empty prefix/namespace
        try {
            return coreAppendElement(SAAJSOAPBodyElement.class, name.getURI(), name.getLocalName(), name.getPrefix());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final SOAPBodyElement addBodyElement(QName qname) throws SOAPException {
        try {
            return coreAppendElement(SAAJSOAPBodyElement.class, qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public SOAPBodyElement addDocument(Document arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final SOAPFault getFault() {
        try {
            CoreElement firstElement = coreGetFirstChildByType(CoreElement.class);
            return firstElement instanceof SOAPFault ? (SOAPFault)firstElement : null;
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }

    public final boolean hasFault() {
        return getFault() != null;
    }

    public final SOAPFault addFault() throws SOAPException {
        if (getFault() != null) {
            throw new SOAPException(""); // TODO
        } else {
            try {
                return coreAppendElement(getSOAPVersion().getSOAPFaultClass(), getNamespaceURI(), "Fault", getPrefix());
            } catch (CoreModelException ex) {
                throw SAAJExceptionUtil.toSOAPException(ex);
            }
        }
    }

    public SOAPFault addFault(Name arg0, String arg1, Locale arg2)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFault addFault(Name arg0, String arg1) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFault addFault(QName arg0, String arg1, Locale arg2)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPFault addFault(QName arg0, String arg1) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Document extractContentAsDocument() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
