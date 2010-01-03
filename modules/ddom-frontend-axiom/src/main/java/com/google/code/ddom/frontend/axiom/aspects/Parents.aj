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

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreLeafNode;
import com.google.code.ddom.backend.CoreNSAwareElement;
import com.google.code.ddom.backend.CoreNSAwareAttribute;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreProcessingInstruction;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.CoreTextNode;
import com.google.code.ddom.backend.Implementation;
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
    declare parents: @Implementation CoreDocument+ implements AxiomDocument;
    declare parents: @Implementation CoreNSAwareElement+ implements AxiomElement;
    declare parents: @Implementation CoreTextNode+ implements AxiomTextNode;
    declare parents: @Implementation CoreText+ implements AxiomText;
    declare parents: @Implementation CoreProcessingInstruction+ implements AxiomProcessingInstruction;
    declare parents: @Implementation CoreNode+ implements AxiomNode;
    declare parents: @Implementation CoreLeafNode+ implements AxiomLeafNode;
    declare parents: @Implementation CoreChildNode+ implements AxiomChildNode;
    declare parents: @Implementation CoreNSAwareAttribute+ implements AxiomAttribute;
}
