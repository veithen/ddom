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
package com.googlecode.ddom.frontend.dom.support;

import com.googlecode.ddom.core.CoreNode;
import com.googlecode.ddom.core.NodeMigrationPolicy;
import com.googlecode.ddom.core.TextCollectorPolicy;

public final class Policies {
    private Policies() {}
    
    public static final NodeMigrationPolicy ATTRIBUTE_MIGRATION_POLICY = new NodeMigrationPolicy() {
        public Action getAction(boolean hasParent, boolean isForeignDocument, boolean isForeignModel) {
            return Action.REJECT;
        }
    };
    
    public static final NodeMigrationPolicy NODE_MIGRATION_POLICY = new NodeMigrationPolicy() {
        public Action getAction(boolean hasParent, boolean isForeignDocument, boolean isForeignModel) {
            return isForeignDocument ? Action.REJECT : Action.MOVE;
        }
    };
    
    /**
     * {@link TextCollectorPolicy} implementation for {@link org.w3c.dom.Node#getTextContent()}.
     */
    public static final TextCollectorPolicy GET_TEXT_CONTENT = new TextCollectorPolicy() {
        public Action getAction(int nodeType, boolean textSeen) {
            // TODO: need to make sure that we have enough unit test coverage for this
            return nodeType == CoreNode.CDATA_SECTION_NODE ? Action.RECURSE : Action.SKIP;
        }
    };
}