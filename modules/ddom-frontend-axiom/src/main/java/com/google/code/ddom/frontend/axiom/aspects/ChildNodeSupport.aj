/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.frontend.axiom.aspects;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMNode;

import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomChildNode;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

public aspect ChildNodeSupport {
    public OMContainer AxiomChildNode.getParent() {
        return (OMContainer)coreGetParent();
    }
    
    public OMNode AxiomChildNode.getPreviousOMSibling() {
        try {
            return (OMNode)coreGetPreviousSibling();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public OMNode AxiomChildNode.getNextOMSibling() {
        try {
            return (OMNode)coreGetNextSibling();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void AxiomChildNode.insertSiblingBefore(OMNode sibling) {
        try {
            coreInsertSiblingBefore((CoreNode)sibling);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void AxiomChildNode.insertSiblingAfter(OMNode sibling) {
        try {
            coreInsertSiblingAfter((CoreNode)sibling);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public OMNode AxiomChildNode.detach() {
        try {
            coreDetach();
            return null; // TODO
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
}
