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

public class ModelDefinition implements Serializable {
    private static final long serialVersionUID = -5835186734017759182L;
    
    private final String backend;
    // TODO: this should probably be sorted if we want to use ModelDefinition as a key
    private final String[] frontends;
    
    ModelDefinition(String backend, String[] frontends) {
        this.backend = backend;
        this.frontends = frontends;
    }
    
    public String getBackend() {
        return backend;
    }

    public String[] getFrontends() {
        return frontends.clone();
    }
    
    // TODO: implement equals and hashCode
}
