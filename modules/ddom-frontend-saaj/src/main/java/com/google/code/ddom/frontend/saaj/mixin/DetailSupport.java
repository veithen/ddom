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
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.frontend.saaj.intf.SAAJDetail;
import com.google.code.ddom.frontend.saaj.intf.SAAJDetailEntry;
import com.google.code.ddom.frontend.saaj.support.NameUtil;
import com.google.code.ddom.frontend.saaj.support.ReifyingIterator;
import com.google.code.ddom.frontend.saaj.support.SAAJExceptionUtil;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(SAAJDetail.class)
public abstract class DetailSupport implements SAAJDetail {
    public final DetailEntry addDetailEntry(Name name) throws SOAPException {
        try {
            return coreAppendElement(SAAJDetailEntry.class, NameUtil.getNamespaceURI(name), name.getLocalName(), NameUtil.getPrefix(name));
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final DetailEntry addDetailEntry(QName qname) throws SOAPException {
        try {
            return coreAppendElement(SAAJDetailEntry.class, qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
        } catch (CoreModelException ex) {
            throw SAAJExceptionUtil.toSOAPException(ex);
        }
    }

    public final Iterator getDetailEntries() {
        return new ReifyingIterator<SAAJDetailEntry>(coreGetChildrenByType(Axis.CHILDREN, CoreNSAwareElement.class), SAAJDetailEntry.class);
    }
}
