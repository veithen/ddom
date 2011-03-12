/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist.support;

import com.googlecode.ddom.backend.linkedlist.intf.LLBuilder;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.stream.XmlHandler;

/**
 * Stores the state of the builder for a given parent information item in the original document. The
 * builder maintains a stack of input contexts, i.e. contexts are nested, and their nesting follows
 * the nesting of information items in the original document. Taking into account that the
 * underlying parser always points to a particular information item, the number of contexts active
 * at a given time is given by the depth of the current information item in the original document.
 * 
 * @author Andreas Veithen
 */
public class InputContext {
    private final LLBuilder builder;
    private LLParentNode targetNode;
    private XmlHandler passThroughHandler;
    
    public InputContext(LLBuilder builder) {
        this.builder = builder;
    }

    public LLBuilder getBuilder() {
        return builder;
    }
    
    public LLParentNode getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(LLParentNode targetNode) {
        this.targetNode = targetNode;
    }

    public XmlHandler getPassThroughHandler() {
        return passThroughHandler;
    }

    public void setPassThroughHandler(XmlHandler passThroughHandler) {
        this.passThroughHandler = passThroughHandler;
    }
}
