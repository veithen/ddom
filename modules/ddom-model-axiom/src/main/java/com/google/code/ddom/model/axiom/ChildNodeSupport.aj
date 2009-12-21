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
package com.google.code.ddom.model.axiom;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMNode;

import com.google.code.ddom.spi.model.CoreModelException;
import com.google.code.ddom.spi.model.CoreNode;

public aspect ChildNodeSupport {
    public OMContainer AxiomChildNode.getParent() {
        return (OMContainer)coreGetParent();
    }
    
    public OMNode AxiomChildNode.getPreviousOMSibling() {
        return (OMNode)coreGetPreviousSibling();
    }
    
    public OMNode AxiomChildNode.getNextOMSibling() {
        return (OMNode)coreGetNextSibling();
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
        coreDetach();
        return null; // TODO
    }
}
