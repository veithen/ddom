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

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.RolePlayer;
import org.apache.axiom.soap.SOAPHeaderBlock;

import com.google.code.ddom.collections.FilteredIterator;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPHeader;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPHeaderBlock;
import com.google.code.ddom.frontend.axiom.soap.support.RoleFilter;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.NSUtil;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;

@Mixin(AxiomSOAPHeader.class)
public abstract class SOAPHeaderSupport implements AxiomSOAPHeader {
    public final SOAPHeaderBlock addHeaderBlock(String localName, OMNamespace ns) {
        try {
            return coreAppendElement(getSOAPVersionEx().getSOAPHeaderBlockClass(), NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns));
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    // TODO: is remove supposed to work?
    public final Iterator examineAllHeaderBlocks() {
        return coreGetChildrenByType(Axis.CHILDREN, AxiomSOAPHeaderBlock.class);
    }

    // TODO: is remove supposed to work?
    public final Iterator examineHeaderBlocks(String role) {
        return new FilteredIterator<AxiomSOAPHeaderBlock>(
                coreGetChildrenByType(Axis.CHILDREN, AxiomSOAPHeaderBlock.class), new RoleFilter(role));
    }

    // TODO: is remove supposed to work?
    public Iterator examineMustUnderstandHeaderBlocks(String role) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final ArrayList getHeaderBlocksWithNSURI(String nsURI) {
        try {
            ArrayList<AxiomSOAPHeaderBlock> result = new ArrayList<AxiomSOAPHeaderBlock>();
            CoreChildNode child = coreGetFirstChild();
            while (child != null) {
                if (child instanceof AxiomSOAPHeaderBlock) {
                    AxiomSOAPHeaderBlock headerBlock = (AxiomSOAPHeaderBlock)child;
                    if (nsURI.equals(headerBlock.coreGetNamespaceURI())) {
                        result.add(headerBlock);
                    }
                }
                child = child.coreGetNextSibling();
            }
            return result;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public Iterator extractAllHeaderBlocks() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator extractHeaderBlocks(String role) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getHeadersToProcess(RolePlayer rolePlayer, String namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator getHeadersToProcess(RolePlayer rolePlayer) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
