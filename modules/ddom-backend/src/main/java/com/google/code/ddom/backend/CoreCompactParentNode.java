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
package com.google.code.ddom.backend;

/**
 * Parent node able to store a single text node without creating a {@link CoreText} instance.
 * 
 * @author Andreas Veithen
 */
public interface CoreCompactParentNode extends CoreParentNode {
    /**
     * 
     * either a String or a ChildNode
     * 
     * @return
     */
    Object coreGetContent();

    /**
     * Set the content of this node to the given value. This will remove all children previously
     * owned by this element.
     * 
     * @param value
     *            the value to set
     */
    void coreSetValue(String value);
    
    // TODO: specify behavior if the element neither has children nor a value
    boolean coreIsExpanded();
}
