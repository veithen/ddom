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
package com.googlecode.ddom.stream.dom;

import org.w3c.dom.Node;

import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlReader;

public class DOMInput extends XmlInput {
    private final Node rootNode;
    
    public DOMInput(Node rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    protected XmlReader createReader(XmlHandler handler) {
        return new DOMReader(handler, rootNode);
    }

    public void dispose() {
        // TODO Auto-generated method stub
        
    }
}
