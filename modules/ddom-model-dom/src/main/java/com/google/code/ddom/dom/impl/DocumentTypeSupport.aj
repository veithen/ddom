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

import org.w3c.dom.NamedNodeMap;

public aspect DocumentTypeSupport {
    public final String DOMDocumentType.getName() {
        return coreGetRootName();
    }

    public final String DOMDocumentType.getPublicId() {
        return coreGetPublicId();
    }

    public final String DOMDocumentType.getSystemId() {
        return coreGetSystemId();
    }

    public final NamedNodeMap DOMDocumentType.getEntities() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String DOMDocumentType.getInternalSubset() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final NamedNodeMap DOMDocumentType.getNotations() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
