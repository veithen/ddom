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
package com.google.code.ddom.model;

import java.io.Serializable;
import java.util.SortedSet;

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

    // TODO: clean this up
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((backend == null) ? 0 : backend.hashCode());
        result = prime * result + ((frontends == null) ? 0 : frontends.hashCode());
        return result;
    }

    // TODO: clean this up
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModelDefinition other = (ModelDefinition) obj;
        if (backend == null) {
            if (other.backend != null)
                return false;
        } else if (!backend.equals(other.backend))
            return false;
        if (frontends == null) {
            if (other.frontends != null)
                return false;
        } else if (!frontends.equals(other.frontends))
            return false;
        return true;
    }
}
