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

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;

import com.google.code.ddom.core.Axis;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.saaj.ext.DetailEntryExtension;
import com.google.code.ddom.frontend.saaj.ext.DetailExtension;
import com.google.code.ddom.frontend.saaj.intf.SAAJDetail;
import com.google.code.ddom.frontend.saaj.support.ReifyingIterator;

@Mixin(DetailExtension.class)
public abstract class DetailSupport implements SAAJDetail {
    public DetailEntry addDetailEntry(Name arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public DetailEntry addDetailEntry(QName arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator<DetailEntry> getDetailEntries() {
        return new ReifyingIterator<DetailEntry>(coreGetChildrenByType(Axis.CHILDREN, CoreNSAwareElement.class), DetailEntryExtension.class, DetailEntry.class);
    }
}
