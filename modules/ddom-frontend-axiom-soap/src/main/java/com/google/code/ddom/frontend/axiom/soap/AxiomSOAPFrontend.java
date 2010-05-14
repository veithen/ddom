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
package com.google.code.ddom.frontend.axiom.soap;

import java.util.Map;

import com.google.code.ddom.commons.cl.ClassCollection;
import com.google.code.ddom.commons.cl.Module;
import com.google.code.ddom.frontend.Frontend;
import com.google.code.ddom.frontend.axiom.AxiomFrontend;
import com.google.code.ddom.spi.Provider;

@Provider(name="axiom-soap")
public class AxiomSOAPFrontend extends AxiomFrontend {
    @Override
    public ClassCollection getMixins(Map<String, Frontend> frontends) {
        // TODO Auto-generated method stub
        return super.getMixins(frontends);
    }

    @Override
    public ClassCollection getModelExtensionInterfaces() {
        return Module.forClass(AxiomSOAPFrontend.class).getPackage("com.google.code.ddom.frontend.axiom.soap.ext");
    }
}