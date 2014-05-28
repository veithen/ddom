/*
 * Copyright 2009-2011,2014 Andreas Veithen
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
package com.googlecode.ddom.backend.testsuite;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.NSAwareAttributeMatcher;
import com.googlecode.ddom.core.NodeMigrationPolicy;

public final class Policies {
    private Policies() {}
    
    public static final AttributeMatcher NSAWARE_ATTRIBUTE_MATCHER = new NSAwareAttributeMatcher(false, false);

    public static final NodeMigrationPolicy REQUIRE_SAME_DOCUMENT = new NodeMigrationPolicy() {
        public Action getAction(boolean hasParent, boolean isForeignDocument, boolean isForeignModel) {
            return isForeignDocument ? Action.REJECT : Action.MOVE;
        }
    };
    
    public static final NodeMigrationPolicy REJECT = new NodeMigrationPolicy() {
        public Action getAction(boolean hasParent, boolean isForeignDocument, boolean isForeignModel) {
            return Action.REJECT;
        }
    };
    
    public static final NodeMigrationPolicy MOVE = new NodeMigrationPolicy() {
        public Action getAction(boolean hasParent, boolean isForeignDocument, boolean isForeignModel) {
            return Action.MOVE;
        }
    };
}
