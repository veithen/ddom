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

import org.apache.axiom.om.OMElement;

import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;

public aspect DocumentSupport {
    public String AxiomDocument.getXMLVersion() {
        return coreGetXmlVersion();
    }
    
    public void AxiomDocument.setXMLVersion(String version) {
        coreSetXmlVersion(version);
    }
    
    public String AxiomDocument.getCharsetEncoding() {
        // TODO: need to check that this is the right property!
        return coreGetXmlEncoding();
    }
    
    public void AxiomDocument.setCharsetEncoding(String charsetEncoding) {
        // TODO: need to check that this is the right property!
        coreSetXmlEncoding(charsetEncoding);
    }
    
    public String AxiomDocument.isStandalone() {
        return coreGetStandalone() ? "yes" : "no";
    }

    public void AxiomDocument.setStandalone(String isStandalone) {
        coreSetStandalone("yes".equals(isStandalone));
    }
    
    public OMElement AxiomDocument.getOMDocumentElement() {
        return (OMElement)coreGetDocumentElement();
    }
    
    public void AxiomDocument.setOMDocumentElement(@SuppressWarnings("unused") OMElement rootElement) {
        throw new UnsupportedOperationException("This operation is unsupported because it is ill-defined in the Axiom API");
    }
}
