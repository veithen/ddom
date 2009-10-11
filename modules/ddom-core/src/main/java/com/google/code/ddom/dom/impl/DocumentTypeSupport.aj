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
package com.google.code.ddom.dom.impl;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

public aspect DocumentTypeSupport {
    declare parents: DocumentTypeImpl implements DocumentType;
    
    public final String DocumentTypeImpl.getName() {
        return coreGetRootName();
    }

    public final String DocumentTypeImpl.getPublicId() {
        return coreGetPublicId();
    }

    public final String DocumentTypeImpl.getSystemId() {
        return coreGetSystemId();
    }

    public final NamedNodeMap DocumentTypeImpl.getEntities() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String DocumentTypeImpl.getInternalSubset() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final NamedNodeMap DocumentTypeImpl.getNotations() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
