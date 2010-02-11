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

import com.google.code.ddom.backend.CoreCDATASection;
import com.google.code.ddom.backend.CoreComment;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreDocumentTypeDeclaration;
import com.google.code.ddom.backend.CoreEntityReference;
import com.google.code.ddom.backend.CoreNSAwareElement;
import com.google.code.ddom.backend.CoreNSAwareAttribute;
import com.google.code.ddom.backend.CoreNSUnawareElement;
import com.google.code.ddom.backend.CoreNSUnawareAttribute;
import com.google.code.ddom.backend.CoreNamespaceDeclaration;
import com.google.code.ddom.backend.CoreProcessingInstruction;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.NodeFactory;

public class NodeFactoryImpl implements NodeFactory {
    public CoreDocument createDocument() {
        return new Document(this);
    }
}
