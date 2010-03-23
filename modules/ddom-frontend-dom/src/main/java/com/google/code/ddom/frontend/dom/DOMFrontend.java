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
package com.google.code.ddom.frontend.dom;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.code.ddom.spi.Provider;
import com.google.code.ddom.spi.model.Frontend;

@Provider(name="dom")
public class DOMFrontend implements Frontend {
    public List<String> getAspectClasses(Map<String,Frontend> frontends) {
        return Arrays.asList(new String[] {
                "com.google.code.ddom.frontend.dom.aspects.AttributeSupport",
                "com.google.code.ddom.frontend.dom.aspects.CharacterDataSupport",
                "com.google.code.ddom.frontend.dom.aspects.Clone",
                "com.google.code.ddom.frontend.dom.aspects.Create",
                "com.google.code.ddom.frontend.dom.aspects.DocumentFragmentSupport",
                "com.google.code.ddom.frontend.dom.aspects.DocumentSupport",
                "com.google.code.ddom.frontend.dom.aspects.DocumentTypeSupport",
                "com.google.code.ddom.frontend.dom.aspects.ElementSupport",
                "com.google.code.ddom.frontend.dom.aspects.GetElementsBy",
                "com.google.code.ddom.frontend.dom.aspects.Hierarchy",
                "com.google.code.ddom.frontend.dom.aspects.LeafNodeSupport",
                "com.google.code.ddom.frontend.dom.aspects.NamedNodeSupport",
                "com.google.code.ddom.frontend.dom.aspects.NamespaceLookup",
                "com.google.code.ddom.frontend.dom.aspects.NodeName",
                "com.google.code.ddom.frontend.dom.aspects.NodeSupport",
                "com.google.code.ddom.frontend.dom.aspects.NodeType",
                "com.google.code.ddom.frontend.dom.aspects.NodeValue",
                "com.google.code.ddom.frontend.dom.aspects.Normalization",
                "com.google.code.ddom.frontend.dom.aspects.ParentNodeSupport",
                "com.google.code.ddom.frontend.dom.aspects.Parents",
                "com.google.code.ddom.frontend.dom.aspects.ProcessingInstructionSupport",
                "com.google.code.ddom.frontend.dom.aspects.SchemaTypeInfo",
                "com.google.code.ddom.frontend.dom.aspects.Sibling",
                "com.google.code.ddom.frontend.dom.aspects.TextContent",
                "com.google.code.ddom.frontend.dom.aspects.TextSupport",
                "com.google.code.ddom.frontend.dom.aspects.UserDataSupport" });
    }
}
