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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.model.Frontend;

@Provider(name="axiom")
public class AxiomFrontend implements Frontend {
    public List<String> getMixins(Map<String,Frontend> frontends) {
        List<String> mixins = new ArrayList<String>();
        mixins.addAll(Arrays.asList(new String[] {
                "com.google.code.ddom.frontend.axiom.mixin.AttributeSupport",
                "com.google.code.ddom.frontend.axiom.mixin.CDATASectionSupport",
                "com.google.code.ddom.frontend.axiom.mixin.ChildNodeSupport",
                "com.google.code.ddom.frontend.axiom.mixin.CommentSupport",
                "com.google.code.ddom.frontend.axiom.mixin.ContainerSupport",
                "com.google.code.ddom.frontend.axiom.mixin.DocumentSupport",
                "com.google.code.ddom.frontend.axiom.mixin.ElementSupport",
                "com.google.code.ddom.frontend.axiom.mixin.LeafNodeSupport",
                "com.google.code.ddom.frontend.axiom.mixin.NamedNodeSupport",
                "com.google.code.ddom.frontend.axiom.mixin.NamespaceDeclarationSupport",
                "com.google.code.ddom.frontend.axiom.mixin.NodeSupport",
//                "com.google.code.ddom.frontend.axiom.aspects.Parents",
                "com.google.code.ddom.frontend.axiom.mixin.ProcessingInstructionSupport",
//                "com.google.code.ddom.frontend.axiom.aspects.QNameCaching", // TODO: should be configurable
                "com.google.code.ddom.frontend.axiom.mixin.TextNodeSupport",
                "com.google.code.ddom.frontend.axiom.mixin.TextSupport",
        }));
        if (!frontends.containsKey("dom")) {
            mixins.add("com.google.code.ddom.frontend.axiom.mixin.dom.NamedNodeSupport");
            mixins.add("com.google.code.ddom.frontend.axiom.mixin.dom.ProcessingInstructionSupport");
        }
        return mixins;
    }
}
