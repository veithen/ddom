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

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class ModelDefinitionTest {
    @Test
    public void testEqualsAndHashCode() {
        ModelBuilder modelBuilder1 = new ModelBuilder();
        modelBuilder1.setBackend("backend");
        modelBuilder1.addFrontend("frontend1");
        modelBuilder1.addFrontend("frontend2");
        ModelDefinition model1 = modelBuilder1.buildModelDefinition();

        ModelBuilder modelBuilder2 = new ModelBuilder();
        modelBuilder2.setBackend("backend");
        modelBuilder2.addFrontend("frontend2");
        modelBuilder2.addFrontend("frontend1");
        ModelDefinition model2 = modelBuilder2.buildModelDefinition();
        
        Set<ModelDefinition> set = new HashSet<ModelDefinition>();
        set.add(model1);
        Assert.assertTrue(set.contains(model2));
        set.clear();
        set.add(model2);
        Assert.assertTrue(set.contains(model1));
    }
}
