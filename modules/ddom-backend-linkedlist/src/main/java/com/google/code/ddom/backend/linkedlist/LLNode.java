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

import com.google.code.ddom.core.CoreNode;
import com.google.code.ddom.core.WrongDocumentException;

public interface LLNode extends CoreNode {
    // TODO: need to check if we really need interface methods for this
    boolean internalGetFlag(int flag);
    void internalSetFlag(int flag, boolean value);
    
    LLDocument internalGetOwnerDocument();
    
    void internalValidateOwnerDocument(CoreNode node) throws WrongDocumentException;
}
