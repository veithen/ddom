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
package com.googlecode.ddom.core;

/**
 * Indicates that an operation could not be completed because a node is expected to have a parent,
 * but didn't have one.
 * 
 * @author Andreas Veithen
 */
public class NoParentException extends HierarchyException {
    private static final long serialVersionUID = 7197332472239819416L;

    public NoParentException() {
    }

    public NoParentException(String message) {
        super(message);
    }
}
