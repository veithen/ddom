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
package com.google.code.ddom.frontend.axiom.soap.mixin;

import java.util.Iterator;

import org.apache.axiom.om.OMElement;

import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPFaultDetail;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.Policies;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;

@Mixin(AxiomSOAPFaultDetail.class)
public abstract class SOAPFaultDetailSupport implements AxiomSOAPFaultDetail {
    public final void addDetailEntry(OMElement detailElement) {
        try {
            coreAppendChild((AxiomElement)detailElement, Policies.NODE_MIGRATION_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final Iterator getAllDetailEntries() {
        return coreGetChildrenByType(Axis.CHILDREN, AxiomElement.class);
    }
}
