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

public interface CoreAttribute extends CoreCompactParentNode {
    /**
     * Get the owner element of this attribute.
     * 
     * @return the owner element of the attribute
     */
    CoreElement coreGetOwnerElement();
    
    /**
     * Get the attribute immediately following the current attribute.
     * 
     * @return the next attribute, or <code>null</code> if the attribute is the last attribute of
     *         its owner element or if the attribute has no owner element
     */
    CoreAttribute coreGetNextAttribute();
    
    /**
     * Get the attribute immediately preceding the current attribute.
     * 
     * @return the previous attribute, or <code>null</code> if the attribute is the first attribute
     *         of its owner element or if the attribute has no owner element
     */
    CoreAttribute coreGetPreviousAttribute();
    
    void internalSetNextAttribute(CoreAttribute attr);
    void internalSetOwnerElement(CoreElement newOwner);
    
    /**
     * Insert a new attribute after this instance. Note that this method will NOT check if the
     * element already has an attribute with the same name, and always insert the attribute.
     * 
     * @param attr
     *            the new attribute
     */
    // TODO: check if we still need this as public method
    void coreInsertAttributeAfter(CoreAttribute attr);
}
