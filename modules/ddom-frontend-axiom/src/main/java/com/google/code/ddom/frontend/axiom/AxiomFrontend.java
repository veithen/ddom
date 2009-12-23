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
package com.google.code.ddom.frontend.axiom;

import java.util.Arrays;
import java.util.List;

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.model.Frontend;

@Provider(name="axiom")
public class AxiomFrontend implements Frontend {
    public List<String> getAspectClasses() {
        return Arrays.asList(new String[] {
                "com.google.code.ddom.frontend.axiom.aspects.AttributeSupport",
                "com.google.code.ddom.frontend.axiom.aspects.ChildNodeSupport",
                "com.google.code.ddom.frontend.axiom.aspects.ContainerSupport",
                "com.google.code.ddom.frontend.axiom.aspects.DeferredParsing",
                "com.google.code.ddom.frontend.axiom.aspects.Deprecated",
                "com.google.code.ddom.frontend.axiom.aspects.DocumentSupport",
                "com.google.code.ddom.frontend.axiom.aspects.ElementSupport",
                "com.google.code.ddom.frontend.axiom.aspects.ElementText",
                "com.google.code.ddom.frontend.axiom.aspects.Factory",
                "com.google.code.ddom.frontend.axiom.aspects.LineNumber",
                "com.google.code.ddom.frontend.axiom.aspects.NamedNodeSupport",
                "com.google.code.ddom.frontend.axiom.aspects.NamespaceDeclarations",
                "com.google.code.ddom.frontend.axiom.aspects.NodeType",
                "com.google.code.ddom.frontend.axiom.aspects.Parents",
                "com.google.code.ddom.frontend.axiom.aspects.ProcessingInstructionSupport",
                "com.google.code.ddom.frontend.axiom.aspects.QNameCaching", // TODO: should be configurable
                "com.google.code.ddom.frontend.axiom.aspects.Serialization",
                "com.google.code.ddom.frontend.axiom.aspects.TextSupport",
        });
    }
}
