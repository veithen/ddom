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
 * Controls how {@link CoreParentNode#coreGetTextContent(TextCollectorPolicy)} collects the text
 * content from child nodes.
 * 
 * @author Andreas Veithen
 */
public interface TextCollectorPolicy {
    /**
     * The default policy appropriate for the following use cases:
     * <ul>
     * <li>Retrieving the value of an attribute.
     * <li>Getting the content of a CDATA section, a processing instruction or a comment.
     * <li>Getting the value of an element the content of which is described by a simple type. 
     * </ul>
     */
    TextCollectorPolicy DEFAULT = new TextCollectorPolicy() {
        public Action getAction(int nodeType) {
            return nodeType == CoreNode.CDATA_SECTION_NODE ? Action.RECURSE : Action.FAIL;
        }
    };
    
    /**
     * Identifies the action to be executed by
     * {@link CoreParentNode#coreGetTextContent(TextCollectorPolicy)} when encountering a given type
     * of node.
     */
    enum Action {
        /**
         * Skip the node, i.e. don't collect text from it.
         */
        SKIP,
        
        /**
         * Recursively collect text from the node.
         */
        RECURSE,
        
        /**
         * Stop collecting text and return the text accumulated so far.
         */
        STOP,
        
        /**
         * Stop collecting text and throw an exception.
         */
        // TODO: specify exception type
        FAIL
    }

    /**
     * Determine the action to be executed for a given node. This method is only called for nodes
     * other than {@link CoreCharacterData}.
     * 
     * @param nodeType
     *            the type of node
     * @return the action to be executed; must never be <code>null</code>
     */
    Action getAction(int nodeType);
}
