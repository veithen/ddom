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

import javax.xml.soap.SOAPException;

import com.google.code.ddom.frontend.dom.support.DOM2AttributeMatcher;
import com.google.code.ddom.frontend.saaj.intf.SAAJSOAPHeaderElement;
import com.google.code.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(SAAJSOAPHeaderElement.class)
public abstract class SOAPHeaderElementSupport implements SAAJSOAPHeaderElement {
    private String getSOAPAttribute(String localName) {
        try {
            CoreNSAwareAttribute attr = (CoreNSAwareAttribute)coreGetAttribute(DOM2AttributeMatcher.INSTANCE, getSOAPVersion().getEnvelopeNamespaceURI(), localName);
            return attr == null ? null : attr.coreGetTextContent(TextCollectorPolicy.DEFAULT);
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toRuntimeException(ex);
        }
    }
    
    private void setSOAPAttribute(String localName, String value) {
        // TODO: ugly, use core model methods directly
        setAttributeNS(getSOAPVersion().getEnvelopeNamespaceURI(), "SOAP-ENV:" + localName, value);
    }
    
    public final String getActor() {
        return getSOAPAttribute(getSOAPVersion().getActorAttributeLocalName());
    }

    public final void setActor(String actor) {
        setSOAPAttribute(getSOAPVersion().getActorAttributeLocalName(), actor);
    }

    public final boolean getMustUnderstand() {
        String value = getSOAPAttribute("mustUnderstand");
        return value != null && getSOAPVersion().parseMustUnderstand(value);
    }

    public final void setMustUnderstand(boolean mustUnderstand) {
        setSOAPAttribute("mustUnderstand", getSOAPVersion().formatMustUnderstand(mustUnderstand));
    }

    public boolean getRelay() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getRole() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setRelay(boolean arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setRole(String arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
