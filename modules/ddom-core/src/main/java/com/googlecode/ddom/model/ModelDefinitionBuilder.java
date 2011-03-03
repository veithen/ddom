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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

// TODO: rename this ModelConfigurator?
public class ModelDefinitionBuilder {
    private String backend = "linkedlist";
    private List<String> frontends = new ArrayList<String>();

    public void setBackend(String backend) {
        if (backend == null) {
            throw new IllegalArgumentException("backend may not be null");
        }
        this.backend = backend;
    }
    
    // TODO: this method should have a parameter to pass properties to the frontend
    public void addFrontend(String frontend) {
        if (frontend == null) {
            throw new IllegalArgumentException("frontend may not be null");
        }
        frontends.add(frontend);
    }
    
    public ModelDefinition buildModelDefinition() {
        return new ModelDefinition(backend, new TreeSet<String>(frontends));
    }
    
    public static ModelDefinition buildModelDefinition(String frontend) {
        ModelDefinitionBuilder builder = new ModelDefinitionBuilder();
        builder.addFrontend(frontend);
        return builder.buildModelDefinition();
    }
}