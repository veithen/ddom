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

import com.google.code.ddom.core.model.DocumentImpl;
import com.google.code.ddom.core.model.ElementImpl;
import com.google.code.ddom.core.model.LeafNodeImpl;
import com.google.code.ddom.core.model.NSAwareElementImpl;
import com.google.code.ddom.core.model.NSAwareTypedAttributeImpl;
import com.google.code.ddom.core.model.NodeImpl;
import com.google.code.ddom.core.model.ProcessingInstructionImpl;
import com.google.code.ddom.core.model.TextImpl;
import com.google.code.ddom.core.model.TextNodeImpl;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomChildNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomLeafNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomNode;
import com.google.code.ddom.frontend.axiom.intf.AxiomProcessingInstruction;
import com.google.code.ddom.frontend.axiom.intf.AxiomText;
import com.google.code.ddom.frontend.axiom.intf.AxiomTextNode;

public aspect Parents {
    declare parents: DocumentImpl implements AxiomDocument;
    declare parents: NSAwareElementImpl implements AxiomElement;
    declare parents: TextNodeImpl implements AxiomTextNode;
    declare parents: TextImpl implements AxiomText;
    declare parents: ProcessingInstructionImpl implements AxiomProcessingInstruction;
    declare parents: NodeImpl implements AxiomNode;
    declare parents: LeafNodeImpl implements AxiomLeafNode;
    declare parents: (LeafNodeImpl || ElementImpl) implements AxiomChildNode;
    declare parents: NSAwareTypedAttributeImpl implements AxiomAttribute;
}
