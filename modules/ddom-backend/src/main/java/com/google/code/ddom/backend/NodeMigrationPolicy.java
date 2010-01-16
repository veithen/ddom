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
 * Defines the policy to apply when the core model is requested to insert a node that already has a
 * parent or is owned by a different document.
 * 
 * @author Andreas Veithen
 */
public interface NodeMigrationPolicy {
    public enum Action {
        /**
         * Reject the operation and trigger an appropriate exception.
         * TODO: specify exceptions
         */
        REJECT,

        /**
         * Move the node. If the node is owned by the same document, it will simply be detached from
         * its parent using {@link CoreChildNode#coreDetach()}. If it is owned by a different
         * document, it will be adopted using [TODO]. This action is only applicable if the node is
         * an instance from the same model.
         */
        MOVE,
        
        /**
         * Clone the node, i.e. insert a (deep) copy instead of the original node.
         * TODO: specify methods that will be used
         */
        CLONE
    }
    
    /**
     * 
     * @param hasParent
     *            <code>true</code> if the node to migrate has a parent (or an owner element in the
     *            case of {@link CoreAttribute}); <code>false</code> if the node is detached
     * @param isForeignDocument
     *            <code>true</code> if the node to migrate belongs to a different document
     * @param isForeignModel
     *            <code>true</code> if the node to migrate belongs to a different model. Note that
     *            this parameter can only be <code>true</code> if <code>isForeignDocument</code> is
     *            also <code>true</code>
     * @return
     */
    Action getAction(boolean hasParent, boolean isForeignDocument, boolean isForeignModel);
}
