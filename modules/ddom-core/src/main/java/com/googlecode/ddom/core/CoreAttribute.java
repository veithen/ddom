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
package com.googlecode.ddom.core;

/**
 * Represents an attribute information item.
 * 
 * @author Andreas Veithen
 */
public interface CoreAttribute extends CoreParentNode {
    /**
     * Get the owner element of this attribute.
     * 
     * @return the owner element of the attribute
     */
    CoreElement coreGetOwnerElement();
    
    /**
     * Check if this node has an owner element.
     * 
     * @return <code>true</code> if and only if this node currently has an owner element
     */
    boolean coreHasOwnerElement();
    
    /**
     * Remove this attribute from its owner element. The attribute will keep its current owner
     * document.
     * 
     * @return <code>true</code> if the attribute had an owner element and has been removed from
     *         that element; <code>false</code> if the attribute didn't have an owner element and no
     *         changes have been made
     */
    boolean coreRemove();
    
    /**
     * Remove this attribute from its owner element and assign it to a new owner document. The owner
     * document will always be changed, even if the attribute has no owner element.
     * 
     * @return <code>true</code> if the attribute had an owner element and has been removed from
     *         that element; <code>false</code> if the attribute didn't have an owner element
     */
    boolean coreRemove(CoreDocument document);
    
    /**
     * Get the attribute immediately following the current attribute.
     * 
     * @return the next attribute, or <code>null</code> if the attribute is the last attribute of
     *         its owner element or if the attribute has no owner element
     * @throws DeferredParsingException 
     */
    CoreAttribute coreGetNextAttribute() throws DeferredParsingException;
    
    /**
     * Get the attribute immediately preceding the current attribute.
     * 
     * @return the previous attribute, or <code>null</code> if the attribute is the first attribute
     *         of its owner element or if the attribute has no owner element
     */
    CoreAttribute coreGetPreviousAttribute();
}
