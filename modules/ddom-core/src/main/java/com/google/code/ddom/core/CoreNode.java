/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.core;

public interface CoreNode {
    /**
     * Get the node factory for the model to which this node belongs. Since there is a single
     * {@link NodeFactory} instance for each model (i.e. back-end and front-end combination), the
     * return value of this method may be used to determine whether two nodes belong to the same
     * model.
     * 
     * @return the node factory; the return value MUST be the singleton instance described in the
     *         documentation of the {@link NodeFactory} interface
     */
    NodeFactory coreGetNodeFactory();
    
    /**
     * Get the owner document to which this node belongs. The concept and semantics of owner
     * documents are defined in the manual.
     * 
     * @param create
     *            indicates whether the owner document should be created if it has not been created
     *            yet
     * @return the owner document or <code>null</code> if the owner document has not been created
     *         yet and <code>create</code> is <code>false</code>
     */
    CoreDocument coreGetOwnerDocument(boolean create);
    
    /**
     * Determines if this node has the same owner document as another node.
     * 
     * @param other
     *            the other node
     * @return <code>true</code> if the two nodes have the same owner document; <code>false</code>
     *         otherwise
     */
    boolean coreIsSameOwnerDocument(CoreNode other);
}
