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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.WrongDocumentException;

@Implementation
public abstract class Node implements CoreNode {
    public final CoreDocument coreGetDocument() {
        return getDocument();
    }

    abstract Document getDocument();
    
    public final void validateOwnerDocument(CoreNode node) throws WrongDocumentException {
        CoreDocument document1 = ((Node)node).getDocument(); // TODO: get rid of cast
        CoreDocument document2 = getDocument();
        if (document1 != null && document2 != null && document1 != document2) {
            throw new WrongDocumentException();
        }
    }
}
