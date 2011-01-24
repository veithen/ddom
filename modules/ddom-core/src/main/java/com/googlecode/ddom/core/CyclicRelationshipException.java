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
 * Indicates that an attempt was made to perform an operation that would turn the document tree into
 * a cyclic graph.
 * 
 * @author Andreas Veithen
 */
public class CyclicRelationshipException extends HierarchyException {
    private static final long serialVersionUID = 6161496839157477471L;

    public CyclicRelationshipException() {
    }

    public CyclicRelationshipException(String message) {
        super(message);
    }
}
