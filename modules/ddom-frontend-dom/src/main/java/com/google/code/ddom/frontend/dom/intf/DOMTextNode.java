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
package com.google.code.ddom.frontend.dom.intf;

import org.w3c.dom.Text;

import com.google.code.ddom.core.CoreTextNode;

public interface DOMTextNode extends CoreTextNode, Text, DOMCharacterData {
    /**
     * Create a new text node of the same type as this one.
     * 
     * @param data the data for the new text node
     * @return the new text node
     */
    DOMTextNode createNewTextNode(String data);
}
