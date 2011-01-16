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
package com.google.code.ddom.frontend.saaj;

import java.util.Map;

import com.google.code.ddom.commons.cl.ClassCollection;
import com.google.code.ddom.commons.cl.ClassCollectionAggregate;
import com.google.code.ddom.commons.cl.Module;
import com.google.code.ddom.core.ext.ModelExtension;
import com.google.code.ddom.frontend.Frontend;
import com.google.code.ddom.frontend.dom.DOMFrontend;
import com.google.code.ddom.spi.Provider;

@Provider(name="saaj")
public class SAAJFrontend extends DOMFrontend {
    @Override
    public ClassCollection getMixins(Map<String,Frontend> frontends) {
        ClassCollectionAggregate aggregate = new ClassCollectionAggregate();
        aggregate.add(super.getMixins(frontends));
        aggregate.add(Module.forClass(SAAJFrontend.class).getPackage("com.google.code.ddom.frontend.saaj.mixin"));
        return aggregate;
    }

    public ModelExtension getModelExtension() {
        return new SAAJModelExtension();
    }
}
