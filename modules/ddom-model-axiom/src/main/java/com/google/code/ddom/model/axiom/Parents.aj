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
package com.google.code.ddom.model.axiom;

import com.google.code.ddom.core.model.*;

public aspect Parents {
    declare parents: DocumentImpl implements AxiomDocument;
    declare parents: NSAwareElementImpl implements AxiomElement;
    declare parents: TextNodeImpl implements AxiomTextNode;
    declare parents: ProcessingInstructionImpl implements AxiomProcessingInstruction;
    declare parents: NodeImpl implements AxiomNode;
    declare parents: LeafNodeImpl implements AxiomLeafNode;
}
