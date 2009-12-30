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
package com.google.code.ddom.frontend.dom.aspects;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.frontend.dom.intf.AbortNormalizationException;
import com.google.code.ddom.frontend.dom.intf.NormalizationConfig;

import com.google.code.ddom.frontend.dom.intf.*;

/**
 * Aspect implementing {@link org.w3c.dom.Node#normalize()} and
 * {@link org.w3c.dom.Document#normalizeDocument()}.
 * 
 * @author Andreas Veithen
 */
public aspect Normalization {
    public final void DOMNode.normalize() {
        try {
            normalize(NormalizationConfig.DEFAULT);
        } catch (AbortNormalizationException ex) {
            // Do nothing, just abort.
        }
    }
    
    public final void DOMDocument.normalizeDocument() {
        try {
            normalize((NormalizationConfig)getDomConfig());
        } catch (AbortNormalizationException ex) {
            // Do nothing, just abort.
        }
    }
    
    public final void DOMLeafNode.normalize(NormalizationConfig config) throws AbortNormalizationException {
        
    }
    
    private void DOMParentNode.normalizeChildren(NormalizationConfig config) throws AbortNormalizationException {
        CoreChildNode child = coreGetFirstChild();
        while (child != null) {
            ((DOMNode)child).normalize(config);
            child = child.coreGetNextSibling();
        }
    }
    
    public final void DOMElement.normalize(NormalizationConfig config) throws AbortNormalizationException {
        coreCoalesce(!config.isKeepCDATASections());
        normalizeChildren(config);
    }
    
    public final void DOMDocument.normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }
    
    public final void DOMDocumentFragment.normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }
    
    public final void DOMAttribute.normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }
}
