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

import com.google.code.ddom.core.model.*;

public aspect Parents {
    declare parents: AttributeImpl implements DOMAttribute;
    declare parents: TypedAttributeImpl implements DOMTypedAttribute;
    declare parents: NamespaceDeclarationImpl implements DOMNamespaceDeclaration;
    declare parents: CharacterDataImpl implements DOMCharacterData;
    declare parents: CommentImpl implements DOMComment;
    declare parents: DocumentFragmentImpl implements DOMDocumentFragment;
    declare parents: DocumentImpl implements DOMDocument;
    declare parents: DocumentTypeImpl implements DOMDocumentType;
    declare parents: ElementImpl implements DOMElement;
    declare parents: NSAwareElementImpl implements DOMNSAwareElement;
    declare parents: NSUnawareElementImpl implements DOMNSUnawareElement;
    declare parents: EntityReferenceImpl implements DOMEntityReference;
    declare parents: ParentNodeImpl implements DOMParentNode;
    declare parents: LeafNodeImpl implements DOMLeafNode;
    declare parents: NSAwareTypedAttributeImpl implements DOMNSAwareTypedAttribute;
    declare parents: NSUnawareTypedAttributeImpl implements DOMNSUnawareTypedAttribute;
    declare parents: NodeImpl implements DOMNode;
    declare parents: ProcessingInstructionImpl implements DOMProcessingInstruction;
    declare parents: (LeafNodeImpl || ElementImpl) implements DOMChildNode;
    declare parents: TextNodeImpl implements DOMTextNode;
    declare parents: CDATASectionImpl implements DOMCDATASection;
}
