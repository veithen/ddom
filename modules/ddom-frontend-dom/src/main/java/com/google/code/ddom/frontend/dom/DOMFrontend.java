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
package com.google.code.ddom.frontend.dom;

import java.util.Map;

import com.google.code.ddom.commons.cl.ClassCollection;
import com.google.code.ddom.commons.cl.Module;
import com.google.code.ddom.frontend.dom.support.APIObjectFactoryImpl;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.frontend.APIObjectFactory;
import com.googlecode.ddom.frontend.Frontend;
import com.googlecode.ddom.spi.Provider;

@Provider(name="dom")
public class DOMFrontend implements Frontend {
    public ClassCollection getMixins(Map<String,Frontend> frontends) {
        return Module.forClass(DOMFrontend.class).getPackage("com.google.code.ddom.frontend.dom.mixin");
    }

    public ModelExtension getModelExtension() {
        return null;
    }

    public APIObjectFactory getAPIObjectFactory(NodeFactory nodeFactory) {
        return new APIObjectFactoryImpl(nodeFactory);
    }
}
