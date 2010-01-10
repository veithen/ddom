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

import org.apache.axiom.om.OMXMLParserWrapper;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.frontend.axiom.intf.AxiomChildNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomContainer;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomLeafNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomNode;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;

public aspect DeferredParsing {
    public void AxiomNode.close(boolean build) {
        CoreDocument document = getDocument();
        if (build) {
            try {
                // TODO: not sure if the document or only the node should be built
                document.coreBuild();
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        }
        // TODO
//        document.dispose();
    }
    
    public boolean AxiomContainer.isComplete() {
        return coreIsComplete();
    }
    
    public boolean AxiomLeafNode.isComplete() {
        // A leaf node is always complete
        return true;
    }
    
    public void AxiomContainer.build() {
        try {
            coreBuild();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void AxiomLeafNode.build() {
        // Do nothing: a leaf node is always complete
    }
    
    public void AxiomChildNode.buildWithAttachments() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomChildNode.discard() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomContainer.buildNext() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomElement.setBuilder(OMXMLParserWrapper wrapper) {
        throw new UnsupportedOperationException();
    }
    
    public OMXMLParserWrapper AxiomElement.getBuilder() {
        return null;
    }
}
