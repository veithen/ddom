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
package com.googlecode.ddom.frontend.saaj.mixin;

import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

import org.w3c.dom.Document;

import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPBody;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPBodyElement;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPElement;
import com.googlecode.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.stream.dom.DOMSource;

@Mixin(SAAJSOAPBody.class)
public abstract class SOAPBodySupport implements SAAJSOAPBody {
    public final Class<? extends SAAJSOAPElement> getChildType() {
        return SAAJSOAPBodyElement.class;
    }

    public final SOAPBodyElement addBodyElement(Name name) throws SOAPException {
        return (SOAPBodyElement)addChildElement(name);
    }

    public final SOAPBodyElement addBodyElement(QName qname) throws SOAPException {
        return (SOAPBodyElement)addChildElement(qname);
    }

    public final SOAPBodyElement addDocument(Document document) throws SOAPException {
        // TODO: if the document was created using a DDOM model, we can optimize things a bit
        try {
            // From the Javadoc of SOAPBody#addDocument: "Calling this method invalidates the
            // document parameter. The client application should discard all references to this
            // Document and its contents upon calling addDocument. The behavior of an application
            // that continues to use such references is undefined."
            // This means that we can safely use a DOMSource here and defer creation of the nodes:
            SAAJSOAPBodyElement child = coreAppendElement(SAAJSOAPBodyElement.class, null, null, null);
            child.coreSetSource(new DOMSource(document.getDocumentElement()));
            return child;
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
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
