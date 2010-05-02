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
package com.google.code.ddom.frontend.axiom.aspects;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.frontend.axiom.intf.AxiomTextNode;

public aspect TextNodeSupport {
    public String AxiomTextNode.getText() {
        return coreGetData();
    }
    
    public char[] AxiomTextNode.getTextCharacters() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean AxiomTextNode.isCharacters() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName AxiomTextNode.getTextAsQName() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace AxiomTextNode.getNamespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public DataHandler AxiomTextNode.getDataHandler() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean AxiomTextNode.isOptimized() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomTextNode.setOptimize(boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public boolean AxiomTextNode.isBinary() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomTextNode.setBinary(boolean binary) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String AxiomTextNode.getContentID() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomTextNode.setContentID(String contentID) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
