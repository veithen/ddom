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
package com.google.code.ddom.dom.model;

import org.w3c.dom.Node;

public interface DOMNode extends Node {
    /**
     * Get the document to which this node belongs. In contrast to {@link Node#getOwnerDocument()},
     * this method will never return <code>null</code>.
     * 
     * @return the document
     */
    DOMDocument getDocument();
    
    CharSequence collectTextContent(CharSequence appendTo);
}
