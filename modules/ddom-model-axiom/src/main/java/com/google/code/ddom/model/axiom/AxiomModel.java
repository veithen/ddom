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
package com.google.code.ddom.model.axiom;

import java.util.Arrays;
import java.util.List;

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.model.Model;

@Provider(name="axiom")
public class AxiomModel implements Model {
    public List<String> getAspectClasses() {
        return Arrays.asList(new String[] {
                "com.google.code.ddom.model.axiom.ChildNodeSupport",
                "com.google.code.ddom.model.axiom.DeferredParsing",
                "com.google.code.ddom.model.axiom.Deprecated",
                "com.google.code.ddom.model.axiom.Factory",
                "com.google.code.ddom.model.axiom.NodeType",
                "com.google.code.ddom.model.axiom.Parents",
                "com.google.code.ddom.model.axiom.Serialization",
        });
    }
}
