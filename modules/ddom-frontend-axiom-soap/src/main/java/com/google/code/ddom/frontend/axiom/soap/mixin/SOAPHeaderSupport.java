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
package com.google.code.ddom.frontend.axiom.soap.mixin;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.RolePlayer;
import org.apache.axiom.soap.SOAPHeaderBlock;

import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.soap.ext.SOAPHeaderExtension;
import com.google.code.ddom.frontend.axiom.soap.intf.AxiomSOAPHeader;

@Mixin(SOAPHeaderExtension.class)
public abstract class SOAPHeaderSupport implements AxiomSOAPHeader {
    public SOAPHeaderBlock addHeaderBlock(String localName, OMNamespace ns) throws OMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator examineAllHeaderBlocks() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator examineHeaderBlocks(String role) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator examineMustUnderstandHeaderBlocks(String role) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator extractAllHeaderBlocks() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Iterator extractHeaderBlocks(String role) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public ArrayList getHeaderBlocksWithNSURI(String nsURI) {
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
