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

import org.apache.axiom.om.OMElement;

import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomDocument;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.googlecode.ddom.frontend.axiom.support.Policies;

@Mixin(CoreDocument.class)
public abstract class DocumentSupport implements AxiomDocument {
    public String getXMLVersion() {
        try {
            return coreGetXmlVersion();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void setXMLVersion(String version) {
        coreSetXmlVersion(version);
    }
    
    public String getCharsetEncoding() {
        try {
            // TODO: need to check that this is the right property!
            return coreGetXmlEncoding();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void setCharsetEncoding(String charsetEncoding) {
        // TODO: need to check that this is the right property!
        coreSetXmlEncoding(charsetEncoding);
    }
    
    public String isStandalone() {
        try {
            return coreGetStandalone() ? "yes" : "no";
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void setStandalone(String isStandalone) {
        coreSetStandalone("yes".equals(isStandalone));
    }
    
    public OMElement getOMDocumentElement() {
        try {
            return (OMElement)coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public final void setOMDocumentElement(OMElement documentElement) {
        if (documentElement == null) {
            throw new IllegalArgumentException("documentElement must not be null");
        }
        try {
            CoreElement existingDocumentElement = coreGetDocumentElement();
            if (existingDocumentElement == null) {
                coreAppendChild((AxiomElement)documentElement, Policies.NODE_MIGRATION_POLICY);
            } else {
                existingDocumentElement.coreReplaceWith((AxiomElement)documentElement);
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void close(boolean build) {
        // TODO: this should be implemented by a common mixin
    }
}
