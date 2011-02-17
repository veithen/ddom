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
package com.googlecode.ddom.model;

import java.io.Serializable;
import java.util.SortedSet;

/**
 * A model definition. A model is defined by a back-end implementation and a set
 * of one or more front-end implementations.
 * <p>
 * This is an immutable class that can be used as a cache key.
 * 
 * @author Andreas Veithen
 */
public class ModelDefinition implements Serializable {
    private static final long serialVersionUID = -5835186734017759182L;
    
    private final String backend;
    private final SortedSet<String> frontends;
    
    ModelDefinition(String backend, SortedSet<String> frontends) {
        this.backend = backend;
        this.frontends = frontends;
    }
    
    public String getBackend() {
        return backend;
    }

    public String[] getFrontends() {
        return frontends.toArray(new String[frontends.size()]);
    }

    @Override
    public int hashCode() {
        return backend.hashCode() + frontends.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModelDefinition) {
            ModelDefinition other = (ModelDefinition)obj;
            return backend.equals(other.backend) && frontends.equals(other.frontends);
        } else {
            return false;
        }
    }
}
