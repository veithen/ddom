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
package com.googlecode.ddom.frontend.axiom.mixin;

import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMNode;

import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomChildNode;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.googlecode.ddom.frontend.axiom.support.Policies;

@Mixin(CoreChildNode.class)
public abstract class ChildNodeSupport implements AxiomChildNode {
    public OMContainer getParent() {
        return (OMContainer)coreGetParent();
    }
    
    public OMNode getPreviousOMSibling() {
        try {
            return (OMNode)coreGetPreviousSibling();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public OMNode getNextOMSibling() {
        try {
            return (OMNode)coreGetNextSibling();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void insertSiblingBefore(OMNode sibling) {
        try {
            coreInsertSiblingBefore((CoreChildNode)sibling, Policies.NODE_MIGRATION_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void insertSiblingAfter(OMNode sibling) {
        try {
            coreInsertSiblingAfter((CoreChildNode)sibling, Policies.NODE_MIGRATION_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public OMNode detach() {
        try {
            coreDetach();
            return this;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void buildWithAttachments() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void discard() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
