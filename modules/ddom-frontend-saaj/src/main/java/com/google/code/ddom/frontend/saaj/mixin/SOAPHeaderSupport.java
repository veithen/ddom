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

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;

import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPHeader;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPHeaderElement;
import com.google.code.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(SAAJSOAPHeader.class)
public abstract class SOAPHeaderSupport implements SAAJSOAPHeader {
    public final SOAPHeaderElement addHeaderElement(Name name) throws SOAPException {
        // TODO: need unit test with empty prefix/namespace
        try {
            return (SAAJSOAPHeaderElement)coreAppendElement(getChildType(), name.getURI(), name.getLocalName(), name.getPrefix());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final SOAPHeaderElement addHeaderElement(QName qname) throws SOAPException {
        try {
            return (SAAJSOAPHeaderElement)coreAppendElement(getChildType(), qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public SOAPHeaderElement addNotUnderstoodHeaderElement(QName arg0)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPHeaderElement addUpgradeHeaderElement(Iterator arg0)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPHeaderElement addUpgradeHeaderElement(String arg0)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPHeaderElement addUpgradeHeaderElement(String[] arg0)
            throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator examineAllHeaderElements() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator examineHeaderElements(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator examineMustUnderstandHeaderElements(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator extractAllHeaderElements() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator extractHeaderElements(String actor) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
