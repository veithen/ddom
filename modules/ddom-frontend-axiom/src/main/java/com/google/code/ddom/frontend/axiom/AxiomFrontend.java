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
package com.google.code.ddom.frontend.axiom;

import java.util.Map;

import com.google.code.ddom.commons.cl.ClassCollection;
import com.google.code.ddom.commons.cl.ClassCollectionAggregate;
import com.google.code.ddom.commons.cl.EmptyClassCollection;
import com.google.code.ddom.commons.cl.Module;
import com.google.code.ddom.core.NodeFactory;
import com.google.code.ddom.core.ext.ModelExtension;
import com.google.code.ddom.frontend.APIObjectFactory;
import com.google.code.ddom.frontend.Frontend;
import com.google.code.ddom.spi.Provider;

@Provider(name="axiom")
public class AxiomFrontend implements Frontend {
    public ClassCollection getMixins(Map<String,Frontend> frontends) {
        ClassCollectionAggregate aggregate = new ClassCollectionAggregate();
        Module module = Module.forClass(AxiomFrontend.class);
        aggregate.add(module.getPackage("com.google.code.ddom.frontend.axiom.mixin"));
        if (!frontends.containsKey("dom")) {
            aggregate.add(module.getPackage("com.google.code.ddom.frontend.axiom.mixin.dom"));
        }
        return aggregate;
    }

    public ClassCollection getModelExtensionInterfaces() {
        return EmptyClassCollection.INSTANCE;
    }

    public ModelExtension getModelExtension() {
        return null;
    }

    public APIObjectFactory getAPIObjectFactory(NodeFactory nodeFactory) {
        return null;
    }
}
