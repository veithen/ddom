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
package com.google.code.ddom.frontend.saaj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.code.ddom.frontend.dom.DOMFrontend;
import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.model.Frontend;

@Provider(name="saaj")
public class SAAJFrontend extends DOMFrontend {
    @Override
    public List<String> getAspectClasses(Map<String,Frontend> frontends) {
        List<String> aspects = new ArrayList<String>(super.getAspectClasses(frontends));
        aspects.add("com.google.code.ddom.frontend.saaj.aspects.AttributeAsName");
        aspects.add("com.google.code.ddom.frontend.saaj.aspects.NodeSupport");
        aspects.add("com.google.code.ddom.frontend.saaj.aspects.Parents");
        aspects.add("com.google.code.ddom.frontend.saaj.aspects.SOAPElementSupport");
        return aspects;
    }
}
