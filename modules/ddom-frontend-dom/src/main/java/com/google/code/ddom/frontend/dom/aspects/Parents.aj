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
package com.google.code.ddom.frontend.dom.aspects;

import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreCDATASection;
import com.google.code.ddom.backend.CoreCharacterData;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreComment;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreDocumentType;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreEntityReference;
import com.google.code.ddom.backend.CoreLeafNode;
import com.google.code.ddom.backend.CoreNSAwareElement;
import com.google.code.ddom.backend.CoreNSAwareTypedAttribute;
import com.google.code.ddom.backend.CoreNSUnawareElement;
import com.google.code.ddom.backend.CoreNSUnawareTypedAttribute;
import com.google.code.ddom.backend.CoreNamespaceDeclaration;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.CoreProcessingInstruction;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.CoreTextNode;
import com.google.code.ddom.backend.CoreTypedAttribute;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMCDATASection;
import com.google.code.ddom.frontend.dom.intf.DOMCharacterData;
import com.google.code.ddom.frontend.dom.intf.DOMChildNode;
import com.google.code.ddom.frontend.dom.intf.DOMComment;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentFragment;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentType;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.intf.DOMEntityReference;
import com.google.code.ddom.frontend.dom.intf.DOMLeafNode;
import com.google.code.ddom.frontend.dom.intf.DOMNSAwareElement;
import com.google.code.ddom.frontend.dom.intf.DOMNSAwareTypedAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMNSUnawareElement;
import com.google.code.ddom.frontend.dom.intf.DOMNSUnawareTypedAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMNamespaceDeclaration;
import com.google.code.ddom.frontend.dom.intf.DOMNode;
import com.google.code.ddom.frontend.dom.intf.DOMParentNode;
import com.google.code.ddom.frontend.dom.intf.DOMProcessingInstruction;
import com.google.code.ddom.frontend.dom.intf.DOMText;
import com.google.code.ddom.frontend.dom.intf.DOMTextNode;
import com.google.code.ddom.frontend.dom.intf.DOMTypedAttribute;

public aspect Parents {
    declare parents: @Implementation CoreAttribute+ implements DOMAttribute;
    declare parents: @Implementation CoreTypedAttribute+ implements DOMTypedAttribute;
    declare parents: @Implementation CoreNamespaceDeclaration+ implements DOMNamespaceDeclaration;
    declare parents: @Implementation CoreCharacterData+ implements DOMCharacterData;
    declare parents: @Implementation CoreComment+ implements DOMComment;
    declare parents: @Implementation CoreDocumentFragment+ implements DOMDocumentFragment;
    declare parents: @Implementation CoreDocument+ implements DOMDocument;
    declare parents: @Implementation CoreDocumentType+ implements DOMDocumentType;
    declare parents: @Implementation CoreElement+ implements DOMElement;
    declare parents: @Implementation CoreNSAwareElement+ implements DOMNSAwareElement;
    declare parents: @Implementation CoreNSUnawareElement+ implements DOMNSUnawareElement;
    declare parents: @Implementation CoreEntityReference+ implements DOMEntityReference;
    declare parents: @Implementation CoreParentNode+ implements DOMParentNode;
    declare parents: @Implementation CoreLeafNode+ implements DOMLeafNode;
    declare parents: @Implementation CoreNSAwareTypedAttribute+ implements DOMNSAwareTypedAttribute;
    declare parents: @Implementation CoreNSUnawareTypedAttribute+ implements DOMNSUnawareTypedAttribute;
    declare parents: @Implementation CoreNode+ implements DOMNode;
    declare parents: @Implementation CoreProcessingInstruction+ implements DOMProcessingInstruction;
    declare parents: @Implementation CoreChildNode+ implements DOMChildNode;
    declare parents: @Implementation CoreTextNode+ implements DOMTextNode;
    declare parents: @Implementation CoreText+ implements DOMText;
    declare parents: @Implementation CoreCDATASection+ implements DOMCDATASection;
}
