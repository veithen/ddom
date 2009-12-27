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

import org.apache.axiom.om.OMNode;

import com.google.code.ddom.frontend.axiom.intf.AxiomCDATASection;
import com.google.code.ddom.frontend.axiom.intf.AxiomComment;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomProcessingInstruction;
import com.google.code.ddom.frontend.axiom.intf.AxiomText;

public aspect NodeType {
    public final int AxiomElement.getType() {
        return OMNode.ELEMENT_NODE;
    }
    
    public final int AxiomText.getType() {
        return OMNode.TEXT_NODE;
    }
    
    public final int AxiomCDATASection.getType() {
        return OMNode.CDATA_SECTION_NODE;
    }
    
    public final int AxiomComment.getType() {
        return OMNode.COMMENT_NODE;
    }
    
    public final int AxiomProcessingInstruction.getType() {
        return OMNode.PI_NODE;
    }
}