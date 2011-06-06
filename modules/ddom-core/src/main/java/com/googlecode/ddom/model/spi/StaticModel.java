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
package com.googlecode.ddom.model.spi;

import java.io.Serializable;

import com.googlecode.ddom.model.ModelDefinition;

/**
 * Contains the information about a statically woven model.
 * 
 * @author Andreas Veithen
 */
public class StaticModel implements Serializable {
    private static final long serialVersionUID = 7608492816426921776L;

    private final ModelDefinition definition;
    private final String nodeFactoryClassName;
    
    public StaticModel(ModelDefinition definition, String nodeFactoryClassName) {
        this.definition = definition;
        this.nodeFactoryClassName = nodeFactoryClassName;
    }

    public ModelDefinition getModelDefinition() {
        return definition;
    }

    public String getNodeFactoryClassName() {
        return nodeFactoryClassName;
    }
}