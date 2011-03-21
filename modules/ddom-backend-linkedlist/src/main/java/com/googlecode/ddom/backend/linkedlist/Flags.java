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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.CoreCharacterData;

public class Flags {
    private Flags() {}
    
    public static final int STATE_EXPANDED = 0;
    
    public static final int STATE_VALUE_SET = 1;
    
    public static final int STATE_CONTENT_SET = 2;
    
    public static final int STATE_SOURCE_SET = 3;
    
    public static final int STATE_ATTRIBUTES_PENDING = 4;
    
    public static final int STATE_CHILDREN_PENDING = 5;
    
    public static final int STATE_CONSUMED = 6;
    
    /**
     * Defines the bit mask for the part of the flags that indicate the state of a
     * {@link LLParentNode}.
     */
    public static final int STATE_MASK = 7;
    
    /**
     * Used by {@link LLChildNode} instances to indicate whether the node has a parent or not.
     * This is necessary to interpret the meaning of the <code>owner</code> attribute if it
     * refers to a document node (which may be the parent or simply the owner document).
     */
    public static final int HAS_PARENT = 8;
    
    /**
     * Used to store the information returned by {@link CoreCharacterData#coreIsIgnorable()}.
     */
    public static final int IGNORABLE = 16;
}
