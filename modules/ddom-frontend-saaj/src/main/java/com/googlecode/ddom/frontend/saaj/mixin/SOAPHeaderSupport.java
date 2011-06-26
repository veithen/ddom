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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;

import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ElementMatcher;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPHeader;
import com.googlecode.ddom.frontend.saaj.intf.SAAJSOAPHeaderElement;
import com.googlecode.ddom.frontend.saaj.support.HeaderElementMatcher;
import com.googlecode.ddom.frontend.saaj.support.MustUnderstandHeaderElementMatcher;

@Mixin(SAAJSOAPHeader.class)
public abstract class SOAPHeaderSupport implements SAAJSOAPHeader {
    public final SOAPHeaderElement addHeaderElement(Name name) throws SOAPException {
        return (SOAPHeaderElement)addChildElement(name);
    }

    public final SOAPHeaderElement addHeaderElement(QName qname) throws SOAPException {
        return (SOAPHeaderElement)addChildElement(qname);
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

    public final Iterator examineAllHeaderElements() {
        return getChildElements();
    }

    public final Iterator examineHeaderElements(String actor) {
        return examineHeaderElements(HeaderElementMatcher.INSTANCE, actor);
    }

    public final Iterator examineMustUnderstandHeaderElements(String actor) {
        return examineHeaderElements(MustUnderstandHeaderElementMatcher.INSTANCE, actor);
    }
    
    private Iterator examineHeaderElements(ElementMatcher<SAAJSOAPHeaderElement> matcher, String param) {
        // TODO: we may potentially have an issue with elements that have not been reified yet (i.e. that have been created using plain DOM methods)
        return coreGetElements(Axis.CHILDREN, SAAJSOAPHeaderElement.class, matcher, null, param);
    }

    private <T> Iterator<T> extract(Iterator<T> elements) {
        List<T> list = new ArrayList<T>();
        while (elements.hasNext()) {
            list.add(elements.next());
            elements.remove();
        }
        return list.iterator();
    }
    
    public final Iterator extractAllHeaderElements() {
        return extract(examineAllHeaderElements());
    }

    public final Iterator extractHeaderElements(String actor) {
        return extract(examineHeaderElements(actor));
    }
}
