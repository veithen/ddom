/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.support;

import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNamespaceDeclaration;

public class PrefixIterator extends AbstractNamespaceIterator<String> {
    private final String namespaceURI;
    
    public PrefixIterator(CoreElement element, String namespaceURI) {
        super(element);
        this.namespaceURI = namespaceURI;
    }

    @Override
    protected boolean matches(CoreNamespaceDeclaration nsDeclaration) {
        try {
            return nsDeclaration.coreGetDeclaredNamespaceURI().equals(namespaceURI);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    @Override
    protected String getValue(CoreNamespaceDeclaration nsDeclaration) {
        return nsDeclaration.coreGetDeclaredPrefix();
    }
}
